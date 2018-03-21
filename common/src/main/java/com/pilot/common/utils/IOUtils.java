package com.pilot.common.utils;

import java.io.Closeable;
import java.io.IOException;

public class IOUtils {

	private IOUtils() {
		throw new UnsupportedOperationException();
	}

	public static void close(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				throw new RuntimeException("IOException occurred. ", e);
			}
		}
	}

	/**
	 * Close closable and hide possible {@link IOException}
	 *
	 * @param closeable closeable object
	 */
	public static void closeQuietly(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException ignored) {
				// Ignored
			}
		}
	}
}
