package com.kaisen.common.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

/**
 * 图片工具类
 */
public class ImageUtil {

	static public String getExtName(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot + 1);
			}
		}
		return filename;
	}

	static public String getFileName(String image) {
		if ((image != null) && (image.length() > 0)) {
			int dot = image.lastIndexOf('/');
			if ((dot > -1) && (dot < (image.length() - 1))) {
				return image.substring(dot + 1);
			}
		}
		return image;
	}

	static public String getFilePath(String image) {
		if ((image != null) && (image.length() > 0)) {
			int dot = image.lastIndexOf('/');
			if ((dot > -1) && (dot < (image.length() - 1))) {
				return image.substring(0, dot);
			}
		}
		return image;
	}

	/**
	 * @see #resize(File, File, int, int)
	 */
	public static BufferedImage resize(String srcImage, int w, int h,
			boolean zoomOutOnly) throws IOException {
		FileInputStream in = null;
		BufferedImage buffer = null;
		try {
			in = new FileInputStream(srcImage);
			buffer = _resize(in, w, h, zoomOutOnly);
		} finally {
			if (in != null) {
				in.close();
			}
		}
		return buffer;
	}

	/**
	 * 强制压缩/放大图片到固定的大小
	 * 
	 * @param isImage
	 *            原始图片流
	 * @param w
	 *            新宽度
	 * @param h
	 *            新高度
	 * @throws IOException
	 */
	private static BufferedImage _resize(InputStream isImage, int w, int h,
			boolean zoomOutOnly) throws IOException {
		BufferedImage image = ImageIO.read(isImage);
		// 得到图片的原始大小， 以便按比例压缩。
		int imageWidth = image.getWidth(null);
		int imageHeight = image.getHeight(null);

		// 只是缩小模式 且 所给长宽比均比原始大时 保持原图片尺寸
		if (zoomOutOnly && imageWidth < w && imageHeight < h) {
			h = imageHeight;
			w = imageWidth;
		} else {
			// 得到合适的压缩大小，按比例。
			if ((1.0 * imageWidth / imageHeight) > (1.0 * w / h)) {
				h = (int) Math.round((imageHeight * w * 1.0 / imageWidth));
			} else {
				w = (int) Math.round((imageWidth * h * 1.0 / imageHeight));
			}
		}

		// 构建图片对象
		BufferedImage _image = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_RGB);

		// 绘制缩小后的图（平滑处理优先）
		_image.getGraphics().drawImage(
				image.getScaledInstance(w, h, Image.SCALE_SMOOTH), 0, 0, w, h,
				null);
		return _image;
	}
}
