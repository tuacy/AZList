package com.pilot.common.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SDCardHelperUtils {

	public static boolean isSDCardMounted() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	public static String getSDCardBaseDir() {
		if (isSDCardMounted()) {
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		}
		return null;
	}

	private static long getAvailableBytes(StatFs stat) {
		long bytesAvailable;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
			bytesAvailable = stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
		} else {
			bytesAvailable = stat.getBlockSize() * stat.getAvailableBlocks();
		}
		return bytesAvailable;
	}

	private static long getFreeBytes(StatFs stat) {
		long freeBytes;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
			freeBytes = stat.getBlockSizeLong() * stat.getFreeBlocksLong();
		} else {
			freeBytes = stat.getBlockSize() * stat.getFreeBlocks();
		}
		return freeBytes;
	}

	private static long getCountBytes(StatFs stat) {
		long countBytes;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
			countBytes = stat.getBlockSizeLong() * stat.getBlockCountLong();
		} else {
			countBytes = stat.getBlockSize() * stat.getBlockCount();
		}
		return countBytes;
	}


	public static long getSDCardCountBytes() {
		if (isSDCardMounted()) {
			StatFs fs = new StatFs(getSDCardBaseDir());
			return getCountBytes(fs);
		}
		return 0;
	}

	public static long getSDCardFreeBytes() {
		if (isSDCardMounted()) {
			StatFs fs = new StatFs(getSDCardBaseDir());
			return getFreeBytes(fs);
		}
		return 0;
	}


	public static long getSDCardAvailableSize() {
		if (isSDCardMounted()) {
			StatFs stat = new StatFs(getSDCardBaseDir());
			return getAvailableBytes(stat);
		}
		return 0;
	}


	public static boolean saveFileToSDCardPublicDir(byte[] data, String type, String fileName) {
		BufferedOutputStream bos = null;
		if (isSDCardMounted()) {
			File file = Environment.getExternalStoragePublicDirectory(type);
			try {
				bos = new BufferedOutputStream(new FileOutputStream(new File(file, fileName)));
				bos.write(data);
				bos.flush();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (null != bos) {
						bos.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	public static boolean saveFileToSDCardCustomDir(byte[] data, String dir, String fileName) {
		BufferedOutputStream bos = null;
		if (isSDCardMounted()) {
			File file = new File(getSDCardBaseDir() + File.separator + dir);
			if (!file.exists()) {
				file.mkdirs();
			}
			try {
				bos = new BufferedOutputStream(new FileOutputStream(new File(file, fileName)));
				bos.write(data);
				bos.flush();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (null != bos) {
						bos.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	public static boolean saveFileToSDCardPrivateFilesDir(byte[] data, String type, String fileName, Context context) {
		BufferedOutputStream bos = null;
		if (isSDCardMounted()) {
			File file = context.getExternalFilesDir(type);
			try {
				bos = new BufferedOutputStream(new FileOutputStream(new File(file, fileName)));
				bos.write(data);
				bos.flush();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (null != bos) {
						bos.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	public static boolean saveFileToSDCardPrivateCacheDir(byte[] data, String fileName, Context context) {
		BufferedOutputStream bos = null;
		if (isSDCardMounted()) {
			File file = context.getExternalCacheDir();
			try {
				bos = new BufferedOutputStream(new FileOutputStream(new File(file, fileName)));
				bos.write(data);
				bos.flush();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (null != bos) {
						bos.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	public static boolean saveBitmapToSDCardPrivateCacheDir(Bitmap bitmap, String fileName, Context context) {
		if (isSDCardMounted()) {
			BufferedOutputStream bos = null;
			File file = context.getExternalCacheDir();

			try {
				bos = new BufferedOutputStream(new FileOutputStream(new File(file, fileName)));
				if (fileName.contains(".png") || fileName.contains(".PNG")) {
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
				} else {
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
				}
				bos.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (bos != null) {
					try {
						bos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}


	public static byte[] loadFileFromSDCard(String fileDir) {
		BufferedInputStream bis = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			bis = new BufferedInputStream(new FileInputStream(new File(fileDir)));
			byte[] buffer = new byte[8 * 1024];
			int c = 0;
			while ((c = bis.read(buffer)) != -1) {
				baos.write(buffer, 0, c);
				baos.flush();
			}
			return baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				baos.close();
				if (null != bis) {
					bis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public Bitmap loadBitmapFromSDCard(String filePath) {
		byte[] data = loadFileFromSDCard(filePath);
		if (data != null) {
			Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
			if (bm != null) {
				return bm;
			}
		}
		return null;
	}

	public static String getSDCardPublicDir(String type) {
		return Environment.getExternalStoragePublicDirectory(type).getAbsolutePath();
	}

	public static String getSDCardPrivateCacheDir(@NonNull Context context) {
		if (null != context.getExternalCacheDir()) {
			return context.getExternalCacheDir().getAbsolutePath();
		}
		return null;
	}

	public static String getSDCardPrivateFilesDir(@NonNull Context context, String type) {
		File sdCardPrivateFilesDir = context.getExternalFilesDir(type);
		if (null != sdCardPrivateFilesDir) {
			return sdCardPrivateFilesDir.getAbsolutePath();
		}
		return null;
	}

	public static boolean isFileExist(String filePath) {
		File file = new File(filePath);
		return file.isFile();
	}

	public static boolean removeFileFromSDCard(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			try {
				file.delete();
				return true;
			} catch (Exception e) {
				return false;
			}
		} else {
			return false;
		}
	}
}
