package cn.com.studyshop.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.com.studyshop.utils.TokenUtils;

/**
 * 获取授权Token
 * 
 * @author weiyiLiu
 *
 */
@Service
public class TokenService {

	@Value("${face.auth.client.id}")
	private String clientId;

	@Value("${face.auth.client.secret}")
	private String clientSecret;

	@Value("${face.auth.token.access.url}")
	private String tokenRequestUrl;

	@Value("${face.auth.token.validate.day}")
	private Integer tokenValidateDay;

	public String faceRegist() {

		return TokenUtils.getInstance(tokenRequestUrl, clientId, clientSecret, tokenValidateDay).getToken();
	}
}
