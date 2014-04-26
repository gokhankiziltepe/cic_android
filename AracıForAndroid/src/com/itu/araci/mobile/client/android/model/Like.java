package com.itu.araci.mobile.client.android.model;

public class Like {
	private String id;
	private String name;
	private Integer type;
	private String dateAdded;
	private Boolean isLike;
	private String likedEntityBase;

	public String getLikedEntityBase() {
		return likedEntityBase;
	}

	public void setLikedEntityBase(String likedEntityBase) {
		this.likedEntityBase = likedEntityBase;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(String dateAdded) {
		this.dateAdded = dateAdded;
	}

	public Boolean getIsLike() {
		return isLike;
	}

	public void setIsLike(Boolean isLike) {
		this.isLike = isLike;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}
