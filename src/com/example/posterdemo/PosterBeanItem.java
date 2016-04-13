package com.example.posterdemo;

public class PosterBeanItem {
	private String apkName;
	private int appId;
	private String img;
	private String img_size;
	private String introduce;
	private String version;
	private String download;
	private String bundle_app;

	public String getBundle_app() {
		return bundle_app;
	}

	public void setBundle_app(String bundle_app) {
		this.bundle_app = bundle_app;
	}

	public String getDownload() {
		return download;
	}

	public void setDownload(String download) {
		this.download = download;
	}

	public String getApkName() {
		return apkName;
	}

	public void setApkName(String apkName) {
		this.apkName = apkName;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getImg_size() {
		return img_size;
	}

	public void setImg_size(String img_size) {
		this.img_size = img_size;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "PosterBeanItem [apkName=" + apkName + ", appId=" + appId + ", img=" + img + ", img_size=" + img_size
				+ ", introduce=" + introduce + ", version=" + version + ", download=" + download + ", bundle_app="
				+ bundle_app + "]";
	}


}
