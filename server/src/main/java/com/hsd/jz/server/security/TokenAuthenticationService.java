package com.hsd.jz.server.security;

import static java.util.Collections.emptyList;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenAuthenticationService {

	private static final String SECRET = System.getenv("JZ_JWT_SIGNING_KEY");

	//	static final long EXPIRATIONTIME = 864_000_000; // 10 days
	private static final long EXPIRATIONTIME = 2_592_000_000l; // 30 days
	private static final String TOKEN_PREFIX = "Bearer";
	private static final String HEADER_STRING = "Authorization";
	private static final String HEADER_OUT_STRING = "Jwt";

	public static void addAuthentication(HttpServletResponse res, String username) {
		try {
			String JWT = Jwts.builder().setSubject(URLEncoder.encode(username, "UTF-8")).setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
					.signWith(SignatureAlgorithm.HS512, SECRET).compact();
			res.addHeader(HEADER_OUT_STRING, TOKEN_PREFIX + " " + JWT);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static String getAuthenticationJwt(String username, String name) {
		String JWT = "asdf";
		try {
			JwtBuilder builder = Jwts.builder();
			if (name != null) {
				Map<String, Object> claims = new HashMap();
				claims.put("name", URLEncoder.encode(name, "UTF-8"));
				builder.setClaims(claims);
			}
			builder.setSubject(URLEncoder.encode(username, "UTF-8"));
			builder.setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME));
			JWT = builder.signWith(SignatureAlgorithm.HS512, SECRET).compact();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return JWT;
	}

	static Authentication getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(HEADER_STRING);
		if (token != null) {
			try {
				// parse the token.
				String user = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody().getSubject();
				user = URLDecoder.decode(user, "UTF-8");
				return user != null ? new UsernamePasswordAuthenticationToken(user, null, emptyList()) : null;
			} catch (Exception ignored) {
			}
		}
		return null;
	}
}