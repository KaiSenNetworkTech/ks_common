package com.kaisen.common.util;

import java.util.Random;

public class KSStringUtil {

	/**
	 * unicode 0x0020
	 */
	public static final char SpaceCharA = ' ';

	/**
	 * unicode 0x3000
	 */
	// public static final char SpaceCharB = ' ';
	// public static final char SpaceCharB = (char)12288;
	public static final char SpaceCharB = (char) Integer.valueOf("3000", 16)
			.intValue();

	/**
	 * unicode 0xe525
	 */
	// public static final char SpaceCharC = '';
	// public static final char SpaceCharC = (char)58661;
	public static final char SpaceCharC = (char) Integer.valueOf("e525", 16)
			.intValue();

	/**
	 * unicode 0xe5f1
	 */
	// public static final char SpaceCharD = '';
	// public static final char SpaceCharD = (char)58865;
	public static final char SpaceCharD = (char) Integer.valueOf("e5f1", 16)
			.intValue();

	/**
	 * String of unicode char 0x0020
	 */
	public static final String SpaceStringA = String.valueOf(SpaceCharA);

	/**
	 * String of unicode char 0x3000
	 */
	public static final String SpaceStringB = String.valueOf(SpaceCharB);

	/**
	 * String of unicode char 0xe525
	 */
	public static final String SpaceStringC = String.valueOf(SpaceCharC);

	/**
	 * String of unicode char 0xe5f1
	 */
	public static final String SpaceStringD = String.valueOf(SpaceCharD);

	/**
	 * @see TestFinalClass.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
	 */
	private final static Character.UnicodeBlock chinese = Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS;

	/**
	 * 判断一个字符串是否包含中文
	 * 
	 * @param str
	 * @return
	 */
	public static boolean containChinese(String str) {
		if (null == str || str.isEmpty()) {
			return false;
		}
		char[] chars = str.toCharArray();
		for (char c : chars) {
			boolean isChinese = isChinese(c);
			if (isChinese) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 是否包含字母
	 * 
	 * @param str
	 * @return
	 */
	public static boolean containLetter(String str) {
		if (null == str || str.isEmpty()) {
			return false;
		}
		char[] chars = str.toCharArray();
		for (char c : chars) {
			boolean isLetter = isLetter(c);
			if (isLetter) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断一个字符串是否包含字母（大写或小写）
	 * 
	 * @param str
	 * @return
	 */
	public static boolean containLowerOrUpperCase(String str) {
		if (null == str || str.isEmpty()) {
			return false;
		}
		char[] chars = str.toCharArray();
		for (char c : chars) {
			boolean isLowOrUpperCase = isLowerOrUpperCase(c);
			if (isLowOrUpperCase) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 按pattern模板替换paras参数
	 * 
	 * @param pattern
	 * @param paras
	 * @return
	 */
	public static String format(String pattern, Object... paras) {
		if (null == pattern && null == paras) {
			return pattern;
		}
		if (null == pattern) {
			StringBuffer buf = new StringBuffer();
			for (Object obj : paras) {
				buf.append(obj);
			}
			return buf.toString();
		}
		int count = 0;
		while (pattern.contains("{}")) {
			if (paras.length <= count) {
				break;
			}
			pattern = pattern.replaceFirst("\\{\\}", paras[count++].toString());
		}
		return pattern;
	}

	/**
	 * 得到正常的可读支付，a-z,A-Z,0-9,汉字
	 * 
	 * @param str
	 * @return
	 */
	public static String getNomalCharacter(String str) {
		if (null == str || str.length() <= 0) {
			return str;
		}

		char[] chars = str.toCharArray();
		StringBuffer buf = new StringBuffer();
		for (char c : chars) {
			if (' ' == c || '-' == c || (c >= 'A' && c <= 'Z')
					|| (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9')
					|| String.valueOf(c).matches("[\\u4E00-\\u9FA5]+")) {
				buf.append(c);
			}
		}

		return buf.toString();
	}

	/**
	 * 随机生成固定长度的字符串
	 * 
	 * @param length
	 * @return String
	 */
	public static String getRandomString(int length) { // length表示生成字符串的长度
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * 判断一个char是否为中文。
	 * 
	 * @param c
	 *            字符
	 * @return 是否为中文
	 */
	public static boolean isChinese(char c) {
		return (chinese == Character.UnicodeBlock.of(c));
	}

	/**
	 * 检查字符串是否为<code>null</code>或空字符串<code>""</code>。
	 * 
	 * <pre>
	 * StringUtil.isEmpty(null) = true
	 * StringUtil.isEmpty("") = true
	 * StringUtil.isEmpty(" ") = false
	 * StringUtil.isEmpty("bob") = false
	 * StringUtil.isEmpty("  bob  ") = false
	 * </pre>
	 * 
	 * @param str
	 *            要检查的字符串
	 * @return 如果为空, 则返回<code>true</code>
	 */
	public static boolean isEmpty(String str) {
		return ((str == null) || (str.length() == 0));
	}

	/**
	 * 判断字符串数组中是否包含某字符串
	 * 
	 * @param source
	 * @param needle
	 * @return
	 */
	public static Boolean isHave(String right, String needle) {
		if (right == null || needle == null)
			return false;
		String[] source = right.split(";");
		for (String s : source) {
			if (s.equals(needle))
				return true;
		}
		return false;
	}

	/**
	 * 是否字母
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isLetter(char c) {
		return Character.isLetter(c);
	}

	/**
	 * 是否小写字母
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isLowerCase(char c) {
		return Character.isLowerCase(c);
	}

	/**
	 * 是否英文字母（大写或小写）
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isLowerOrUpperCase(char c) {
		return Character.isLowerCase(c) || Character.isUpperCase(c);
	}

	/**
	 * 是否大写字母
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isUpperCase(char c) {
		return Character.isUpperCase(c);
	}

	/**
	 * 相同字符的个数
	 * 
	 * @param src
	 * @param dst
	 * @return
	 */
	public static int repeatCharNum(String src, String dst) {
		int num = 0;
		if (KSStringUtil.isEmpty(src) || KSStringUtil.isEmpty(dst))
			return num;
		src = src.replace(" ", "");
		for (int i = 0; i < src.length(); i++) {
			if (dst.indexOf(src.charAt(i)) != -1)
				num++;
		}
		return num;
	}

	/**
	 * 仅仅大写第一个字母
	 * 
	 * @param str
	 * @return
	 */
	public static String upperCaseFirstLetterOnly(String str) {
		if (null == str || str.length() <= 0) {
			return str;
		}

		StringBuilder buf = new StringBuilder(str.length());

		char[] array = str.toCharArray();

		buf.append(String.valueOf(array[0]).toUpperCase());

		for (int i = 1; i < array.length; i++) {
			buf.append(array[i]);
		}

		return buf.toString();
	}

	/**
	 * 大写第一个字母小写其他字母
	 * 
	 * @param str
	 * @return
	 */
	public static String upperCaseFirstLetterOtherLowerCase(String str) {
		if (null == str || str.length() <= 0) {
			return str;
		}

		StringBuilder buf = new StringBuilder(str.length());

		char[] array = str.toCharArray();

		buf.append(String.valueOf(array[0]).toUpperCase());

		for (int i = 1; i < array.length; i++) {
			buf.append(String.valueOf(array[i]).toLowerCase());
		}

		return buf.toString();
	}

}
