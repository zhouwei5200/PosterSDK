package com.example.posterdemo;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

public class VersionUpdate {
	public static String version = "V1.0";
	
	private final static String versionUrl = "http://api.lovek12.cn/index.php?r=ad/version";
	public static int FLAG = 0;
	private static Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:

				break;

			default:
				break;
			}
		};
	};

	public static String isUptodata(final Context context) {
		Map<String, String> versionMap = new HashMap<String, String>();
		versionMap.put("version", version);
		final String completUrl = NetUtils.appendParams(versionUrl, versionMap);

		Runnable updataRunnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Looper.prepare();
					URL url = new URL(completUrl);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setConnectTimeout(5000);
					// conn.setRequestProperty("Accept-Charset", "utf-8");
					// conn.setRequestProperty("Content-Type",
					// "application/x-www-form-urlencoded");
					conn.setRequestMethod("GET");
					System.out.println("数据准备提交" + conn.getResponseCode());
					if (200 == conn.getResponseCode()) {
						System.out.println("数据开始提交");
						InputStream inputStream = conn.getInputStream();
						String content = NetUtils.convertStreamToString(inputStream);

						if (!TextUtils.isEmpty(content)) {
							PosterVersionBean posterVersionBean = ParseUtils.parseVersionJson(content, context);
							Message message = new Message();
							message.obj = posterVersionBean;
							handler.sendMessage(message);
							if (posterVersionBean.getData() != null) {
								Toast.makeText(context, "当前最新版本是" + posterVersionBean.getData(), Toast.LENGTH_LONG)
										.show();
								version = posterVersionBean.getData();

							} else {
								Toast.makeText(context, "当前是最新版本", Toast.LENGTH_LONG).show();
							}

						}
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
		cachedThreadPool.execute(updataRunnable);

		return null;
	}

}
