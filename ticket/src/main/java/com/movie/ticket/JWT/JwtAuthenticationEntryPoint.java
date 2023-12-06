package com.movie.ticket.JWT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.ticket.decorator.Response;
import com.movie.ticket.decorator.Unauthorized;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ObjectMapper mapper = new ObjectMapper();
        Unauthorized unauthorized = new Unauthorized();

        response.setContentType("application/JSON");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        unauthorized.setStatus(new Response(HttpStatus.UNAUTHORIZED, "Unauthorized", "401"));
        response.getWriter().write(mapper.writeValueAsString(unauthorized));
    }


}
