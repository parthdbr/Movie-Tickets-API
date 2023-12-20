package com.movie.ticket.JWT;

import com.movie.ticket.model.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Component
public class JwtUser {

    @Autowired
    private Environment env;
    @Value("${spring.ticket.key}")
    private String key;

    public JwtUser(String name, String seesion_id, String authorities) {
        this.id = name;
        this.seesion_id = seesion_id;
        this.roleList = authorities;
    }


    private enum names {
        ID , SESSION_ID, ROLE
    }

    String id;
    String seesion_id;
    String roleList;


    public Map<String, String> toClaims() {

        Map<String, String> claims = new HashMap<>();
        claims.put(names.ID.toString(), id);
        claims.put(names.SESSION_ID.toString(), seesion_id);
        claims.put(names.ROLE.toString(), roleList);
        return claims;

    }

    /*
    public JwtUser formClaims(Claims claims) {
    }
*/



}
