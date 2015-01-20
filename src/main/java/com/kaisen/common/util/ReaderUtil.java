package com.kaisen.common.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 读取文件内容的类
 */
public class ReaderUtil {

	/**
	 * 写日志.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(ReaderUtil.class);

	/**
	 * 读取一个目录下文件名称
	 *
	 * @param list
	 *            存储文件名的池子
	 * @param file
	 *            File[]数组
	 * @param recursive
	 *            是否递归遍历其子目录，true是，false否。
	 */
	private static void _fileNames(List<String> list, File[] file,
			boolean recursive) {
		for (int i = 0; i < file.length; i++) {
			// 如果是文件
			if (file[i].isFile()) {
				list.add(file[i].toString());
			}

			// 如果是目录 && 如果需要递归
			if (file[i].isDirectory() && recursive) {
				_fileNames(list, file[i].listFiles(), recursive);
			}
		}
	}

	/**
	 * 递归遍历文件 或 一个目录下所有文件后读取到字符串库
	 *
	 * @param list
	 *            用于存储字符窜数组的库
	 * @param file
	 *            一个文件或者一个目录
	 * @return 递归遍历每个文件后读取到字符串数组库
	 */
	private static List<String> _read(List<String> list, File file) {
		// 如果是文件
		if (file.isFile()) {
			// 读取文件然后 存入库里面
			list.add(read(file.toString(), null));
			return list;
		}

		// 如果是目录
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				// 递归调用自己反复读取文件内容
				_read(list, files[i]);
			}
		}

		return list;
	}

	/**
	 * 读取一个目录下文件名称。默认不递归子目录
	 *
	 * @param file
	 *            文件路径
	 * @return 路径下文件名数组
	 */
	public static String[] getFileNames(File file) {
		return getFileNames(file, false);
	}

	/**
	 * 读取一个目录下文件名称
	 *
	 * @param file
	 *            文件路径
	 * @param recursive
	 *            是否递归遍历其子目录，true是，false否。
	 * @return 路径下文件名数组
	 */
	public static String[] getFileNames(File file, boolean recursive) {
		if (file.isFile()) {
			return new String[] { file.toString() };
		} else {
			List<String> list = new ArrayList<String>();
			_fileNames(list, file.listFiles(), recursive);
			return list.toArray(new String[list.size()]);
		}
	}

	/**
	 * 返回 一个目录或一个文件 下面所有文件取得的字符串数组。 例如一个目录下3个文件则返回长度为3的数组 直接传入一个文件则返回1个数组
	 *
	 * @param file
	 *            一个目录 或 一个文件
	 * @return 每个文件的文本字符串数组
	 */
	public static String[] read(File file) {
		List<String> list = new ArrayList<String>();

		list = _read(list, file);

		return (String[]) list.toArray(new String[list.size()]);
	}

	public static String read(InputStream in) throws IOException {
		return read(in, "utf-8");
	}

	public static String read(InputStream in, String charsetName)
			throws IOException {
		return new String(readByte(in), charsetName);
	}

	/**
	 * 从指定的文件中读取 文本内容
	 *
	 * @param file
	 *            指定的文件
	 * @return 读取到文本内容
	 */
	public static String read(String file) {
		return read(file, (String) null);
	}

	/**
	 * 从指定的文件中读取 文本内容
	 *
	 * @param file
	 *            指定的文件
	 * @return 读取到文本内容
	 */
	public static String read(String file, String charsetName) {
		logger.info("ReaderUtil reader(" + file + ")");

		FileInputStream fis = null;
		BufferedInputStream bis = null;
		InputStreamReader isr = null;
		try {
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			if (charsetName == null) {
				isr = new InputStreamReader(bis);
			} else {
				isr = new InputStreamReader(bis, charsetName);
			}

			StringBuffer tmp = new StringBuffer(10240);
			char data[] = new char[10240];
			int size = 0;
			while ((size = isr.read(data)) != -1) {
				tmp.append(new String(data, 0, size));
			}

			return tmp.toString();
		} catch (Exception e) {
			logger.error("ReaderFile reader(" + file + ") error!", e);
			return null;
		} finally {
			try {
				if (isr != null) {
					isr.close();
				}

				if (bis != null) {
					bis.close();
				}

				if (fis != null) {
					fis.close();
				}
			} catch (Exception e) {
				logger.error("ReaderFile reader(" + file + ") close error!", e);
			}
		}
	}

	/**
	 * 从指定的文件中读取二进制流
	 *
	 * @param file
	 *            指定的文件
	 * @return byte[] 二进制流
	 */
	public static byte[] readByte(File file) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);

			List<byte[]> list = new ArrayList<byte[]>();
			int bufSize = 10240;
			byte[] buf;

			int count = 0;
			int size = 0;
			while (-1 != (size = fis.read((buf = new byte[bufSize])))) {
				list.add(buf);
				count += size;
			}

			byte[] data = new byte[count];
			int s = 0;
			for (byte[] tmp : list) {
				int c = (count - s);
				System.arraycopy(tmp, 0, data, s, ((c < bufSize) ? c : bufSize));
				s += bufSize;
			}

			return data;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		} finally {
			if (null != fis) {
				try {
					fis.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
					return null;
				}
			}
		}
	}

	public static byte[] readByte(InputStream in) throws IOException {
		if (null == in) {
			throw new NullPointerException(
					"Parameter (InputStream in) is null!");
		}

		ByteArrayOutputStream data = new ByteArrayOutputStream();
		int num = 0;
		byte[] buffer = new byte[1024];
		while ((num = in.read(buffer)) > 0) {
			data.write(buffer, 0, num);
		}
		in.close();
		return data.toByteArray();
	}

	/**
	 * 从指定的文件中读取二进制流
	 *
	 * @param file
	 *            指定的文件
	 * @return byte[] 二进制流
	 */
	public static byte[] readByte(String file) {
		return readByte(new File(file));
	}
}
