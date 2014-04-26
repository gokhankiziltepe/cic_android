package com.itu.araci.mobile.client.android.model;

public class Comment {
	private String id;
	private String commentText;
	private String dateAdded;
	private String ownerInfo;
	private String commentedToEntityBase;

	public String getCommentedToEntityBase() {
		return commentedToEntityBase;
	}

	public void setCommentedToEntityBase(String commentedToEntityBase) {
		this.commentedToEntityBase = commentedToEntityBase;
	}

	public String getCommentText() {
		return commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	public String getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(String dateAdded) {
		this.dateAdded = dateAdded;
	}

	public String getOwnerInfo() {
		return ownerInfo;
	}

	public void setOwnerInfo(String ownerInfo) {
		this.ownerInfo = ownerInfo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
