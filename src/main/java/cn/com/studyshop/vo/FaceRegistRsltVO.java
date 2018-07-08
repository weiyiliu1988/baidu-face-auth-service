package cn.com.studyshop.vo;

import java.io.Serializable;

public class FaceRegistRsltVO implements Serializable {

	private static final long serialVersionUID = 1L;

	public String getError_code() {
		return error_code;
	}

	public void setError_code(String error_code) {
		this.error_code = error_code;
	}

	public String getLog_id() {
		return log_id;
	}

	public void setLog_id(String log_id) {
		this.log_id = log_id;
	}

	public String getError_msg() {
		return error_msg;
	}

	public void setError_msg(String error_msg) {
		this.error_msg = error_msg;
	}

	private String error_code;
	private String log_id;
	private String error_msg;

	@Override
	public String toString() {
		return "FaceRegistRsltVO [error_code=" + error_code + ", log_id=" + log_id + ", error_msg=" + error_msg + "]";
	}

}
