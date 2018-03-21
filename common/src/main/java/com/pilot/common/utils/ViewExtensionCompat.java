package com.pilot.common.utils;

import android.os.Build;
import android.widget.PopupWindow;

public class ViewExtensionCompat {

	/**
	 * 现在只是支持LOLLIPOP以上的版本(包括LOLLIPOP)，如果调用这个函数没效果，估计是PopupWindow没有设置setBackgroundDrawable
	 */
	public static void setPopupElevation(PopupWindow popup, int elevation) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			popup.setElevation(elevation);
		}
	}

}
