package com.hsd.jz.server.security;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.hsd.jz.api.consts.Consts;
import com.hsd.jz.api.db.DBUtils;
import com.hsd.jz.api.db.entity.AccessLogEntry;
import com.hsd.jz.api.db.entity.JZUser;

public class JwtAuthenticationFilter extends GenericFilterBean {

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
//		logger.debug("doFilter()");
		Authentication authentication = TokenAuthenticationService.getAuthentication((HttpServletRequest) req);
//		logger.debug("doFilter() authentication.name={}", Optional.ofNullable(authentication).map(Authentication::getName).orElse("ISNULL"));
		if (authentication != null) {
//			logger.info("time filter 01=" + System.currentTimeMillis());
			JZUser user = DBUtils.loadUser(authentication.getName());
//			logger.info("time filter 02=" + System.currentTimeMillis());
			if (user != null) {
				user.getAccessLog().add(new AccessLogEntry().setIp(req.getRemoteAddr()).setCreated(System.currentTimeMillis()));
				int fromIndex = user.getAccessLog().size() > Consts.DB_USER_ACCESS_LOG_LIMIT ? user.getAccessLog().size() - Consts.DB_USER_ACCESS_LOG_LIMIT : 0;
				user.setAccessLog(user.getAccessLog().subList(fromIndex, user.getAccessLog().size()));
//				logger.info("time filter 03=" + System.currentTimeMillis());
				DBUtils.saveUser(user);
//				logger.info("time filter 04=" + System.currentTimeMillis());
			}
		}

		SecurityContextHolder.getContext().setAuthentication(authentication);
		filterChain.doFilter(req, res);
//		logger.info("time filter 05=" + System.currentTimeMillis());
	}

}
