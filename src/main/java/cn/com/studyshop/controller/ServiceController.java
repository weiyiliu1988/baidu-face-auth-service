package cn.com.studyshop.controller;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Maps;

import cn.com.studyshop.service.TokenService;
import cn.com.studyshop.vo.FaceRegistRsltVO;
import cn.com.studyshop.vo.FaceRegistVO;

@RestController
@RequestMapping("/service")
public class ServiceController {

	private Logger logger = LoggerFactory.getLogger(ServiceController.class);

	@Autowired
	private TokenService tokenService;

	@Autowired
	private RestTemplate restTemplate;

	@SuppressWarnings("resource")
	@GetMapping("/status")
	public String serviceStatus() throws UnsupportedEncodingException {

		HttpHeaders headers = new HttpHeaders();

		// headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		headers.set("Content-Type", "application/x-www-form-urlencoded");
		FaceRegistVO faceRegistVo = new FaceRegistVO();
		MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<String, String>();
		faceRegistVo.setUid(UUID.randomUUID().toString());

		requestMap.add("uid", "test_323");
		requestMap.add("group_id", "test");

		faceRegistVo.setGroup_id("test");
		faceRegistVo.setUser_info("18565666666");
		Base64.Encoder encoder = Base64.getEncoder();
		String imgFile = "C:\\Users\\weiyiLiu\\Desktop\\me.jpg";// 待处理的图片
		byte[] imageByte = new byte[1024];
		try {
			new FileInputStream(imgFile).read(imageByte);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String base64Str = encoder.encodeToString(imageByte);

		System.out.println("=============================");
		System.out.println("=============================");
		System.out.println("=============================");

		System.out.println(base64Str);
		requestMap.add("image", base64Str);
		requestMap.add("user_info", "123312132");
		faceRegistVo.setImage(URLEncoder.encode(base64Str, "utf-8"));
		Map<String, String> requestParamMap = Maps.newHashMap();
		requestParamMap.put("token", tokenService.faceRegist());
		String url = "https://aip.baidubce.com/rest/2.0/face/v2/faceset/user/add?token="
				+ URLEncoder.encode(tokenService.faceRegist(), "utf-8");
		FaceRegistRsltVO restStrResult = null;
		logger.debug("[请求URL]===={}", url);
		try {
			restStrResult = restTemplate.postForObject(url, new HttpEntity(requestMap, headers),
					FaceRegistRsltVO.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		// FaceRegistRsltVO restStrResult = restTemplate
		// .postForEntity(url, new HttpEntity<Map>(requestMap, headers),
		// FaceRegistRsltVO.class, requestParamMap)
		// .getBody();

		return restStrResult.toString();
	}
}
