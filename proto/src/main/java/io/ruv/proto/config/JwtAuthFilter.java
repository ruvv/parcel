package io.ruv.proto.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//@Component
//@RequiredArgsConstructor
//public class JwtAuthFilter extends OncePerRequestFilter {
//
//    private final JwtService jwtService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        var header = request.getHeader("Authorization");
//
//
//        if (header == null || !header.startsWith("Bearer ")) {
//
//            filterChain.doFilter(request, response);
//            return;
//        } else {
//
//            var jwt = header.substring(7);
////            var username = jwtService.extractUsername(jwt);
//
//        }
//    }
//}
