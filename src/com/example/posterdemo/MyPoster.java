package com.example.posterdemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.Toast;

public class MyPoster {
	private MyPoster() {
	}

	private static class PosterInstance {
		private static final MyPoster myPoster = new MyPoster();
	}

	// 用于图片缓存
	public static final HashMap<String, WeakReference<Bitmap>> mImageCache = new HashMap<String, WeakReference<Bitmap>>();
	private static MyPoster myPoster = null;
	public static Intent intent;

	public static MyPoster getInstance() {
		return PosterInstance.myPoster;
	}

	private String serviceName = "com.example.posterdemo.PosterService";
	private String mUrl = "http://api.lovek12.cn/index.php?r=ad/index&version=v0.0&os=ad";
	/**
	 * 定义集合，静态，用于接收图片
	 */
	public static List<ImageView> imageViews = new ArrayList<ImageView>();
	private Runnable runnable;
	public static PosterBean posterBean;

	/**
	 * 
	 * @param context
	 */
	public void startPost(Context context) {
		if (!ApkUtils.isServiceWork(context, serviceName)) {
			intent = new Intent(context, PosterService.class);
			context.startService(intent);
		} else {
			Toast.makeText(context, "广告已经开启", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 开启的context和关闭的context必须是同一个
	 * 
	 * @param context
	 */
	public void stopPost(Context context) {
		if (ApkUtils.isServiceWork(context, serviceName)) {
			context.stopService(intent);
		}
	}
	//获取当前最新版本
	public String obtainVersion(Context context){
		VersionUpdate.isUptodata(context);
		return VersionUpdate.version;
	}



	/**
	 * 加载图片
	 * 
	 * @param mUrl
	 * @param context
	 * @return
	 */
	public ImageView requestPosterBitmap(final String mUrl, final Context context) {
		if (mUrl == null) {
			return null;
		}
		// 先判断内存中是否存在该图片
		final ImageView imageView = new ImageView(context);

		if (mImageCache.get(mUrl) != null && mImageCache.get(mUrl).get() != null) {
			Bitmap bitmap = mImageCache.get(mUrl).get();
			if (bitmap != null && !bitmap.isRecycled()) {
				Toast.makeText(context, "通过，内存加载", Toast.LENGTH_LONG).show();
				imageView.setImageBitmap(bitmap);
				return imageView;
			} else {
				mImageCache.remove(mUrl);
			}
		} else {
		}

		// 以下为sd卡缓存
		Bitmap bitmap = ImageCacheUtils.getBitmap(mUrl);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
			mImageCache.put(mUrl, new WeakReference<Bitmap>(bitmap));
			return imageView;
		}

		// 网络请求图片
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Looper.prepare();
					URL url = new URL(mUrl);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setConnectTimeout(5000);
					conn.setRequestProperty("Accept-Charset", "utf-8");
					conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
					conn.setRequestMethod("GET");
					if (200 == conn.getResponseCode()) {
						InputStream inputStream = conn.getInputStream();
						Bitmap mBitmap = BitmapFactory.decodeStream(inputStream);
						imageView.setImageBitmap(mBitmap);
						mImageCache.put(mUrl, new WeakReference<Bitmap>(mBitmap));
						ImageCacheUtils.SaveBitmap(mBitmap, mUrl);
					} else {
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					Looper.loop();
				}
			}
		};
		ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		cachedThreadPool.execute(runnable);
		return imageView;
	}

	/**
	 * 让用户首次注册 并发网络请求 并 更新数据
	 * 
	 * @param context
	 */
	public void requestPoster(final Context context) {
		// ApkUtils.isInstall(context, "");
		/**
		 * 1.先加载本地json数据
		 */
		String nowLoadingContent = (String) SPUtils.get(context, "JSON", "");
		System.out.println("bean  111 ");
		if (!TextUtils.isEmpty(nowLoadingContent)) {
			System.out.println("bean  221 " + nowLoadingContent);
			posterBean = ParseUtils.parseJson(nowLoadingContent, context);
			System.out.println("bean   " + posterBean.toString());
			if (posterBean.getStatue() == 1 && posterBean.getData() != null && posterBean.getData().size() > 0) {
				imageViews.clear();
				for (int i = 0; i < posterBean.getData().size(); i++) {
					ImageView imageView = requestPosterBitmap(posterBean.getData().get(i).getImg(), context);
					imageViews.add(imageView);
				}
			}
			/**
			 * 2.加载完以后进行网络更新
			 */
			runnable = new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Looper.prepare();
						URL url = new URL(mUrl);
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						conn.setConnectTimeout(5000);
						conn.setRequestProperty("Accept-Charset", "utf-8");
						conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
						conn.setRequestMethod("GET");
						if (200 == conn.getResponseCode()) {
							InputStream inputStream = conn.getInputStream();
							String content = NetUtils.convertStreamToString(inputStream);
							if (!TextUtils.isEmpty(content)) {
								SPUtils.put(context, "JSON", content);

							}
						} else {
							Toast.makeText(context, "请求失败1", Toast.LENGTH_LONG).show();
						}

					} catch (Exception e) {
						// TODO Auto-generated catch block
						Toast.makeText(context, "请求失败2", Toast.LENGTH_LONG).show();
						e.printStackTrace();
					} finally {
						Looper.loop();
					}
				}
			};

			ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
			cachedThreadPool.execute(runnable);
		} else {
			// 如果第一次没有数据则加载本地资源
			imageViews.clear();
			posterBean = ParseUtils.parseJson(StringDemo.content, context);
			SPUtils.put(context, "JSON", StringDemo.content);
			imageViews.addAll(initDrawable(context));
			System.out.println("长度为" + imageViews.size());
		}

	}

	// 测试 将asset中的jpg添加到集合中
	private static List<ImageView> initDrawable(Context context) {
		// TODO Auto-generated method stub
		InputStream inputStream1;

		try {
			inputStream1 = context.getAssets().open("1.png");

			Bitmap bit1 = BitmapFactory.decodeStream(inputStream1);

			ImageView imageView1 = new ImageView(context);

			imageView1.setImageBitmap(bit1);

			List<ImageView> imageViews = new ArrayList<ImageView>();
			imageViews.add(imageView1);

			return imageViews;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	

}
