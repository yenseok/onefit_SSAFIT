package com.ssafy.mvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.ssafy.mvc.filter.JwtAuthenticationFilter;
import com.ssafy.mvc.model.service.UserService;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@EnableMethodSecurity
@SecurityScheme(name = "jwtAuth", // 이 이름을 나중에 @Operation에서 참조
		type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, UserService userService) throws Exception {
	    http.csrf(csrf -> csrf.disable())
	        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers("/upload/profile/**", "/upload/board/**", "/upload/equipment/**").permitAll()
	            .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html",
	                    "/swagger-resources/**", "/webjars/**").permitAll()
	            .requestMatchers("/auth/**", "/board/**", "/gyms/**", "/trainers/**").permitAll()
	            .requestMatchers("/boards/latest", "/boards/popular", "/boards/search",
	                    "/boards/{boardId:[\\d]+}", "/boards/{boardId:[\\d]+}/comment-count",
	                    "/boards/{boardId:[\\d]+}/like", "/comments/board/**").permitAll()
	            .requestMatchers("/boards/**", "/comments/**").hasRole("USER")
	            .requestMatchers("/users/**", "/reservations/**", "/pt/**").authenticated()
	            .anyRequest().authenticated())
	        .addFilterBefore(new JwtAuthenticationFilter(userService), UsernamePasswordAuthenticationFilter.class)
	        .formLogin(form -> form.disable())
	        .httpBasic(httpBasic -> httpBasic.disable());

	    return http.build();
	}


	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.addAllowedOriginPattern("*");
		config.addAllowedMethod("*");
		config.addAllowedHeader("*");
		config.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);

		return (CorsConfigurationSource) source; // 타입 문제 없음
	}

}
