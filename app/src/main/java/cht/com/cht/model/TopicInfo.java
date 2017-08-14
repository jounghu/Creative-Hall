package cht.com.cht.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class TopicInfo implements Serializable{
	private int id;
	private int user_id;
	private String headImg;
	private String nickName;
	private String title;
	private String content;
	private Timestamp date;
	private int zan;
	private int comment;
	private List<String> photos;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
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
	public Timestamp getDate() {
		return date;
	}
	public void setDate(Timestamp date) {
		this.date = date;
	}
	public int getZan() {
		return zan;
	}
	public void setZan(int zan) {
		this.zan = zan;
	}
	public int getComment() {
		return comment;
	}
	public void setComment(int comment) {
		this.comment = comment;
	}
	public List<String> getPhotos() {
		return photos;
	}
	public void setPhotos(List<String> photos) {
		this.photos = photos;
	}
	public TopicInfo(int id, int user_id, String headImg, String nickName,
			String title, String content, Timestamp date, int zan, int comment,
			List<String> photos) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.headImg = headImg;
		this.nickName = nickName;
		this.title = title;
		this.content = content;
		this.date = date;
		this.zan = zan;
		this.comment = comment;
		this.photos = photos;
	}
	public TopicInfo() {
		super();
	}
	@Override
	public String toString() {
		return "TopicInfo [id=" + id + ", user_id=" + user_id + ", headImg="
				+ headImg + ", nickName=" + nickName + ", title=" + title
				+ ", content=" + content + ", date=" + date + ", zan=" + zan
				+ ", comment=" + comment + ", photos=" + photos + "]";
	}

	@Override
	public boolean equals(Object o) {
		if(!(o instanceof TopicInfo)){
			return false;
		}

		TopicInfo topicInfo = (TopicInfo) o;

		return topicInfo.getId() == this.id;
	}

	@Override
	public int hashCode() {
		return this.id;
	}
	
}
