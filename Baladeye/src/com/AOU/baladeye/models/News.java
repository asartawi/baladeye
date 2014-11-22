package com.AOU.baladeye.models;

import android.graphics.Bitmap;

public class News {

	private String title;
	private String content;
	private String pic;
	private Bitmap bitmap;
	
	public News(String title, String content, String pic, Bitmap bitmap) {
		super();
		this.title = title;
		this.content = content;
		this.pic = pic;
		this.bitmap = bitmap;
	}
	
	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	
}
