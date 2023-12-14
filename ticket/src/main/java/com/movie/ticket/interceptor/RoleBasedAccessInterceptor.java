package com.movie.ticket.interceptor;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.ticket.Util.JwtUtil;
import com.movie.ticket.Util.Utils;
import com.movie.ticket.decorator.Response;
import com.movie.ticket.decorator.Unauthorized;
import com.movie.ticket.model.RestAPIs;
import com.movie.ticket.repository.RestAPIRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


@Slf4j
@Component
public class RoleBasedAccessInterceptor implements HandlerInterceptor {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    RestAPIRepository restAPIRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object Handler) throws IOException {

        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        List<String> roles = new ArrayList<String>();
        for (GrantedAuthority a : authorities) {
            roles.add(a.getAuthority());
        }
        if (Handler instanceof HandlerMethod handlerMethod){
            String controller=handlerMethod.getBean().getClass().getSimpleName().replace("Controller", "");
            log.info("Method {}" , handlerMethod.getMethod().getName());
            String api = Utils.getApiName(handlerMethod.getMethod());
            RestAPIs restAPIs = restAPIRepository.findByName(api);
            if (controller.equals("admin") && roles.stream().anyMatch(restAPIs.getRoles()::contains))
                return true;
            else if (controller.equals("user") && roles.stream().anyMatch(restAPIs.getRoles()::contains)) {
              return true;
            } 
          /*  if (controller.equals("admin") && roles.contains("ADMIN"))
                return true;
            else if (controller.equals("user") && roles.contains("USER"))
                return true;*/
        }
        ObjectMapper mapper = new ObjectMapper();
        Unauthorized ua = new Unauthorized();
        response.setContentType("application/text");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ua.setStatus(new Response(HttpStatus.UNAUTHORIZED, "Role Invalid", "401"));
        response.getWriter().write(mapper.writeValueAsString(ua));

        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {



    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        log.info("Completed");
    }
}















