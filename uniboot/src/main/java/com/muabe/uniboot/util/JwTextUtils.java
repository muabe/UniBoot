package com.muabe.uniboot.util;

import java.util.Locale;

public class JwTextUtils {

	public static String optEmpty(String str, String emptyString) {
		if (str == null || str.length() == 0) {
			return emptyString;
		}
		return str;
	}

	public static String optNull(String str, String nullString) {
		if (str == null) {
			return nullString;
		}
		return str;
	}

	public static boolean isContains(String str, String[] array) {
		if (str != null) {
			for (int i = 0; i < array.length; i++) {
				if (str.equals(array[i])) {
					return true;
				}
			}
		}
		return false;
	}


	public static String getContains(String str, String[] array) {
		if (str != null) {
			for (int i = 0; i < array.length; i++) {
				if (str.equals(array[i])) {
					return array[i];
				}
			}
		}
		return null;
	}

	public static String getContains(String str, String[] array, String defaultString) {
		if (str != null) {
			for (int i = 0; i < array.length; i++) {
				if (str.equals(array[i])) {
					return array[i];
				}
			}
		}
		return defaultString;
	}

	public static int getContainsIndex(String str, String[] array, int defaultIndex) {
		if (str != null) {
			for (int i = 0; i < array.length; i++) {
				if (str.equals(array[i])) {
					return i;
				}
			}
		}
		return defaultIndex;
	}


	public static String comma(Object number){
		if(number==null){
			return "";
		}
		return String.format(Locale.getDefault(), "%,d",number);
	}

	public static String comma(int number){
		return String.format(Locale.getDefault(), "%,d",number);
	}

	public static String comma(long number){
		return String.format(Locale.getDefault(), "%,d",number);
	}
}

