package com.hsd.jz.server.security;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hsd.jz.api.consts.Consts;
import com.hsd.jz.api.db.DBUtils;
import com.hsd.jz.api.db.entity.JZUser;
import com.hsd.jz.api.utils.PrimitiveUtils;
import com.hsd.jz.server.security.pojo.UsernamePasswordCredentials;

public class JwtSigninFilter extends AbstractAuthenticationProcessingFilter {

	private static final Logger logger = LoggerFactory.getLogger(JwtSigninFilter.class);

	public JwtSigninFilter(String url, AuthenticationManager authManager) {
		super(new AntPathRequestMatcher(url));
		setAuthenticationManager(authManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException, IOException, ServletException {
		logger.debug("attemptAuthentication()");
		UsernamePasswordCredentials creds = new ObjectMapper().readValue(req.getInputStream(), UsernamePasswordCredentials.class);
		logger.debug("attemptAuthentication() username={}", creds.getUsername());

		String passwordHash = "";
		JZUser user = DBUtils.loadUser(Consts.USER_USERPASS_PREFIX + creds.getUsername());
		if (user != null) {
			String salt = user.getPasswordSalt();
			passwordHash = PrimitiveUtils.getMd5(salt + creds.getPassword());
		}

		return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(Consts.USER_USERPASS_PREFIX + creds.getUsername(), passwordHash,
				Collections.singletonList(new SimpleGrantedAuthority(Consts.ROLE_USER))));
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth)
			throws IOException, ServletException {
		logger.debug("successfulAuthentication() username={}", auth.getName());
		TokenAuthenticationService.addAuthentication(res, auth.getName());
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
			throws IOException, ServletException {
		logger.debug("unsuccessfulAuthentication() message={}", failed.getMessage());
		super.unsuccessfulAuthentication(request, response, failed);
	}

}
