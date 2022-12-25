package id.julianraziffigaro.demo.sbssja.security.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtils {

  private static final String SECRETE = "this is long long long long long long long secrete keyword";

  private static final String EXPIRATION_TIME = "1800";

  private Key key;

  @PostConstruct
  public void init() {
    this.key = Keys.hmacShaKeyFor(SECRETE.getBytes());
  }

  public Claims getAllClaimsFromToken(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
    return Jwts.parserBuilder().setSigningKey(this.key).build().parseClaimsJws(token).getBody();
  }

  public String generateToken(UserDetails user) throws InvalidKeyException {
    Map<String, Object> claims = new HashMap<>();
    claims.put("role", user.getAuthorities());
    return doGenerateToken(claims, user.getUsername());
  }

  public Boolean validateToken(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
    return !isTokenExpired(token);
  }

  private String doGenerateToken(Map<String, Object> claims, String username) throws InvalidKeyException {
    long expirationTimeLong = Long.parseLong(EXPIRATION_TIME);
    final Date createdDate = new Date();
    final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong * 1000);

    return Jwts.builder()
      .setClaims(claims)
      .setSubject(username)
      .setIssuedAt(createdDate)
      .setExpiration(expirationDate)
      .signWith(key)
      .compact();
  }

  private Boolean isTokenExpired(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
    return getAllClaimsFromToken(token).getExpiration().before(new Date());
  }
}
