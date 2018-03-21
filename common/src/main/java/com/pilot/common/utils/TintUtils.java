package com.pilot.common.utils;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Hans.Chen on 2016-03-23.
 */
public class TintUtils {

	public TintUtils() {
		throw new UnsupportedOperationException("cannot not be instantiated");
	}

	public static void setTintList(@NonNull ImageButton button, ColorStateList colors) {
		if (colors == null) {
			return;
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			button.setImageTintList(colors);
		} else {
			button.setImageDrawable(getTintedDrawable(button.getDrawable(), colors));
		}
	}

	public static void setTintList(@NonNull TextView textView, ColorStateList colors) {
		if (colors == null) {
			return;
		}

		textView.setTextColor(colors);
	}

	public static void setBackgroundTintList(@NonNull Button button, ColorStateList colors) {
		if (colors == null) {
			return;
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			button.setBackgroundTintList(colors);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			button.setBackground(getTintedDrawable(button.getBackground(), colors));
		} else {
			button.setBackgroundDrawable(getTintedDrawable(button.getBackground(), colors));
		}
	}

	public static void setForegroundTintList(@NonNull Button button, ColorStateList colors) {
		if (colors == null) {
			return;
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			button.setForegroundTintList(colors);
		}
	}

	public static Drawable getTintedDrawable(Drawable drawable, ColorStateList colors) {
		if (colors == null) {
			return drawable;
		}
		if (drawable == null) {
			return null;
		}

		final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
		DrawableCompat.setTintList(wrappedDrawable, colors);
		wrappedDrawable.invalidateSelf();
		return wrappedDrawable;
	}
}
