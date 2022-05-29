package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.ArrayList;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		/*http.csrf().disable().authorizeRequests()
				.antMatchers("/admin/**").hasRole("ADMIN")
				.anyRequest().permitAll()
				.and().formLogin(Customizer.withDefaults());*/

		http
				.authorizeRequests()
				.antMatchers("/", "/home", "/signup").permitAll()
				.anyRequest().authenticated()
				.and()
				.formLogin()
				.loginPage("/login")
				.permitAll()
				.and()
				.logout()
				.permitAll();
	}

	@Bean
	@Override
	public UserDetailsService userDetailsService() {
		ArrayList<UserDetails> userDetailsList = new ArrayList<>();

		for (int i = 0; i < 5; i++) {
			userDetailsList.add(User.withDefaultPasswordEncoder()
					.username("user" + i)
					.password("pass" + i)
					.roles("USER")
					.build());
		}

		userDetailsList.add(User.withDefaultPasswordEncoder()
					.username("admin1")
					.password("adminPass")
					.roles("ADMIN", "USER")
					.build());

		return new InMemoryUserDetailsManager(userDetailsList);
	}
}