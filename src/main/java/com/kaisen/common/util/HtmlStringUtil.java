package com.kaisen.common.util;

import java.text.DecimalFormat;

import org.apache.commons.lang3.StringUtils;

/**
 * 网页字符串显示处理工具包类
 */
public class HtmlStringUtil {

	public static String shortly(String str, int limitlength) {
		if (null == str || limitlength <= 0 || str.length() < limitlength) {
			return str;
		}
		return StringUtils.substring(str, 0, limitlength) + "...";
	}

	public static String shortly(String str, int limitlength, String endstr) {
		if (null == str || limitlength <= 0 || str.length() < limitlength) {
			return str;
		}
		return StringUtils.substring(str, 0, limitlength) + endstr;
	}

	/**
	 * 将String[]数组变成用符号分割的字符串
	 * 
	 * @param gule
	 * @param strs
	 * @return
	 */
	public static String join(String gule, String[] strs) {
		if (null == strs)
			return "";
		String str = "";
		for (int i = 0; i < strs.length; i++) {
			str += (str.length() > 0 ? gule : "") + strs[i];
		}
		return str;
	}

	public static String formatFileSize(long fileSize) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileSize < 1024) {
			fileSizeString = df.format((double) fileSize) + " B";
		} else if (fileSize < 1048576) {
			fileSizeString = df.format((double) fileSize / 1024) + " KB";
		} else if (fileSize < 1073741824) {
			fileSizeString = df.format((double) fileSize / 1048576) + " MB";
		} else {
			fileSizeString = df.format((double) fileSize / 1073741824) + " GB";
		}
		return fileSizeString;
	}
}
