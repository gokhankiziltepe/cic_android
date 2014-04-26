package com.itu.araci.mobile.client.android.model;

public class Followed {
	private Integer id;
	private String followerInfo;
	private String dateAdded;
	private Integer type;
	private String followedEntityBase;

	// place 0
	// user 1

	public String getFollowedEntityBase() {
		return followedEntityBase;
	}

	public void setFollowedEntityBase(String followedEntityBase) {
		this.followedEntityBase = followedEntityBase;
	}

	public Integer getId() {
		return id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFollowerInfo() {
		return followerInfo;
	}

	public void setFollowerInfo(String followerInfo) {
		this.followerInfo = followerInfo;
	}

	public String getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(String dateAdded) {
		this.dateAdded = dateAdded;
	}

}
