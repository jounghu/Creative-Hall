package cht.com.cht.model;

import java.io.Serializable;

public class GameInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2858137131012324461L;

	private int id;
	private String head_img;
	private String title;
	private String content;
	private int zanNum;
	private String time;
	private String location;
	private String orgnization;
	
	
	
	
	
	public GameInfo() {
		super();
	}





	public GameInfo(int id, String head_img, String title, String content,
			int zanNum, String time, String location, String orgnization) {
		super();
		this.id = id;
		this.head_img = head_img;
		this.title = title;
		this.content = content;
		this.zanNum = zanNum;
		this.time = time;
		this.location = location;
		this.orgnization = orgnization;
	}





	public int getId() {
		return id;
	}





	public void setId(int id) {
		this.id = id;
	}





	public String getHead_img() {
		return head_img;
	}





	public void setHead_img(String head_img) {
		this.head_img = head_img;
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





	public int getZanNum() {
		return zanNum;
	}





	public void setZanNum(int zanNum) {
		this.zanNum = zanNum;
	}





	public String getTime() {
		return time;
	}





	public void setTime(String time) {
		this.time = time;
	}





	public String getLocation() {
		return location;
	}





	public void setLocation(String location) {
		this.location = location;
	}





	public String getOrgnization() {
		return orgnization;
	}





	public void setOrgnization(String orgnization) {
		this.orgnization = orgnization;
	}





	@Override
	public String toString() {
		return "GameInfo [id=" + id + ", head_img=" + head_img + ", title="
				+ title + ", content=" + content + ", zanNum=" + zanNum
				+ ", time=" + time + ", location=" + location
				+ ", orgnization=" + orgnization + "]";
	}


	@Override
	public boolean equals(Object o) {
		if(!(o instanceof GameInfo)){
			return false;
		}
		GameInfo gameInfo = (GameInfo) o;

		return gameInfo.id==this.id;
	}

	@Override
	public int hashCode() {
		return this.id;
	}
}
