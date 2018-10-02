package com.eniso.acmwebservice.Security;

import com.eniso.acmwebservice.Dao.AcmerRepository;
import com.eniso.acmwebservice.Entity.Acmer;
import com.eniso.acmwebservice.Entity.Role;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Component
public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds; // 24h

    @Autowired
    private AcmerRepository acmerRepository;

    public String createToken(String username, List<Role> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("auth", roles.stream().map(s -> new SimpleGrantedAuthority(s.getAuthority()))
                .collect(Collectors.toList()));

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        String encodedSecretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());

        return Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, encodedSecretKey).compact();
    }

    public Optional<Jws<Claims>> getClaims(Optional<String> token) {
        if (!token.isPresent()) {
            return Optional.empty();
        }
        String encodedSecretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        return Optional.of(Jwts.parser().setSigningKey(encodedSecretKey).parseClaimsJws(token.get()));
    }


    public Authentication getAuthentication(String token) {
        Acmer user = acmerRepository.findByHandle(getUsername(token));
        return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
    }

    public String getUsername(String token) {
        String encodedSecretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        return Jwts.parser().setSigningKey(encodedSecretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    public boolean validateToken(String token) {
        String encodedSecretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        try {
            Jwts.parser().setSigningKey(encodedSecretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Expired or invalid JWT token");
        }
    }

}
