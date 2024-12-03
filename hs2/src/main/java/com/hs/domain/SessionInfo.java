package com.hs.domain;

public class SessionInfo {
	private Long memberIdx;
	private String userId;
	private String userName;
	private int userLevel;
	
	public Long getMemberIdx() {
		return memberIdx;
	}
	public void setMemberIdx(Long memberIdx) {
		this.memberIdx = memberIdx;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(int userLevel) {
		this.userLevel = userLevel;
	}
}
