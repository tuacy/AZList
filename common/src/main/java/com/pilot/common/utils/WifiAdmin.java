package com.pilot.common.utils;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.List;

public class WifiAdmin {

	public interface EnableNetworkCallback {

		void onStart();

		void onFailure();

		void onConnectSuccess();

		void onEnd();
	}

	public enum WifiCipherType {
		WIFI_CIPHER_WEP,
		WIFI_CIPHER_WPA,
		WIFI_CIPHER_NO_PASS,
		WIFI_CIPHER_INVALID
	}

	private WifiManager          mWifiManager;
	private WifiManager.WifiLock mWifiLock;
	private Context              mContext;

	public WifiAdmin(Context context) {
		mContext = context;
		mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
	}

	public WifiManager.WifiLock createWifiLock() {
		return mWifiManager.createWifiLock(WifiAdmin.class.getSimpleName());
	}

	public void acquireWifiLock() {
		mWifiLock.acquire();
	}

	public void releaseWifiLock() {
		if (mWifiLock.isHeld()) {
			mWifiLock.acquire();
		}
	}

	public WifiInfo getConnectionInfo() {
		return mWifiManager.getConnectionInfo();
	}

	public int getWifiState() {
		return mWifiManager.getWifiState();
	}

	public void enableNetwork(int netId, boolean disableOthers, EnableNetworkCallback callback) {
		new WifiConnector(netId, disableOthers, callback).start();
	}

	public int addWifiInfo(String SSID, String password) {
		WifiCipherType type = WifiCipherType.WIFI_CIPHER_WPA;
		if (password.equals("")) {
			type = WifiCipherType.WIFI_CIPHER_NO_PASS;
		}
		return addWifiInfo(SSID, password, type);
	}

	public int addWifiInfo(String SSID, String Password, WifiCipherType Type) {
		WifiConfiguration wifiConfig = createWifiInfo(SSID, Password, Type);
		if (wifiConfig == null) {
			return -1;
		}
		removeNetwork(SSID);
		return mWifiManager.addNetwork(wifiConfig);
	}

	public void removeNetwork(String SSID) {
		List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
		if (existingConfigs != null) {
			for (WifiConfiguration existingConfig : existingConfigs) {
				if (existingConfig.SSID.equalsIgnoreCase("\"" + SSID + "\"") || existingConfig.SSID.equalsIgnoreCase(SSID)) {
					mWifiManager.removeNetwork(existingConfig.networkId);
				}
			}
		}
	}

	public WifiConfiguration createWifiInfo(String SSID, String Password, WifiCipherType Type) {
		WifiConfiguration config = new WifiConfiguration();
		config.allowedAuthAlgorithms.clear();
		config.allowedGroupCiphers.clear();
		config.allowedKeyManagement.clear();
		config.allowedPairwiseCiphers.clear();
		config.allowedProtocols.clear();
		config.SSID = "\"" + SSID + "\"";
		if (Type == WifiCipherType.WIFI_CIPHER_NO_PASS) {
			config.hiddenSSID = true;
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
		} else if (Type == WifiCipherType.WIFI_CIPHER_WEP) {
			config.preSharedKey = "\"" + Password + "\"";
			config.hiddenSSID = true;
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			config.wepTxKeyIndex = 0;
		} else if (Type == WifiCipherType.WIFI_CIPHER_WPA) {
			config.preSharedKey = "\"" + Password + "\"";
			config.hiddenSSID = true;
			config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
			config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
			config.status = WifiConfiguration.Status.ENABLED;
		} else {
			return null;
		}
		return config;
	}

	public WifiConfiguration isExist(String SSID) {
		List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
		for (WifiConfiguration existingConfig : existingConfigs) {
			if (existingConfig.SSID.equals("\"" + SSID + "\"") || existingConfig.SSID.equals(SSID)) {
				return existingConfig;
			}
		}
		return null;
	}

	public class WifiConnector extends Thread {

		private static final int DEFAULT_TIMEOUT_MILLIS = 1000 * 7;

		private static final int CONNECT_MESSAGE_START   = 0;
		private static final int CONNECT_MESSAGE_SUCCESS = 1;
		private static final int CONNECT_MESSAGE_FAILURE = 2;
		private static final int CONNECT_MESSAGE_END     = 3;

		private int                   mNetId;
		private boolean               mDisableOthers;
		private EnableNetworkCallback mCallback;
		private Handler               mUiHandler;
		private int                   mRetry;

		private Handler.Callback mHandleCallback = new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				if (mCallback == null) {
					return true;
				}
				switch (msg.what) {
					case CONNECT_MESSAGE_START:
						mCallback.onStart();
						break;
					case CONNECT_MESSAGE_FAILURE:
						mCallback.onFailure();
						break;
					case CONNECT_MESSAGE_SUCCESS:
						mCallback.onConnectSuccess();
						break;
					case CONNECT_MESSAGE_END:
						mCallback.onEnd();
						break;

				}
				return true;
			}
		};

		public WifiConnector(int netId, boolean disableOthers, EnableNetworkCallback callback) {
			this.mNetId = netId;
			this.mDisableOthers = disableOthers;
			this.mCallback = callback;
			this.mUiHandler = new Handler(Looper.getMainLooper(), mHandleCallback);
			this.mRetry = 3;
		}

		@Override
		public void run() {

			mUiHandler.sendEmptyMessage(CONNECT_MESSAGE_START);
			boolean success = false;
			for (int i = 0; i < mRetry; i++) {
				success = startConnect();
				if (success) {
					break;
				}
			}
			if (success) {
				mUiHandler.sendEmptyMessage(CONNECT_MESSAGE_SUCCESS);
			} else {
				mUiHandler.sendEmptyMessage(CONNECT_MESSAGE_FAILURE);
			}
			mUiHandler.sendEmptyMessage(CONNECT_MESSAGE_END);
		}

		private boolean startConnect() {
			if (mNetId == -1 || !NetworkUtils.openWifi(mContext)) {
				return false;
			}

			while (getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			boolean success = mWifiManager.enableNetwork(mNetId, mDisableOthers);
			if (success) {
				long currentTime = System.currentTimeMillis();
				while (System.currentTimeMillis() - currentTime < DEFAULT_TIMEOUT_MILLIS) {
					if (NetworkUtils.isWifiConnected(mContext)) {
						WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
						if (mNetId == wifiInfo.getNetworkId()) {
							return true;
						}
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			return false;
		}
	}
}
