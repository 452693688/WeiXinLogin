package com.tywl.myt.utile;

import android.view.Gravity;
import android.widget.Toast;

import com.tywl.myt.activity.BaseApplication;


public class ToastUtile {

	/**
	 * Ui线程/非UI线程中显示 Toast
	 */
	public static void showToast(final int strID) {
		showToast(strID, Gravity.BOTTOM);
	}

	/**
	 * UI线程/非UI线程均可调用 显示 Toast
	 */
	public static void showToast(final String str) {
		showToast(str, Gravity.BOTTOM);
	}

	/**
	 * UI线程/非UI线程均可调用 显示 Toast
	 */
	public static void showToast(final int strID, final int gravity) {
		showToast(BaseApplication.baseApplication.getString(strID), gravity);
	}

	private static Toast toast = null;

	/**
	 * UI线程/非UI线程均可调用 显示 Toast
	 */
	public static void showToast(final String str, final int gravity) {
		if (toast == null) {
			try {
				toast = Toast.makeText(BaseApplication.baseApplication, str,
						Toast.LENGTH_SHORT);
				if (gravity == Gravity.BOTTOM) {
					toast.setGravity(gravity, 0, 100);
				} else {
					toast.setGravity(gravity, 0, 0);
				}
				toast.show();
			} catch (Exception e) {
			}
		} else {
			toast.cancel();
			toast = null;
			showToast(str, gravity);
		}
	}

}
