package com.pilot.common.utils;

public class ByteUtils {


	/**
	 * 把short转化为byte数组，byte[0]是高位
	 *
	 * @param s 需要转换的数值
	 * @return byte数组
	 */
	public static byte[] shortToByteArray(short s) {
		int byteNum = Short.SIZE / 8;
		byte[] targets = new byte[byteNum];
		for (int i = 0; i < byteNum; i++) {
			int offset = (byteNum - 1 - i) * 8;
			targets[i] = (byte) ((s >>> offset) & 0xff);
		}
		return targets;
	}

	/**
	 * 把int转化为byte数组，byte[0]是高位
	 *
	 * @param src 需要转换的数值
	 * @return byte数组
	 */
	public static byte[] intToByteArray(int src) {
		int byteNum = Integer.SIZE / 8;
		byte[] targets = new byte[byteNum];
		for (int i = 0; i < byteNum; i++) {
			int offset = (byteNum - 1 - i) * 8;
			targets[i] = (byte) ((src >>> offset) & 0xff);
		}
		return targets;
	}

	/**
	 * 把long转化为byte数组，byte[0]是高位
	 *
	 * @param src 需要转换的数值
	 * @return byte数组
	 */
	public static byte[] longToByteArray(long src) {
		int byteNum = Long.SIZE / 8;
		byte[] targets = new byte[byteNum];
		for (int i = 0; i < byteNum; i++) {
			int offset = (byteNum - 1 - i) * 8;
			targets[i] = (byte) ((src >>> offset) & 0xff);
		}
		return targets;
	}

	public static byte[] byteMerger(byte[]... arrays) {

		int totalLength = 0;
		for (byte[] array : arrays) {
			if (array != null) {
				totalLength += array.length;
			}
		}

		byte[] targets = new byte[totalLength];
		int dstPos = 0;
		for (byte[] array : arrays) {
			if (array != null) {
				System.arraycopy(array, 0, targets, dstPos, array.length);
				dstPos += array.length;
			}
		}

		return targets;
	}

	public static String dumpByte(byte[] data, int length) {
		if (null == data) {
			return null;
		}
		String print = "";
		for (int i = 0; i < data.length && i < length; i++) {
			byte temp = data[i];
			String hex = Integer.toHexString(temp & 0xFF);
			if (hex.length() == 1) {
				hex = "0" + hex;
			}
			print = print + "0x" + hex.toUpperCase() + ", ";
		}
		return print;
	}

	/**
	 * Unicode转中文
	 * @param ori
	 * @return
	 */
	public static String convertUnicode(String ori){
		char aChar;
		int len = ori.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = ori.charAt(x++);
			if (aChar == '\\') {
				aChar = ori.charAt(x++);
				if (aChar == 'u') {
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = ori.charAt(x++);
						switch (aChar) {
							case '0':
							case '1':
							case '2':
							case '3':
							case '4':
							case '5':
							case '6':
							case '7':
							case '8':
							case '9':
								value = (value << 4) + aChar - '0';
								break;
							case 'a':
							case 'b':
							case 'c':
							case 'd':
							case 'e':
							case 'f':
								value = (value << 4) + 10 + aChar - 'a';
								break;
							case 'A':
							case 'B':
							case 'C':
							case 'D':
							case 'E':
							case 'F':
								value = (value << 4) + 10 + aChar - 'A';
								break;
							default:
								throw new IllegalArgumentException("Malformed   \\uxxxx   encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';
					outBuffer.append(aChar);
				}
			} else
				outBuffer.append(aChar);
		}

		return outBuffer.toString();
	}
}
