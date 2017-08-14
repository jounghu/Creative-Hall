package cht.com.cht.model;

import java.io.Serializable;

public class Collection implements Serializable {
	private int id;
	private int topic_id;
	private int user_id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTopic_id() {
		return topic_id;
	}
	public void setTopic_id(int topic_id) {
		this.topic_id = topic_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public Collection(int id, int topic_id, int user_id) {
		super();
		this.id = id;
		this.topic_id = topic_id;
		this.user_id = user_id;
	}
	public Collection() {
		super();
	}
	@Override
	public String toString() {
		return "Collection [id=" + id + ", topic_id=" + topic_id + ", user_id="
				+ user_id + "]";
	}
	
}
