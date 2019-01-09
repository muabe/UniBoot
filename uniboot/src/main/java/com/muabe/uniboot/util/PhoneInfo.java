package com.muabe.uniboot.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;

public class PhoneInfo {

	/**
	 * 핸드폰번호
	 * @return
	 */
	public static String getPhoneNumber(Context context){
		TelephonyManager telM = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		@SuppressLint("MissingPermission") String phoneNum = telM.getLine1Number();
		return phoneNum;
	}

	/**
	 * <uses-permission android:name="android.permission.READ_PHONE_STATE" />
	 * @param context
	 * @return
	 */
	@SuppressLint("MissingPermission")
	public static String getDeviceId(Context context){
		TelephonyManager mgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return mgr.getDeviceId();
	}

}
