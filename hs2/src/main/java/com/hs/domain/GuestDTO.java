package com.hs.domain;

public class GuestDTO {
	private long num;
	private String userId;
	private String userName;
	private String content;
	private String reg_date;
	private int block;
	private boolean deletePermit;
	
	public long getNum() {
		return num;
	}
	public void setNum(long num) {
		this.num = num;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getReg_date() {
		return reg_date;
	}
	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
	public int getBlock() {
		return block;
	}
	public void setBlock(int block) {
		this.block = block;
	}
	public boolean isDeletePermit() {
		return deletePermit;
	}
	public void setDeletePermit(boolean deletePermit) {
		this.deletePermit = deletePermit;
	}
}
