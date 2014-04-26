package com.itu.araci.mobile.client.android.model;

public class Image {
	private String id;
	private String path;
	private String timestamp;

	public String getPath() {
		return path;
	}

	public void setPath(String data) {
		this.path = data;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
