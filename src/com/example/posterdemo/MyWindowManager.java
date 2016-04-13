package com.example.posterdemo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MyWindowManager {
	/**
	 * 悬浮窗View的参数
	 */
	private static LayoutParams smallWindowParams;

	/**
	 * 用于控制在屏幕上添加或移除悬浮窗
	 */
	private static WindowManager mWindowManager;
	private static FrameLayout frameLayout;
	private static Button button;
	private static ImageView buttonImage;
	private static ViewPager viewPager;
	private static final int ChangeViewPager = 0;
	private static final int BannerChangeDelay = 3000;
	private static int i;
	private static boolean isStop = false;
	private static Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == ChangeViewPager) {
				int position = viewPager.getCurrentItem();
				viewPager.setCurrentItem(position + 1);
				handler.removeMessages(ChangeViewPager);
				handler.sendEmptyMessageDelayed(ChangeViewPager, BannerChangeDelay);
			}
		}
	};

	/**
	 * 创建一个布局
	 */
	public static void createSmallWindow(Context context) {
		WindowManager windowManager = getWindowManager(context);
		int screenWidth = windowManager.getDefaultDisplay().getWidth();
		int screenHeight = windowManager.getDefaultDisplay().getHeight();
		frameLayout = loadView(context);
		if (smallWindowParams == null) {
			smallWindowParams = new LayoutParams();
			smallWindowParams.type = LayoutParams.TYPE_SYSTEM_ALERT;

			smallWindowParams.format = PixelFormat.RGBA_8888;
			smallWindowParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCH_MODAL;
			smallWindowParams.gravity = Gravity.CENTER_HORIZONTAL;
			smallWindowParams.width = 700;
			smallWindowParams.height = 200;
			// smallWindowParams.x = screenWidth;
			smallWindowParams.y = 450;
		}
		windowManager.addView(frameLayout, smallWindowParams);
	}

	/**
	 * 加载动态布局
	 * 
	 * @param context
	 * @return
	 */
	private static FrameLayout loadView(final Context context) {
		// 第一层布局
		FrameLayout frameLayout = frameLayout(context);
		// 第二层布局
		RelativeLayout layout = relativeLayout(context);
		// 第三层viewpager

		viewPagerLayout(context);
		// 初始化Adaptger,并设置循环
		initAdapter(context, MyPoster.imageViews);

		// 叉号的布局及点击事件
		imageLayout(context);
		// 把这几种布局进行添加
		layout.addView(viewPager);
		layout.addView(buttonImage);
		frameLayout.addView(layout);
		return frameLayout;
	}

	/**
	 * 动态布局 第一层
	 * 
	 * @param context
	 * @return
	 */
	private static FrameLayout frameLayout(final Context context) {
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		FrameLayout frameLayout = new FrameLayout(context);
		frameLayout.setId(0x1);
		frameLayout.setLayoutParams(layoutParams);
		return frameLayout;
	}

	/**
	 * 动态布局 第二层
	 * 
	 * @param context
	 * @return
	 */
	private static RelativeLayout relativeLayout(final Context context) {
		LayoutParams layoutParams2 = new LayoutParams(600, 800);
		RelativeLayout layout = new RelativeLayout(context);
		try {
			InputStream inputStream = context.getAssets().open("shop_cal_bg.png");
			Bitmap bit = BitmapFactory.decodeStream(inputStream);
			Drawable drawable = new BitmapDrawable(bit);
			layout.setBackground(drawable);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		layout.setId(111);
		layout.setLayoutParams(layoutParams2);
		return layout;
	}

	/**
	 * 动态布局第三层 viewpager
	 * 
	 * @param context
	 * @return
	 */
	private static void viewPagerLayout(final Context context) {
		ViewGroup.LayoutParams layoutParams1ViewPager = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		viewPager = new ViewPager(context);
		viewPager.setLayoutParams(layoutParams1ViewPager);
		/*
		 * // 先传图片 List viewList = new ArrayList(); List<Bitmap> bitList =
		 * initDrawable(context); for (int i = 0; i < bitList.size(); i++) {
		 * ImageView imageView = new ImageView(context);
		 * imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
		 * LayoutParams.MATCH_PARENT));
		 * imageView.setScaleType(ScaleType.CENTER_CROP);
		 * imageView.setImageBitmap(bitList.get(i));
		 * imageView.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub // 移除服务 context.stopService(MyPoster.intent); String url =
		 * "http://www.baidu.com"; // web address Intent intent = new
		 * Intent(Intent.ACTION_VIEW);
		 * intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 * intent.setData(Uri.parse(url)); context.startActivity(intent); } });
		 * viewList.add(imageView); }
		 */
		// return viewList;
	}

	/**
	 * 初始化viewAdapter
	 * 
	 * @param context
	 * @param viewList
	 */
	private static void initAdapter(final Context context, List viewList) {
		PosterAdapter posterAdapter = new PosterAdapter(viewList, context);
		viewPager.setAdapter(posterAdapter);
		i = viewList.size() ;
		viewPager.setCurrentItem(0);
		
		handler.sendEmptyMessageDelayed(ChangeViewPager, BannerChangeDelay);
		
	}

	/**
	 * 动态布局 第三层
	 * 
	 * @param context
	 */
	private static void imageLayout(final Context context) {
		RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(60, 60);
		layoutParams1.setMargins(630, 0, 0, 530);

		try {
			buttonImage = new ImageView(context);
			buttonImage.setLayoutParams(layoutParams1);

			InputStream inputStream;
			inputStream = context.getAssets().open("del.png");
			Bitmap delBit = BitmapFactory.decodeStream(inputStream);
			buttonImage.setImageBitmap(delBit);
			buttonImage.setId(222);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		buttonImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 关闭窗口 移除服务
				context.stopService(MyPoster.intent);
			}
		});
	}

	/**
	 * 将广告窗从屏幕上移除。
	 */
	public static void removeSmallWindow(Context context) {
		WindowManager windowManager = getWindowManager(context);
		windowManager.removeView(frameLayout);
	}

	private static WindowManager getWindowManager(Context context) {
		if (mWindowManager == null) {
			mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		}
		return mWindowManager;
	}
}
