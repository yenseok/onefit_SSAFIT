package com.ssafy.mvc.filter;

import com.ssafy.mvc.model.dto.User;
import com.ssafy.mvc.model.service.UserService;
import com.ssafy.mvc.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserService userService;

    public JwtAuthenticationFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                if (JwtUtil.validateToken(token)) {
                    Integer userId = JwtUtil.getUserId(token);
                    String nickName = JwtUtil.getNickName(token);
                    String role = JwtUtil.getRole(token);

                    User user = userService.getUserProfile(userId);  // 사용자 객체 가져오기

                    if (user != null && role != null) {
                        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(user, null, List.of(authority));

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        request.setAttribute("userId", userId);
                        request.setAttribute("nickName", nickName);
                    } else {
                        System.out.println("역할 또는 사용자 정보 없음");
                    }
                }
            } catch (JwtException e) {
                System.out.println("JWT 인증 실패: " + e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired JWT");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
