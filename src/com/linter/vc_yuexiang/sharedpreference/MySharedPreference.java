package com.linter.vc_yuexiang.sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreference {
	/**
	 * 登录信息：保存
	 * 
	 * @param context
	 * @param username
	 */
	public static void saveLoginInfo(Context context, String username) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"loginInfo", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("username", username);
		editor.commit();
	}

	/**
	 * 登录信息：获取
	 * 
	 * @param context
	 * @return username
	 */
	public static String getLoginInfo(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"loginInfo", Context.MODE_PRIVATE);
		String username = sharedPreferences.getString("username", "");
		return username;
	}

	/**
	 * 登录标记：保存
	 * 
	 * @param context
	 * @param flag
	 */
	public static void saveLoginFlag(Context context, boolean flag) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"loginFlag", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean("flag", flag);
		editor.commit();
	}

	/**
	 * 登录标记：获取
	 * 
	 * @param context
	 * @return flag
	 */
	public static boolean getLoginFlag(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"loginFlag", Context.MODE_PRIVATE);
		boolean flag = sharedPreferences.getBoolean("flag", false);
		return flag;
	}
}
