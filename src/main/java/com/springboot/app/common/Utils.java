package com.springboot.app.common;

import java.util.Locale;
import java.util.ResourceBundle;

public class Utils {

	public static boolean isEmptyOrNull(String str) {
		return str == null || str.equals("");
	}

	public static <T extends Enum<T>> boolean isEnumEmptyOrNull(T enumType) {
		return enumType == null || enumType.toString().equals("");
	}

	public static String getMessageForLocale(String message) {
		return ResourceBundle.getBundle("messages", Locale.getDefault()).getString(message);
	}
}