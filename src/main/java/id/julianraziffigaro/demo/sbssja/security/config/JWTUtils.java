package id.julianraziffigaro.demo.sbssja.security.config;

import id.julianraziffigaro.demo.sbssja.security.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtils {

  private final String SECRETE = "secrete keyword";

  private final String expirationTime = "60000";

  private Key key;

  @PostConstruct
  public void init() {
    this.key = Keys.hmacShaKeyFor(SECRETE.getBytes(StandardCharsets.UTF_8));
  }

  public Claims getAllClaimsFromToken(String token) {
    return Jwts.parserBuilder().setSigningKey(this.key).build().parseClaimsJws(token).getBody();
  }

  public String getUsernameFromToken(String token) {
    return getAllClaimsFromToken(token).getSubject();
  }

  public Date getExpirationDateFromToken(String token) {
    return getAllClaimsFromToken(token).getExpiration();
  }

  public String generateToken(User user) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("role", user.getAuthorities());
    return doGenerateToken(claims, user.getUsername());
  }

  public Boolean validateToken(String token) {
    return !isTokenExpired(token);
  }

  private String doGenerateToken(Map<String, Object> claims, String username) {
    long expirationTimeLong = Long.parseLong(expirationTime);
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

  private Boolean isTokenExpired(String token) {
    return getExpirationDateFromToken(token).before(new Date());
  }
}
