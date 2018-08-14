package com.hsd.jz.server.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.hsd.jz.server.security.DBUserDetailsService;
import com.hsd.jz.server.security.JwtAuthenticationFilter;
import com.hsd.jz.server.security.JwtSigninFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	//	@Bean
	//	public JwtAuthenticationFilter jwtAuthenticationTokenFilter() throws Exception {
	//		return new JwtAuthenticationFilter("/**");
	//	}

	@Autowired
	private DBUserDetailsService userDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//		http.addFilterBefore(jwtAuthenticationTokenFilter(), BasicAuthenticationFilter.class);
		//		http.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		http.cors().and().csrf().disable().authorizeRequests()
				// 1
				.antMatchers("/").permitAll()
				// 2
				.antMatchers(HttpMethod.POST, "/signin").permitAll()
				.antMatchers(HttpMethod.POST, "/android").permitAll()
				// 3
//				.anyRequest().authenticated()
				// 
				.and()
				// We filter the api/login requests
				.addFilterBefore(new JwtSigninFilter("/signin", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
				// And filter other requests to check the presence of JWT in header
				.addFilterBefore(new JwtAuthenticationFilter(), BasicAuthenticationFilter.class);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Jwt"));
		configuration.setExposedHeaders(Arrays.asList("Jwt"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
//		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}

}
