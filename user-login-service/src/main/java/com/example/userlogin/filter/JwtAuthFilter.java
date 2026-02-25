package com.example.userlogin.filter;

import com.example.userlogin.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * JWT 鉴权过滤器 - 从 Cookie 读取 token 并校验
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String REQUEST_ATTR_USER_ID = "userId";
    private static final List<String> PUBLIC_PATHS = Arrays.asList(
            "/api/login",
            "/api/register"
    );

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${jwt.cookie-name}")
    private String cookieName;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        if (!path.startsWith("/api")) {
            filterChain.doFilter(request, response);
            return;
        }
        if (isPublicPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getTokenFromCookie(request);
        Claims claims = jwtUtil.parseToken(token);

        if (claims == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\":\"未登录或登录已过期\"}");
            return;
        }

        Long userId = jwtUtil.getUserId(claims);
        request.setAttribute(REQUEST_ATTR_USER_ID, userId);
        filterChain.doFilter(request, response);
    }

    private boolean isPublicPath(String path) {
        for (String publicPath : PUBLIC_PATHS) {
            if (path.equals(publicPath) || path.startsWith(publicPath + "/")) {
                return true;
            }
        }
        if (path.equals("/api/users/login")) {
            return true;
        }
        return false;
    }

    private String getTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
