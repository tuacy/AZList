package com.pilot.common.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 跟网络相关的工具类
 */
public class NetworkUtils {

	private NetworkUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 判断网络是否连接
	 */
	public static boolean isConnected(Context context) {

		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (null != connectivity) {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null && info.isConnected()) {
				if (info.getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断是否Wi-Fi连接
	 */
	public static boolean isWifiConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo networkInfo = cm.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				return true;
			}
		}
		return false;
	}

	public static boolean isMobileConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo networkInfo = cm.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 打开网络设置界面
	 */
	public static void openSetting(Activity activity) {
		Intent intent = new Intent("/");
		ComponentName cm = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
		intent.setComponent(cm);
		intent.setAction("android.intent.action.VIEW");
		activity.startActivityForResult(intent, 0);
	}

	public static int getNetworkType(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager == null ? null : connectivityManager.getActiveNetworkInfo();
		return networkInfo == null ? -1 : networkInfo.getType();
	}

	private static boolean isFastMobileNetwork(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if (telephonyManager == null) {
			return false;
		}

		switch (telephonyManager.getNetworkType()) {
			case TelephonyManager.NETWORK_TYPE_1xRTT:
				return false;
			case TelephonyManager.NETWORK_TYPE_CDMA:
				return false;
			case TelephonyManager.NETWORK_TYPE_EDGE:
				return false;
			case TelephonyManager.NETWORK_TYPE_EVDO_0:
				return true;
			case TelephonyManager.NETWORK_TYPE_EVDO_A:
				return true;
			case TelephonyManager.NETWORK_TYPE_GPRS:
				return false;
			case TelephonyManager.NETWORK_TYPE_HSDPA:
				return true;
			case TelephonyManager.NETWORK_TYPE_HSPA:
				return true;
			case TelephonyManager.NETWORK_TYPE_HSUPA:
				return true;
			case TelephonyManager.NETWORK_TYPE_UMTS:
				return true;
			case TelephonyManager.NETWORK_TYPE_EHRPD:
				return true;
			case TelephonyManager.NETWORK_TYPE_EVDO_B:
				return true;
			case TelephonyManager.NETWORK_TYPE_HSPAP:
				return true;
			case TelephonyManager.NETWORK_TYPE_IDEN:
				return false;
			case TelephonyManager.NETWORK_TYPE_LTE:
				return true;
			case TelephonyManager.NETWORK_TYPE_UNKNOWN:
				return false;
			default:
				return false;
		}
	}

	public static boolean isWifiEnable(Context context) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		return wifiManager.isWifiEnabled();
	}

	public static boolean openWifi(Context context) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		return wifiManager.isWifiEnabled() || wifiManager.setWifiEnabled(true);
	}

	public static boolean closeWifi(Context context) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		return !wifiManager.isWifiEnabled() || wifiManager.setWifiEnabled(false);
	}

	public static List<ScanResult> getWifiScanResults(Context context) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		return wifiManager.getScanResults();
	}

	public static void sortByLevel(List<ScanResult> list) {
		Collections.sort(list, new Comparator<ScanResult>() {
			@Override
			public int compare(ScanResult lhs, ScanResult rhs) {
				int lhsLevel = WifiManager.calculateSignalLevel(lhs.level, 100);
				int rhsLevel = WifiManager.calculateSignalLevel(rhs.level, 100);
				return lhsLevel < rhsLevel ? -1 : (lhsLevel == rhsLevel ? 0 : 1);
			}
		});
	}
}
