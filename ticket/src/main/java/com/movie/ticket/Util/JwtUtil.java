package com.movie.ticket.Util;

import com.movie.ticket.JWT.JwtUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.Serial;
import java.io.Serializable;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtUtil implements Serializable {

    @Autowired
    JwtUser jwtUser;

    @Serial
    private static final long serialVersionUID = -2550185165626007488L;

    public static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60;


    @Value("${spring.ticket.key}")
    private String key;
    //= Keys.secretKeyFor(SignatureAlgorithm.HS512);

    Key keys = Keys.secretKeyFor(SignatureAlgorithm.HS512);


    /* Retrieve UserName from the Token */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public String getIdFromToken(String token) {
        return getClaimFromToken(token, Claims::getId);
    }


    /* Retrieve Date from the Token */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = jwtUser.getCliams(token);
        //getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //generate token for user
    public String generateToken(JwtUser jwtUser, String id) {
        return doGenerateToken(jwtUser, id);
    }

    // set claims, set subject of a claim, set issued time, set expiration time, define the algorithm
    private String doGenerateToken(JwtUser jwtUser, String id) {
        log.info("secret key :{}", key);

        return Jwts.builder()
                .setClaims(jwtUser.toClaims())
                .setSubject(jwtUser.getId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, getBase64(key))
                .compact();
    }

    private String getBase64(String key) {
        //SecretKey keyValue = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        //log.info("key value :{}", keyValue.toString());
        byte[] secret = Base64.getEncoder().encode(key.getBytes());
//        log.info("log Scret key bytes {}", new String(secret));
        return new String(secret);
    }

    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public UsernamePasswordAuthenticationToken getAuthenticationToken(final String token, final Authentication existingAuth, final UserDetails userDetails) {

        final JwtParser jwtParserBuilder = Jwts.parser().setSigningKey(getBase64(key));

        final Jws<Claims> claimsJws = jwtParserBuilder.parseClaimsJws(token);

        final Claims claims = claimsJws.getBody();

        final Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("ROLE").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

}
