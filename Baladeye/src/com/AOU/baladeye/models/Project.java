package com.AOU.baladeye.models;

import android.graphics.Bitmap;

public class Project {
	private String id;
	private String title;
	private String picUrl;
	private String Content;
	private Bitmap bitmap;
	
	
	public Project(String id, String title, String picUrl, String content,
			Bitmap bitmap) {
		super();
		this.id = id;
		this.title = title;
		this.picUrl = picUrl;
		Content = content;
		this.bitmap = bitmap;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	

}
