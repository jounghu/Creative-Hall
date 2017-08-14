package cht.com.cht.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class CommentInfo implements Serializable{
	private static final long serialVersionUID = 7135471387765654520L;
		private int id;
		private String from_user_img;
		private String from_user_nickname;
		private int to_user_id;
		private String to_user_nickname;
		private Timestamp from_user_comment_time;
		private String comment_content;
		
		
		
		
		public CommentInfo() {
			super();
		}




		public CommentInfo(int id, String from_user_img,
				String from_user_nickname, int to_user_id,
				String to_user_nickname, Timestamp from_user_comment_time,
				String comment_content) {
			super();
			this.id = id;
			this.from_user_img = from_user_img;
			this.from_user_nickname = from_user_nickname;
			this.to_user_id = to_user_id;
			this.to_user_nickname = to_user_nickname;
			this.from_user_comment_time = from_user_comment_time;
			this.comment_content = comment_content;
		}




		public int getId() {
			return id;
		}




		public void setId(int id) {
			this.id = id;
		}




		public String getFrom_user_img() {
			return from_user_img;
		}




		public void setFrom_user_img(String from_user_img) {
			this.from_user_img = from_user_img;
		}




		public String getFrom_user_nickname() {
			return from_user_nickname;
		}




		public void setFrom_user_nickname(String from_user_nickname) {
			this.from_user_nickname = from_user_nickname;
		}




		public int getTo_user_id() {
			return to_user_id;
		}




		public void setTo_user_id(int to_user_id) {
			this.to_user_id = to_user_id;
		}




		public String getTo_user_nickname() {
			return to_user_nickname;
		}




		public void setTo_user_nickname(String to_user_nickname) {
			this.to_user_nickname = to_user_nickname;
		}




		public Timestamp getFrom_user_comment_time() {
			return from_user_comment_time;
		}




		public void setFrom_user_comment_time(Timestamp from_user_comment_time) {
			this.from_user_comment_time = from_user_comment_time;
		}




		public String getComment_content() {
			return comment_content;
		}




		public void setComment_content(String comment_content) {
			this.comment_content = comment_content;
		}




		public static long getSerialversionuid() {
			return serialVersionUID;
		}




		@Override
		public String toString() {
			return "CommentInfo [id=" + id + ", from_user_img=" + from_user_img
					+ ", from_user_nickname=" + from_user_nickname
					+ ", to_user_id=" + to_user_id + ", to_user_nickname="
					+ to_user_nickname + ", from_user_comment_time="
					+ from_user_comment_time + ", comment_content="
					+ comment_content + "]";
		}

	@Override
	public boolean equals(Object o) {
		if(!(o instanceof CommentInfo)){
			return false;
		}

		CommentInfo commentInfo = (CommentInfo) o;

		return commentInfo.getId() == this.id;
	}

	@Override
	public int hashCode() {
		return this.id;
	}
}
