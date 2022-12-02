package com.dastan.rest.studentservices.security;

import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig{
	
	@Bean
	public InMemoryUserDetailsManager createUserDetailsManager() {
		UserDetails userDetailsOne = createNewUser("user1", "secret1");
		UserDetails userDetailsTwo = createNewUser("admin1", "secret1");

		return new InMemoryUserDetailsManager(userDetailsOne, userDetailsTwo);
	}
	
	private UserDetails createNewUser(String username, String password) {
		Function<String, String> passwordEncoder = input -> passwordEncoder().encode(input);
		
		return User.builder()
				.passwordEncoder(passwordEncoder)
				.username(username)
				.password(password)
				.roles("USER","ADMIN")
				.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@SuppressWarnings("deprecation")
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.httpBasic()
				.and()
				.authorizeRequests()
				.requestMatchers("/students/**").permitAll()
				.and()
				.authorizeRequests()
				.requestMatchers("/**").hasAnyRole("USER", "ADMIN")
				.anyRequest().authenticated().and().headers().frameOptions().disable();

		return http.build();
	}
	
	
}
