package com.movie.ticket.interceptor;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.ticket.Util.JwtUtil;
import com.movie.ticket.decorator.Response;
import com.movie.ticket.decorator.Unauthorized;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

//import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Slf4j
@Component
public class UserInterceptor implements HandlerInterceptor {

    @Autowired
    JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object Handler) throws IOException {
        log.info("{}",request.getHeader("Authorization"));
        log.info("{}",SecurityContextHolder.getContext().getAuthentication().getAuthorities());

        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        List<String> roles = new ArrayList<String>();

        for (GrantedAuthority a : authorities) {
            roles.add(a.getAuthority());
        }
        log.info("[String] {}",roles);

        log.info("{}",request.getContextPath());

//        if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains("ADMIN"))

        if(!(roles.contains("ADMIN") || roles.contains("ROLE_ANONYMOUS"))) {
            ObjectMapper mapper = new ObjectMapper();
            Unauthorized ua = new Unauthorized();
            response.setContentType("application/text");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ua.setStatus(new Response(HttpStatus.UNAUTHORIZED, "Role Invalid", "401"));
            response.getWriter().write(mapper.writeValueAsString(ua));
            return false;
        }

        return true;

//        if(SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
//            return true;
//        }else {
//            ObjectMapper mapper = new ObjectMapper();
//            Unauthorized ua = new Unauthorized();
//            response.setContentType("application/JSON");
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            ua.setStatus(new Response(HttpStatus.UNAUTHORIZED, "Unauthorized", "401"));
//            response.getWriter().write(mapper.writeValueAsString(ua));
//            return false;
//        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        if(SecurityContextHolder.getContext().getAuthentication().isAuthenticated()){
            log.info("Authenticated");
        }else{
            log.info("Unauthenticated");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        log.info("Completed");
    }
}















