package edu.dosw.KAPPA_OperationSchedule_BackEnd.Config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            String username = getUsernameFromRequest(request);
            String userRole = getUserRoleFromRequest(request);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                setAuthenticationContext(request, username, userRole);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
    }

    private String getUsernameFromRequest(HttpServletRequest request) {
        return request.getHeader("X-User-Id");
    }

    private String getUserRoleFromRequest(HttpServletRequest request) {
        return request.getHeader("X-User-Role");
    }

    private void setAuthenticationContext(HttpServletRequest request, String username, String userRole) {
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + (userRole != null ? userRole : "USER"))
        );

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(username, null, authorities);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        logger.info("Usuario autenticado: " + username + " con rol: " + userRole);
    }
}