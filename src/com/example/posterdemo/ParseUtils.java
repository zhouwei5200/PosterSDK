package com.example.posterdemo;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

public class ParseUtils {
	public static PosterBean parseJson(String content, Context context) {
		// TODO Auto-generated method stub
		try {
			PosterBean posterBean = new PosterBean();
			List<PosterBeanItem> beanItems = new ArrayList<PosterBeanItem>();
			JSONObject jsonObject = new JSONObject(content);
			posterBean.setNote(jsonObject.getString("note"));
			posterBean.setStatue(jsonObject.getInt("status"));
			System.out.println("1111111221" + posterBean.getStatue());
			if (1 == posterBean.getStatue()) {
				System.out.println("2222111111" + posterBean.getNote());
				JSONArray jsonArray = jsonObject.getJSONArray("data");
				System.out.println("1111111111" + jsonArray.toString());
				if (jsonArray != null) {
					System.out.println("2222222" + jsonArray.toString());
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
						PosterBeanItem beanItem = new PosterBeanItem();
						beanItem.setApkName(jsonObject2.getString("apkName"));
						beanItem.setAppId(jsonObject2.getInt("appId"));
						beanItem.setImg(jsonObject2.getString("img"));
						beanItem.setDownload(jsonObject2.getString("download"));
						beanItem.setImg_size(jsonObject2.getString("img_size"));
						beanItem.setIntroduce(jsonObject2.getString("introduce"));
						beanItem.setVersion(jsonObject2.getString("version"));
						beanItem.setBundle_app(jsonObject2.getString("bundle_app"));
						beanItems.add(beanItem);
					}
					posterBean.setData(beanItems);
					return posterBean;
				} else {
					// 如果为null 证明是上传
					return posterBean;
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Toast.makeText(context, "解析失败", Toast.LENGTH_LONG).show();
			return null;
		}
	}

	public static boolean parseCommitJson(String content, Context context) {
		// TODO Auto-generated method stub
		try {
			PosterBean posterBean = new PosterBean();
			List<PosterBeanItem> beanItems = new ArrayList<PosterBeanItem>();
			JSONObject jsonObject = new JSONObject(content);
			posterBean.setNote(jsonObject.getString("note"));
			posterBean.setStatue(jsonObject.getInt("status"));
			System.out.println("1111111221" + posterBean.getStatue());
			if (1 == posterBean.getStatue()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Toast.makeText(context, "解析失败", Toast.LENGTH_LONG).show();
			return false;
		}
	}

	public static PosterVersionBean parseVersionJson(String content, Context context) {
		// TODO Auto-generated method stub
		try {
			PosterVersionBean posterBean = new PosterVersionBean();
			List<PosterBeanItem> beanItems = new ArrayList<PosterBeanItem>();
			JSONObject jsonObject = new JSONObject(content);
			posterBean.setNote(jsonObject.getString("note"));
			posterBean.setStatue(jsonObject.getInt("status"));
			posterBean.setData(jsonObject.getString("data"));
			return posterBean;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Toast.makeText(context, "解析失败", Toast.LENGTH_LONG).show();
			return null;
		}
	}

}
