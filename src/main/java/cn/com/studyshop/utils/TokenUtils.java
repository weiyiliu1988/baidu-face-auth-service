package cn.com.studyshop.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.collect.Maps;
import com.google.gson.Gson;

import cn.com.studyshop.face.constant.FaceConstant;

/**
 * 授权token获取
 * 
 * @author weiyiLiu
 *
 */
public class TokenUtils {

	private Logger logger = LoggerFactory.getLogger(TokenUtils.class);

	private static final Map<String, Date> TK_VALIDATE = Maps.newHashMap();
	private static final Map<String, String> TK_MAP = Maps.newHashMap();

	private static Integer validateDay = 10;// default 10 days

	// 组合
	private static TokenVO tokenVO = null;

	private static TokenUtils tokenUtils = null;

	private TokenUtils() {

	}

	public static TokenUtils getInstance(String authHost, String clientId, String clientSecret, Integer validateDay) {

		if (null == tokenUtils) {
			synchronized (TokenUtils.class) {
				if (null != tokenUtils) {
					return tokenUtils;
				}
				tokenUtils = new TokenUtils();
				// 各部初始化
				TokenUtils.tokenVO = tokenUtils.new TokenVO(authHost, clientId, clientSecret);
				TokenUtils.validateDay = validateDay;
				TK_MAP.put(FaceConstant.FC_TOKEN, null);
				TK_VALIDATE.put(FaceConstant.FC_TOKEN, null);

			}
		}

		return tokenUtils;
	}

	/**
	 * 获取token
	 * 
	 * @return
	 */
	public String getToken() {

		// token未设定
		if (null == TK_VALIDATE.get(FaceConstant.FC_TOKEN) || null == TK_MAP.get(FaceConstant.FC_TOKEN)) {
			return setAndGetToken();
		}

		// token过期
		if (null != TK_VALIDATE.get(FaceConstant.FC_TOKEN)
				&& TK_VALIDATE.get(FaceConstant.FC_TOKEN).compareTo(new Date()) < 0) {
			return setAndGetToken();
		}

		return TokenUtils.TK_MAP.get(FaceConstant.FC_TOKEN);
	}

	private String setAndGetToken() {
		// 获取token
		String token = TokenUtils.tokenVO.getAuth();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, TokenUtils.validateDay);// 有效时间
		Date date = calendar.getTime();
		TK_VALIDATE.put(FaceConstant.FC_TOKEN, date);
		TK_MAP.put(FaceConstant.FC_TOKEN, token);
		return token;
	}

	class TokenVO {

		private String authHost;
		private String ak;
		private String sk;

		private TokenVO(String authHost, String ak, String sk) {
			this.authHost = authHost;
			this.ak = ak;
			this.sk = sk;
		}

		/**
		 * 获取API访问token 该token有一定的有效期，需要自行管理，当失效时需重新获取.
		 * 
		 * @param ak
		 *            - 百度云官网获取的 API Key
		 * @param sk
		 *            - 百度云官网获取的 Securet Key
		 * @return assess_token 示例：
		 *         "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567"
		 */
		public String getAuth() {

			String getAccessTokenUrl = authHost
					// 1. grant_type为固定参数
					+ "grant_type=client_credentials"
					// 2. 官网获取的 API Key
					+ "&client_id=" + ak
					// 3. 官网获取的 Secret Key
					+ "&client_secret=" + sk;

			logger.debug("[获取token] ===============请求地址:{}", getAccessTokenUrl);

			try {
				URL realUrl = new URL(getAccessTokenUrl);
				// 打开和URL之间的连接
				HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
				connection.setRequestMethod(RequestMethod.GET.name());
				connection.connect();
				// 获取所有响应头字段
				Map<String, List<String>> map = connection.getHeaderFields();
				// 遍历所有的响应头字段
				for (String key : map.keySet()) {
					System.err.println(key + "--->" + map.get(key));
				}
				// 定义 BufferedReader输入流来读取URL的响应
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String result = "";
				String line;
				while ((line = in.readLine()) != null) {
					result += line;
				}
				/**
				 * 返回结果示例
				 */
				logger.debug("[token获取返回结果] ============result:{}", result);
				Gson gson = new Gson();
				Map<?, ?> rspMap = gson.fromJson(result, Map.class);
				String access_token = String.valueOf(rspMap.get("access_token"));
				return access_token;
			} catch (Exception e) {
				System.err.printf("获取token失败！");
				logger.debug("[获取token失败] 异常Exception:", e);
				e.printStackTrace(System.err);
			}
			return null;
		}

	}
}
