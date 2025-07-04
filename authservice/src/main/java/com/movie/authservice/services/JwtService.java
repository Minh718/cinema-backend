package com.movie.authservice.services;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.movie.authservice.entities.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtService {

    @NonFinal
    @Value("${jwt.secret}")
    String KEY_TOKEN;

    @NonFinal
    @Value("${jwt.expiration}")
    String EXPIRATION;

    // public String extractId(String token) {
    // try {

    // int i = token.lastIndexOf('.');
    // String withoutSignature = token.substring(0, i + 1);
    // Jwt<Header, Claims> untrusted =
    // Jwts.parserBuilder().build().parseClaimsJwt(withoutSignature);
    // return untrusted.getBody().getSubject();
    // } catch (MalformedJwtException e) {
    // throw new CustomException(ErrorCode.INVALID_TOKEN);
    // }
    // }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(User user) {
        return buildToken(new HashMap<>(), user);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("scope", buildScope(user));
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .addClaims(claims)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token) {
        // String id = extractIdUser(token, key);
        return !isTokenExpired(token);
    }

    public String extractIdUser(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(KEY_TOKEN);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add(role.getName());
            });

        return stringJoiner.toString();
    }
}
