package com.example.posterdemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

public class MyApplication extends Application {
	private String mUrl = "http://api.lovek12.cn/index.php?r=ad/index&version=v0.0&os=ad";
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	//	Toast.makeText(this, "我是jar包的Application", Toast.LENGTH_LONG).show();
	//	requestPoster(getApplicationContext());
		
	}
	public void requestPoster(final Context context) {
		
		new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				try {
					Looper.prepare();
					URL url = new URL(mUrl);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setConnectTimeout(5000);
					conn.setRequestProperty("Accept-Charset", "utf-8");
					conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
					conn.setRequestMethod("GET");
					System.out.println("返回码为 " + conn.getResponseCode());
					if (200 == conn.getResponseCode()) {
						InputStream inputStream = conn.getInputStream();
						String content = convertStreamToString(inputStream);
						Toast.makeText(context, "请求成功", Toast.LENGTH_LONG).show();
						System.out.println("请求成功" + content);
						if (!TextUtils.isEmpty(content)) {
							SPUtils.put(context, "content", content);
							//PosterBean posterBean = parseJson(content, context);
							//System.out.println("对象为" + posterBean.toString());
						}
					} else {
						Toast.makeText(context, "请求失败1", Toast.LENGTH_LONG).show();
					}
					Looper.prepare();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(context, "请求失败2", Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
				
			}
		}.start();
				

		/*new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					URL url = new URL(mUrl);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setConnectTimeout(5000);
					conn.setRequestProperty("Accept-Charset", "utf-8");
					conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
					conn.setRequestMethod("GET");
					System.out.println("返回码为 " + conn.getResponseCode());
					if (200 == conn.getResponseCode()) {
						InputStream inputStream = conn.getInputStream();
						String content = convertStreamToString(inputStream);
						Toast.makeText(context, "请求成功", Toast.LENGTH_LONG).show();
						System.out.println("请求成功" + content);
						if (!TextUtils.isEmpty(content)) {
							SPUtils.put(context, "content", content);
							//PosterBean posterBean = parseJson(content, context);
							//System.out.println("对象为" + posterBean.toString());
						}
					} else {
						Toast.makeText(context, "请求失败1", Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(context, "请求失败2", Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
			}
		}.run();
	*/
	//	ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		//cachedThreadPool.execute(runnable);
	}

	private String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();

	}

}
