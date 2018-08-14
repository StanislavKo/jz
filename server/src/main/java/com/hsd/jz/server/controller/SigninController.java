package com.hsd.jz.server.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.hsd.jz.api.consts.Consts;
import com.hsd.jz.api.db.DBUtils;
import com.hsd.jz.api.db.entity.JZUser;
import com.hsd.jz.api.utils.PrimitiveUtils;
import com.hsd.jz.server.consts.ConstsServer;
import com.hsd.jz.server.controller.pojo.GenericResponse;
import com.hsd.jz.server.security.TokenAuthenticationService;
import com.hsd.jz.server.security.pojo.UidCredentials;
import com.hsd.jz.server.security.pojo.UsernamePasswordCredentials;

@RestController
public class SigninController {

	private static final Logger logger = LoggerFactory.getLogger(SigninController.class);

	//	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/signup", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public GenericResponse signup(@RequestBody UsernamePasswordCredentials userParam, HttpServletRequest req, HttpServletResponse res) {
		logger.info("signup(), username={}", userParam.getUsername());

		JZUser user = DBUtils.loadUser(Consts.USER_USERPASS_PREFIX + userParam.getUsername());
		if (user != null) {
			return new GenericResponse(false, "busy");
		} else {
			String password = userParam.getPassword();
			String salt = RandomStringUtils.randomAlphanumeric(32);
			String passwordMd5 = PrimitiveUtils.getMd5(salt + password);
			user = new JZUser().setCreated(System.currentTimeMillis()).setEnabled(true).setIp(req.getRemoteAddr())
					.setUsername(Consts.USER_USERPASS_PREFIX + userParam.getUsername()).setPasswordSalt(salt).setPasswordHash(passwordMd5);
			DBUtils.saveUser(user);
			TokenAuthenticationService.addAuthentication(res, user.getUsername());
		}

		return new GenericResponse(true, null);
	}

	@RequestMapping(value = "/android", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public GenericResponse signupAndroid(@RequestBody UidCredentials uidParam, HttpServletRequest req, HttpServletResponse res) throws IOException {
		logger.info("signupAndroid(), uid={}", uidParam.getUid());

		JZUser user = DBUtils.loadUserByFirebaseUid(uidParam.getUid());
		if (user != null) {
			TokenAuthenticationService.addAuthentication(res, user.getUsername());
		} else {
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(new ByteArrayInputStream(ConstsServer.FIREBASE_SECRET.getBytes()))).build();
			FirebaseApp defaultApp;
			try {
				defaultApp = FirebaseApp.initializeApp(options);
			} catch (Exception ex) {
				defaultApp = FirebaseApp.getInstance();
			}
			FirebaseAuth defaultAuth = FirebaseAuth.getInstance(defaultApp);

			try {
				UserRecord fbUser = defaultAuth.getUser(uidParam.getUid());

				String username = Consts.USER_FIREBASE_PREFIX + RandomStringUtils.randomAlphanumeric(16);
				user = new JZUser().setCreated(System.currentTimeMillis()).setEnabled(true).setIp(req.getRemoteAddr()).setUsername(username)
						.setFirebaseUid(fbUser.getUid());
				DBUtils.saveUser(user);
				TokenAuthenticationService.addAuthentication(res, user.getUsername());
			} catch (FirebaseAuthException e) {
				e.printStackTrace();
				return new GenericResponse(false, null);
			}
		}

		return new GenericResponse(true, null);
	}

}