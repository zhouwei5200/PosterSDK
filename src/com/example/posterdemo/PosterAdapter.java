package com.example.posterdemo;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class PosterAdapter extends PagerAdapter {
	private List<View> viewList;
	private Context context;
	private String url = "http://api.lovek12.cn/index.php?r=ad/statistical";
	private Runnable runnableCommit;

	public PosterAdapter(List viewList, Context context) {
		this.viewList = viewList;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (viewList.size() == 1) {
			return 1;
		}
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return super.getItemPosition(object);
	}

	@Override
	public void destroyItem(View view, int arg1, Object arg2) {
		int potion = arg1 % viewList.size();

		((ViewPager) view).removeView(viewList.get(potion));
	}

	@Override
	public Object instantiateItem(View arg0, final int arg1) // 实例化Item
	{
		System.out.println("当前页为..." + arg1);
		/**
		 * 防止向左滑动
		 */
		if (viewList.size() == 0) {
			return null;
		}
		final int potion = arg1 % viewList.size();
		System.out.println("当前页为." + potion);

		ImageView imageView = (ImageView) viewList.get(potion);
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String packageName = ApkUtils.isInstall(context,
						MyPoster.posterBean.getData().get(potion).getBundle_app());
				if (!TextUtils.isEmpty(packageName)) {
					// 如果已经安装该应用
					if (packageName.contains("qupingfang")) {
						Map<String, String> paramMap = new HashMap<String, String>();
						paramMap.put("fromapp", "hlqpf");
						paramMap.put("toapp", "hlkxw");
						String competUrl = NetUtils.appendParams(url, paramMap);
						System.out.println("请求地址为" + competUrl);
						commitRequest(competUrl);

					}
					context.stopService(MyPoster.intent);
					ApkUtils.OpenApp(context, packageName);
					
				} else {
					// 没有安装该应用 调到浏览器 提示用户安装
					context.stopService(MyPoster.intent);
					String url = MyPoster.posterBean.getData().get(potion).getDownload(); // web
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setData(Uri.parse(url));
					context.startActivity(intent);
				}
			}
		});
		if (imageView.getParent() != null) {
			((ViewPager) imageView.getParent()).removeView(imageView);
		}
		((ViewPager) arg0).addView((View) imageView, -1);
		return imageView;
	}

	// 用于上传结果
	private void commitRequest(final String mUrl) {

		runnableCommit = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Looper.prepare();
					URL url = new URL(mUrl);
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
							boolean isSuccess = ParseUtils.parseCommitJson(content, context);
							if (isSuccess) {
								Toast.makeText(context, "数据提交成功", Toast.LENGTH_LONG).show();
							} else {
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
		cachedThreadPool.execute(runnableCommit);

	}

}
