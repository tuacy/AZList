package com.pilot.common.utils;

import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;

public class ResourceUtils {

	public static int getColor(Context context, @ColorRes int colorId) {
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
			//noinspection deprecation
			return context.getResources().getColor(colorId);
		} else {
			return context.getColor(colorId);
		}
	}

	public static float getDimension(Context context, @DimenRes int dimenId) {
		return context.getResources().getDimension(dimenId);
	}

}
