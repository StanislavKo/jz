package com.hsd.jz.server.controller;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hsd.jz.api.consts.Consts;
import com.hsd.jz.api.db.DBUtils;
import com.hsd.jz.api.db.entity.JZUser;
import com.hsd.jz.api.db.entity.SearchTerm;
import com.hsd.jz.api.utils.NetworkUtils;
import com.hsd.jz.api.utils.PrimitiveUtils;
import com.hsd.jz.server.security.TokenAuthenticationService;

@RestController
public class Auth20Controller {

	private static final Logger logger = LoggerFactory.getLogger(Auth20Controller.class);

	private static final String VK_SECRET = System.getenv("JZ_VK_SECRET");
	private static final String OK_SECRET = System.getenv("JZ_OK_SECRET");
	private static final String OK_PUBLIC = System.getenv("JZ_OK_PUBLIC");

	@RequestMapping(value = "/vk", method = RequestMethod.GET)
	public ResponseEntity<String> vk(@RequestParam(value = "code", required = false) String code, @RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "error", required = false) String error, @RequestParam(value = "error_description", required = false) String errorDesc,
			HttpServletRequest req, HttpServletResponse res) {
		logger.info("vk code={}, state={}, error={}, desc={}", code, state, error, errorDesc);
		for (String key : req.getParameterMap().keySet()) {
			logger.info("vk key={}, value={}", key, req.getParameter(key));
		}

		if (code != null) {
			String url = "https://oauth.vk.com/access_token?client_id=" + Consts.VK_CLIENT_ID + "&client_secret=" + VK_SECRET + "&redirect_uri="
					+ Consts.VK_REDIRECT_URL + "&code=" + code;
			String jsonStr = NetworkUtils.loadUrlAsData(url);
			logger.info("vk url={}", url);
			logger.info("vk jsonStr={}", jsonStr);
			if (jsonStr != null) {
				JSONObject json = new JSONObject(jsonStr);
				if (json.has("access_token") && json.has("user_id")) {
					String username = Consts.USER_VK_PREFIX + json.getInt("user_id");
					String accessToken = json.getString("access_token");

					url = "https://api.vk.com/method/users.get?user_id=" + json.getInt("user_id") + "&v=5.52&access_token=" + accessToken;
					jsonStr = NetworkUtils.loadUrlAsData(url);
					logger.info("ok url2={}", url);
					logger.info("ok jsonStr2={}", jsonStr);
					json = new JSONObject(jsonStr);

					HttpHeaders headers = new HttpHeaders();
					if (json.has("response") && json.getJSONArray("response").getJSONObject(0).has("first_name")
							&& json.getJSONArray("response").getJSONObject(0).has("last_name")) {
						String username2 = json.getJSONArray("response").getJSONObject(0).getString("first_name") + " "
								+ json.getJSONArray("response").getJSONObject(0).getString("last_name");
						JZUser user = DBUtils.loadUser(username);
						if (user == null) {
							user = new JZUser().setCreated(System.currentTimeMillis()).setEnabled(true).setIp(req.getRemoteAddr()).setUsername(username)
									.setVkAccessToken(accessToken).setUsername2(username2);
						} else {
							user.setVkAccessToken(accessToken);
						}
						DBUtils.saveUser(user);
						String jwt = TokenAuthenticationService.getAuthenticationJwt(user.getUsername(), username2);
						headers.add("Location", "/?state=" + state + "&jwt=" + jwt);
						return new ResponseEntity<String>(headers, HttpStatus.FOUND);
					} else {
						headers.add("Location", "/");
						return new ResponseEntity<String>(headers, HttpStatus.FOUND);
					}
				}
			}
		}

		return new ResponseEntity("{}", HttpStatus.OK);
	}

	@RequestMapping(value = "/ok", method = RequestMethod.GET)
	public ResponseEntity<String> ok(@RequestParam(value = "code", required = false) String code, @RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "error", required = false) String error, HttpServletRequest req, HttpServletResponse res) {
		logger.info("ok code={}, state={}, error={}", code, state, error);
		for (String key : req.getParameterMap().keySet()) {
			logger.info("ok key={}, value={}", key, req.getParameter(key));
		}

		if (code != null) {
			String url = "https://api.ok.ru/oauth/token.do?client_id=" + Consts.OK_CLIENT_ID + "&client_secret=" + OK_SECRET + "&redirect_uri="
					+ Consts.OK_REDIRECT_URL + "&grant_type=authorization_code" + "&code=" + code;
			String jsonStr = NetworkUtils.loadPostUrlAsData(url);
			logger.info("ok url={}", url);
			logger.info("ok jsonStr={}", jsonStr);
			if (jsonStr != null) {
				JSONObject json = new JSONObject(jsonStr);
				if (json.has("access_token")) {
					String accessToken = json.getString("access_token");
					String secretKey = PrimitiveUtils.getMd5(accessToken + OK_SECRET);
					String sig = PrimitiveUtils.getMd5("application_key=" + OK_PUBLIC + "format=jsonmethod=users.getCurrentUser" + secretKey);
					logger.info("ok secretKey={} sig={}", secretKey, sig);

					url = "https://api.ok.ru/fb.do?application_key=" + OK_PUBLIC + "&format=json&method=users.getCurrentUser&sig=" + sig + "&access_token="
							+ accessToken;
					jsonStr = NetworkUtils.loadUrlAsData(url);
					logger.info("ok url2={}", url);
					logger.info("ok jsonStr2={}", jsonStr);
					json = new JSONObject(jsonStr);

					HttpHeaders headers = new HttpHeaders();
					if (json.has("uid")) {
						String username = Consts.USER_OK_PREFIX + json.getString("uid");
						JZUser user = DBUtils.loadUser(username);
						if (user == null) {
							user = new JZUser().setCreated(System.currentTimeMillis()).setEnabled(true).setIp(req.getRemoteAddr()).setUsername(username)
									.setUsername2(json.getString("name")).setOkAccessToken(accessToken);
						} else {
							user.setOkAccessToken(accessToken);
						}
						DBUtils.saveUser(user);
						String jwt = TokenAuthenticationService.getAuthenticationJwt(user.getUsername(), json.getString("name"));
						headers.add("Location", "/?state=" + state + "&jwt=" + jwt);

						return new ResponseEntity<String>(headers, HttpStatus.FOUND);
					} else {
						headers.add("Location", "/");
						return new ResponseEntity<String>(headers, HttpStatus.FOUND);
					}
				}
			}
		}

		return new ResponseEntity("{}", HttpStatus.OK);
	}

}