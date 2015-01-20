package com.kaisen.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * 验证工具
 * 
 */
public class ValidateUtil {

	private static final String NUMBER_PATTERN = "^[0-9]+(.[0-9]{0,1})?$";// 判断小数点后一位的数字的正则表达式
	private static final String CNUMBER_PATTERN = "^[0-9]*$";// 判断数字的正则表达式
	private static final String REGEX_MOBILE = "^1[3458]{1}[0-9]{1}[0-9]{8}$";
	private static final String REGEX_SMS_CODE = "^[0-9]{6}$";
	private static final String REGEX_MAIL_FORMAT = "(\\w+([.!#$%&'*+-/=?^_`{|}~]\\w*)*)@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
	private static final String REGEX_PASSWORD = "^[A-Za-z_0-9!@#\\$%\\^\\*?~&amp;]*$";
	private static final String REGEX_CHECKCODE = "^[A-Za-z_0-9]{4}$";
	private static final int EMAIL_MAX_LENGTH = 128;
	private static final String REGEX_URL = "(http[s]?)://[a-zA-Z0-9.-]+.([a-zA-Z]{2,4})(:d+)?(/[a-zA-Z0-9.-~!@#$%^&*+?:_/=<>]*)?";
	private static Pattern emailPattern;
	static {
		emailPattern = Pattern.compile(REGEX_MAIL_FORMAT);
	}

	/**
	 * 验证是不是数字(验证到小数点后一位)
	 * 
	 * @param number
	 * @return
	 */
	public static boolean isDecimalNumber(String _str) {
		if (StringUtils.isEmpty(_str)) {
			return Boolean.FALSE;
		}
		return _str.matches(NUMBER_PATTERN);
	}

	/**
	 * 验证是不是数字(没有小数点)
	 * 
	 * @param number
	 * @return
	 */
	public static boolean isInteger(String _str) {
		if (StringUtils.isEmpty(_str)) {
			return Boolean.FALSE;
		}
		return _str.matches(CNUMBER_PATTERN);
	}

	/**
	 * 验证Mobile
	 * 
	 * @param _str
	 * @return
	 */
	public static boolean isMobile(String _str) {
		if (StringUtils.isEmpty(_str)) {
			return Boolean.FALSE;
		}

		return _str.matches(REGEX_MOBILE);
	}

	/**
	 * 验证MobileCode
	 * 
	 * @param _str
	 * @return
	 */
	public static boolean isMobileCode(String _str) {
		if (StringUtils.isEmpty(_str)) {
			return Boolean.FALSE;
		}

		return _str.matches(REGEX_SMS_CODE);
	}

	/**
	 * 验证Email
	 * 
	 * @param _str
	 * @return
	 */
	public static boolean isEmail(String _str) {
		if (StringUtils.isEmpty(_str) || _str.length() > EMAIL_MAX_LENGTH)
			return false;

		Matcher m = emailPattern.matcher(_str);
		if (m.matches()) {
			return true;
		}

		return false;
	}

	/**
	 * 验证密码
	 * 
	 * @param _str
	 * @return
	 */
	public static boolean isPwd(String _str) {
		if (StringUtils.isEmpty(_str)) {
			return Boolean.FALSE;
		}

		return _str.matches(REGEX_PASSWORD);
	}

	/**
	 * 验证验证码
	 * 
	 * @param _str
	 * @return
	 */
	public static boolean isCheckCode(String _str) {
		if (StringUtils.isEmpty(_str)) {
			return Boolean.FALSE;
		}

		return _str.matches(REGEX_CHECKCODE);
	}

	/**
	 * 验证URL
	 * 
	 * @param _str
	 * @return
	 */
	public static boolean isUrl(String _str) {
		if (StringUtils.isEmpty(_str)) {
			return Boolean.FALSE;
		}

		return _str.matches(REGEX_URL);
	}

	public static void main(String[] args) {
		System.out.println(null + "1");
		System.out.println(isUrl("sadasd"));
	}
}
