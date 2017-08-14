package cht.com.cht.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * id 用户id
 * sch_id  学校id
 * username 用户名
 * passwrod 密码
 * gender 性别 1代表男性 2代表女性
 * phone 手机
 * nickname 昵称
 * head_img 头像
 * status 用户认证状态 1代表未认证 2代表认证成功
 * type  用户类型  1代表学生 2代表老师 3代表公司机构
 * registe_date 注册日期  yy:mm:dd hh:mm
 * @author Administrator
 *
 */
public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2282823369112253040L;
	
	
	private int id;
	private int sch_id;
	private String username;
	private String password;
	private int gender;
	private String phone;
	private String nickname;
	private String head_img;
	private int status;
	private int type;
	//private Date registe_date;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSch_id() {
		return sch_id;
	}
	public void setSch_id(int sch_id) {
		this.sch_id = sch_id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getHead_img() {
		return head_img;
	}
	public void setHead_img(String head_img) {
		this.head_img = head_img;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

	public User() {
		super();
	}
	public User(int id, int sch_id, String username, String password,
			int gender, String phone, String nickname, String head_img,
			int status, int type, Date registe_date) {
		super();
		this.id = id;
		this.sch_id = sch_id;
		this.username = username;
		this.password = password;
		this.gender = gender;
		this.phone = phone;
		this.nickname = nickname;
		this.head_img = head_img;
		this.status = status;
		this.type = type;

	}
	@Override
	public String toString() {
		return "User [stu_id=" + id + ", sch_id=" + sch_id + ", username="
				+ username + ", password=" + password + ", gender=" + gender
				+ ", phone=" + phone + ", nickname=" + nickname + ", head_img="
				+ head_img + ", status=" + status + ", type=" + type;

	} 
	
	
	
	
}
