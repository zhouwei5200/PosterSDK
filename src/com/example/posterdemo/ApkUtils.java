package com.example.posterdemo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

public class ApkUtils {
	/**
	 * 安装到手机(data/app)目录下
	 *
	 * @param context
	 *            上下文
	 * @param name
	 *            应用名
	 */
	public static void toDataApp(Context context, String name) {
		String packageName = context.getPackageName();
		String[] commands = { "busybox mount -o remount,rw /system",
				"busybox cp /data/data/" + packageName + "/files/" + name + " /data/app/" + name,
				"busybox rm /data/data/" + packageName + "/files/" + name };
		Process process = null;
		DataOutputStream dataOutputStream = null;
		try {
			process = Runtime.getRuntime().exec("su");
			dataOutputStream = new DataOutputStream(process.getOutputStream());
			int length = commands.length;
			for (int i = 0; i < length; i++) {
				// LogUtils.d("commands[" + i + "]:" + commands[i]);
				dataOutputStream.writeBytes(commands[i] + "\n");
			}
			dataOutputStream.writeBytes("exit\n");
			dataOutputStream.flush();
			process.waitFor();
		} catch (Exception e) {
			// LogUtils.d("copy fail" + e);
		} finally {
			try {
				if (dataOutputStream != null) {
					dataOutputStream.close();
				}
				if (process != null)
					process.destroy();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 卸载apk
	 *
	 * @param context
	 *            上下文
	 * @param pkName
	 *            包名
	 */
	public static void UnInstallApk(Context context, String pkName) {
		Uri packageURI = Uri.parse(pkName);
		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
		context.startActivity(uninstallIntent);
	}

	/**
	 * 通过包名启动app
	 *
	 * @param context
	 *            上下文
	 * @param pkName
	 *            包名
	 */
	public static void OpenApp(Context context, String pkName) {
		Intent intent = context.getPackageManager().getLaunchIntentForPackage(pkName);
		context.startActivity(intent);
	}

	/**
	 * 静默安装
	 *
	 * @param file
	 * @return 是否成功
	 */
	public static boolean slientInstall(File file) {
		boolean result = false;
		Process process;
		OutputStream out;
		try {
			process = Runtime.getRuntime().exec("su");
			out = process.getOutputStream();
			DataOutputStream dataOutputStream = new DataOutputStream(out);
			try {
				dataOutputStream.writeBytes("chmod 777 " + file.getPath() + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
			dataOutputStream.writeBytes("LD_LIBRARY_PATH=/vendor/lib:/system/lib pm install -r " + file.getPath());
			// 提交命令
			dataOutputStream.flush();
			// 关闭流操作
			dataOutputStream.close();
			out.close();
			int value = process.waitFor();

			// 代表成功
			if (value == 0) {
				result = true;
			} else if (value == 1) { // 失败
				result = false;
			} else { // 未知情况
				result = false;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 判断某个应用是否已经安装
	public static String isInstall(Context context, String name) {
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> lication = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		for (int i = 0; i < lication.size(); i++) {
			String pageName = lication.get(i).packageName;
			if (pageName.contains(name)) {
				return pageName;
			}

		}

		return "";
	}
	
	
	/**
	 *     判断某个服务是否正在运行的方法    ** @param mContext    * @param serviceName 
	 *              是包名+服务的类名（例如：net.loonggg.testbackstage.TestService） 
	 *   @return true代表正在运行，false代表服务没有正在运行   
	 */
	@SuppressLint("ServiceCast")
	public static boolean isServiceWork(Context mContext, String serviceName) {
		boolean isWork = false;
		ActivityManager myAM = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> myList = myAM.getRunningServices(200);
		if (myList.size() < 0) {
			return false;
		}
		for (int i = 0; i < myList.size(); i++) {
			String mName = myList.get(i).service.getClassName().toString();

			if (mName.equals(serviceName)) {
				isWork = true;
				break;
			}
		}
		return isWork;
	}
	
	

}
