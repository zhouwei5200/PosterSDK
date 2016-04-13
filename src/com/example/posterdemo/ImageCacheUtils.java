package com.example.posterdemo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.GetChars;
import android.widget.Toast;

public class ImageCacheUtils {

	private static String saveBitmapPath;
	private static FileOutputStream fos;
	private static String imageSavePath;

	private static String hashKeyForDisk(String key) {
		String cacheKey;
		try {
			final MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(key.getBytes());
			cacheKey = bytesToHexString(mDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			cacheKey = String.valueOf(key.hashCode());
		}
		return cacheKey;
	}

	private static String bytesToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}

	/**
	 * 判断SD卡是否挂载
	 *
	 * @return true--已被挂载 false--未被挂载
	 */
	public static boolean isSDCardAvailable() {
		return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
	}

	/**
	 * 获取SD卡当前的可用存储空间大小，返回的值得单位是byte
	 *
	 * @return
	 */
	public static long getUsefulSizeOfSD() {

		return Environment.getExternalStorageDirectory().getUsableSpace();
		// return Environment.getExternalStorageDirectory().getFreeSpace();

	}

	/**
	 * 当前方法完成的操作是根据传递进来的bitmap对象获取该对象对应的图片的大小 返回的大小单位是byte
	 *
	 * @param bitmap
	 *            想要获取大小的图片的Bitmap对象
	 * @return
	 */
	public static long getBitmapTotalByte(Bitmap bitmap) {

		if (null != bitmap) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			int options = 100;
			return baos.toByteArray().length;
		} else {
			return 0;
		}

	}

	/**
	 * 从sd卡读取图片
	 *
	 * @param path
	 * @return Bitmap
	 */
	public static Bitmap getBitmap(String path) {
		// String SDPATH = Environment.getExternalStorageDirectory() + "/" +
		// "poster";
		// String bitmapPath = SDPATH + path;
		if (isSDCardAvailable()) {
			String SDPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
			File fileSD = new File(SDPATH);
			File fileBySDSon = new File(fileSD, "poster");
			if (fileBySDSon.exists()) {
				File imageFile = new File(fileBySDSon, URLEncoder.encode(path));
				if (imageFile.exists()) {
					// 图片文件对象存在的时候获取当前的图片对象对应的路径
					imageSavePath = imageFile.getAbsolutePath();
				} else {
					return null;
				}
			} else {
				return null;
			}

		} else {
			// 当sd卡不可用时
		}
		File imageFile = new File(imageSavePath);
		if (imageFile.exists()) {
			Bitmap bitmap = BitmapFactory.decodeFile(imageSavePath);
			return bitmap;
		} else {
			return null;
		}

	}

	/**
	 * 保存Bitmap到本地
	 *
	 * @param bmp
	 * @param name
	 * @return String
	 */
	public static boolean SaveBitmap(Bitmap bmp, String name) {
		if (null != name && null != bmp) {

			if (isSDCardAvailable() && getUsefulSizeOfSD() > getBitmapTotalByte(bmp) * 1.5) { // 判断sd卡是否挂载
				// String SDPATH = Environment.getExternalStorageDirectory() +
				// "/" + "poster";
				String SDPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
				File fileSD = new File(SDPATH);

				File fileBySDSon = new File(fileSD, "poster");

				// File fileBySDSon = new File(fileBySD, "poster");
				if (!fileBySDSon.exists()) {
					fileBySDSon.mkdir();
				}
				saveBitmapPath = new File(fileBySDSon, URLEncoder.encode(name)).getAbsolutePath();

			} else {
				// 当sd卡不可用时
				
			}

			try {
				fos = new FileOutputStream(saveBitmapPath);
				bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					fos.flush();
					fos.close();
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}

		} else {
			return false;
		}

	}

}
