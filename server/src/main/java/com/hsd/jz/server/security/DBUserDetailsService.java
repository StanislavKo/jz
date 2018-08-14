package com.hsd.jz.server.security;

import java.util.Collection;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hsd.jz.api.consts.Consts;
import com.hsd.jz.api.db.DBUtils;
import com.hsd.jz.api.db.entity.JZUser;

@Service
public class DBUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		JZUser user = DBUtils.loadUser(username);
		if (user == null) {
			throw new UsernameNotFoundException(String.format("User %s does not exist!", username));
		}
		UserDetails userDetails = User.withDefaultPasswordEncoder().username(username).password(user.getPasswordHash()).credentialsExpired(false)
				.accountExpired(false).accountLocked(false).disabled(!user.isEnabled()).authorities(Consts.ROLE_USER).build();

		return userDetails;
	}

}