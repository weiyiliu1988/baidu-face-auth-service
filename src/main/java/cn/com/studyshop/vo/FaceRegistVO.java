package cn.com.studyshop.vo;

import java.io.Serializable;

public class FaceRegistVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String uid;
	private String group_id;
	private String image;
	private String user_info;
	private String action_type;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getUser_info() {
		return user_info;
	}

	public void setUser_info(String user_info) {
		this.user_info = user_info;
	}

	public String getAction_type() {
		return action_type;
	}

	public void setAction_type(String action_type) {
		this.action_type = action_type;
	}

	@Override
	public String toString() {
		return "faceRegistVO [uid=" + uid + ", group_id=" + group_id + ", image=" + image + ", user_info=" + user_info
				+ ", action_type=" + action_type + "]";
	}

}
