package com.movie.ticket.JWT;

import com.movie.ticket.Util.JwtUtil;
import com.movie.ticket.model.User;
import com.movie.ticket.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;

        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ") ){
            jwtToken = requestTokenHeader.substring(7);
            try{
                username = jwtUtil.getUsernameFromToken(jwtToken);
            }catch(IllegalArgumentException e){
                System.out.println("Unable to get JWT Token ");
            }catch(ExpiredJwtException e){
                System.out.println("JWT Token has Expired ");
            }
        }else{
            logger.warn("JWT Token does not Starts with Bearer ");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            User user = userRepository.findByIdAndSoftDeleteIsFalse(username);

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(user.getEmail());

            if(jwtUtil.validateToken(jwtToken, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = jwtUtil.getAuthenticationToken(
                        jwtToken, SecurityContextHolder.getContext().getAuthentication(), userDetails
                );

                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            }
        }
        filterChain.doFilter(request,response);
    }


}

