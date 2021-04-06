package com.recipepicker.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@EnableWebSecurity
@Configuration
public class BasicAuthConfig extends WebSecurityConfigurerAdapter{
	@Value("${spring.security.user.name}")
	private String username;

	@Value("${spring.security.user.password}")
	private char[] password;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.inMemoryAuthentication()
		.withUser(username)
		.password(passwordEncoder().encode(new String(password)))
		.roles("USER");
		System.out.println("Configure3");

	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		  http.authorizeRequests().anyRequest().authenticated();
		  http.httpBasic();
		  http.csrf().disable();
	}
}
//	
//	private static final List<String> AUTH_LIST = Arrays.asList(
//            "/swagger-resources/**",
//            "/swagger-ui.html**",
//            "/webjars/**",
//            "favicon.ico");
//	
//	@Value("${spring.security.user.name}")
//	private String username;
//
//	@Value("${spring.security.user.password}")
//	private char[] password;
//	
//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//	
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
//		auth.inMemoryAuthentication()
//		.withUser(username)
//		.password(passwordEncoder().encode(new String(password)))
//		.roles("USER");
//		System.out.println("Configure3");
//
//	}
//	
//	@Override
//	protected void configure(HttpSecurity http) throws Exception{
//		  http.authorizeRequests()
//          .antMatchers("/swagger-resources/**").permitAll()
//          .antMatchers("/swagger-ui.html**").permitAll()
//          .antMatchers("/webjars/**").permitAll()
//          .antMatchers("favicon.ico").permitAll()
//          .antMatchers("/**/*").denyAll();
//		http.httpBasic();
//		System.out.println("Configure");
//		http.csrf().disable();
//        http.cors().and().csrf().disable();
//        http
//        .csrf().disable()
//        .authorizeRequests()
//            .anyRequest().permitAll();
//	}
//	
//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("*"));
//        configuration.setAllowedMethods(Arrays.asList("*"));
//        configuration.setAllowedHeaders(Arrays.asList("*"));
//        configuration.setAllowCredentials(true);
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//		System.out.println("Configure2");
//        return source;
//    }
//	
//
//	}*/
