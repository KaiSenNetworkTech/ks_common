package com.kaisen.common.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RandomUtil {

	private final static String LETTER_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	private final static String LETTER_LOWER = "abcdefghijklmnopqrstuvwxyz";

	private final static String LETTER = LETTER_UPPER + LETTER_LOWER;

	private final static String ASC_II = LETTER + "0123456789";

	private static Random Global_Random = new Random();

	private final static String ASC_CODE = "ABCDEFGHIJKLMNPQRSTUVWXYZ123456789";

	public static char nextChar() {
		return ASC_II.charAt(Global_Random.nextInt(ASC_II.length()));
	}

	/**
	 * 得到一个随机字符串，用于推荐码，需要在TV上输入
	 * 
	 * @param number
	 * @return
	 */
	public static String getCodeString(int number) {
		StringBuffer buf = new StringBuffer();
		for (int j = 0; j < number; j++) {
			buf.append(ASC_CODE.charAt(Global_Random.nextInt(ASC_CODE.length())));
		}
		return buf.toString();
	}

	/**
	 * 给定范围获得随机颜色
	 * 
	 * @param ac
	 *            range 0-255
	 * @param bc
	 *            range 0-255
	 * @see Color#Color(int, int, int)
	 * @return Color
	 */
	public static Color nextColor(int ac, int bc) {
		if (ac > bc) {
			int tmp = ac;
			ac = bc;
			bc = tmp;
		}

		if (bc > 256) {
			bc = 256;
		}
		if (ac < 0) {
			ac = 0;
		}

		return new Color(nextInt(ac, bc), nextInt(ac, bc), nextInt(ac, bc));
	}

	public static Integer[] nextCombine(int m, int n) {
		Set<Integer> buf = new HashSet<Integer>();
		do {
			if (buf.size() > n) {
				break;
			} else {
				buf.add(new Random().nextInt(m));
			}
		} while (true);

		return buf.toArray(new Integer[buf.size()]);
	}

	public static BufferedImage nextImage(String text) {
		int width = 60, height = 20;
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		// 获取图形上下文
		Graphics g = image.getGraphics();
		// 设定背景色
		g.setColor(nextColor(200, 250));
		g.fillRect(0, 0, width, height);
		// 设定字体
		g.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		// 画边框
		// g.setColor(new Color());
		// g.drawRect(0,0,width-1,height-1);

		// 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到
		g.setColor(nextColor(160, 200));
		for (int i = 0; i < 155; i++) {
			int x = Global_Random.nextInt(width);
			int y = Global_Random.nextInt(height);
			int xl = Global_Random.nextInt(12);
			int yl = Global_Random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}
		g.setColor(new Color(20 + Global_Random.nextInt(110),
				20 + Global_Random.nextInt(110), 20 + Global_Random
						.nextInt(110)));
		for (int i = 0; i < text.length(); i++) {

			g.drawString(text.substring(i, i + 1), 13 * i + 6, 16);
		}
		g.dispose();
		return image;
	}

	public static int nextInt() {
		return Global_Random.nextInt();
	}

	public static int nextInt(int n) {
		return Global_Random.nextInt(n);
	}

	public static int nextInt(int min, int max) {
		int data;
		if (max < min) {
			data = min;
			min = max;
			max = data;
		}
		do {
			data = (Global_Random.nextInt()) % max;
		} while (data < min || data > max);
		return data;
	}

	public static char nextLetter() {
		return LETTER.charAt(Global_Random.nextInt(LETTER.length()));
	}

	public static String nextLetter(int number) {
		StringBuffer buf = new StringBuffer();
		for (int j = 0; j < number; j++) {
			buf.append(nextLetter());
		}
		return buf.toString();
	}

	public static String nextString(int number) {
		StringBuffer buf = new StringBuffer();
		for (int j = 0; j < number; j++) {
			buf.append(nextChar());
		}
		return buf.toString();
	}
}
