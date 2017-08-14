package cht.com.cht.model;

import java.util.Date;

public class Comment {
	
	private String comment_id;
	private String comment_parent_id;
	private String comment_content;
	private Date comment_date;
	private String comment_is_leaf;
	private String comment_from_user_id;
	private String comment_to_user_id;
	
	public Comment(String comment_id, String comment_parent_id,
			String comment_content, Date comment_date, String comment_is_leaf,
			String comment_from_user_id, String comment_to_user_id) {
		super();
		this.comment_id = comment_id;
		this.comment_parent_id = comment_parent_id;
		this.comment_content = comment_content;
		this.comment_date = comment_date;
		this.comment_is_leaf = comment_is_leaf;
		this.comment_from_user_id = comment_from_user_id;
		this.comment_to_user_id = comment_to_user_id;
	}

	public String getComment_id() {
		return comment_id;
	}

	public void setComment_id(String comment_id) {
		this.comment_id = comment_id;
	}

	public String getComment_parent_id() {
		return comment_parent_id;
	}

	public void setComment_parent_id(String comment_parent_id) {
		this.comment_parent_id = comment_parent_id;
	}

	public String getComment_content() {
		return comment_content;
	}

	public void setComment_content(String comment_content) {
		this.comment_content = comment_content;
	}

	public Date getComment_date() {
		return comment_date;
	}

	public void setComment_date(Date comment_date) {
		this.comment_date = comment_date;
	}

	public String getComment_is_leaf() {
		return comment_is_leaf;
	}

	public void setComment_is_leaf(String comment_is_leaf) {
		this.comment_is_leaf = comment_is_leaf;
	}

	public String getComment_from_user_id() {
		return comment_from_user_id;
	}

	public void setComment_from_user_id(String comment_from_user_id) {
		this.comment_from_user_id = comment_from_user_id;
	}

	public String getComment_to_user_id() {
		return comment_to_user_id;
	}

	public void setComment_to_user_id(String comment_to_user_id) {
		this.comment_to_user_id = comment_to_user_id;
	}

	@Override
	public String toString() {
		return "Comment [comment_id=" + comment_id + ", comment_parent_id="
				+ comment_parent_id + ", comment_content=" + comment_content
				+ ", comment_date=" + comment_date + ", comment_is_leaf="
				+ comment_is_leaf + ", comment_from_user_id="
				+ comment_from_user_id + ", comment_to_user_id="
				+ comment_to_user_id + "]";
	}


	
}
