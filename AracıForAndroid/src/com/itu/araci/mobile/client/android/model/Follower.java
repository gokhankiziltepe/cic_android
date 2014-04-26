package com.itu.araci.mobile.client.android.model;

public class Follower {
	private Integer id;
	private String followerInfo;
	private String followerId;
	private String dateAdded;
	private Boolean isApproved;

	public Integer getId() {
		return id;
	}

	public String getFollowerId() {
		return followerId;
	}

	public void setFollowerId(String followerId) {
		this.followerId = followerId;
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

	public Boolean getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(Boolean isApproved) {
		this.isApproved = isApproved;
	}

}
