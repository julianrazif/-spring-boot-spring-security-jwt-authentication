package id.julianraziffigaro.demo.sbssja.security.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

@Component
public class JWTUtils {

  private static final String SECRETE = "this is long long long long long long long secrete keyword";

  private static final String EXPIRATION_TIME = "1800";

  private Key key;

  @PostConstruct
  public void init() {
    this.key = Keys.hmacShaKeyFor(SECRETE.getBytes(StandardCharsets.UTF_8));
  }

  public Claims getAllClaimsFromToken(String token) throws ExpiredJwtException, UnsupportedJwtException,
    MalformedJwtException, SignatureException, IllegalArgumentException {

    return Jwts.parserBuilder().setSigningKey(this.key).build().parseClaimsJws(token).getBody();
  }

  public String generateToken(UserDetails user) throws InvalidKeyException {
    Map<String, Object> claims = new HashMap<>();
    claims.put("username", user.getUsername());
    claims.put("authorities", populateAuthorities(user.getAuthorities()));

    return doGenerateToken(claims);
  }

  public Boolean validateToken(String token) throws ExpiredJwtException, UnsupportedJwtException,
    MalformedJwtException, SignatureException, IllegalArgumentException {

    return !isTokenExpired(token);
  }

  public String populateAuthorities(Iterable<? extends GrantedAuthority> collection) {
    Set<String> authoritiesSet = new HashSet<>();

    for (GrantedAuthority grantedAuthority : collection) {
      authoritiesSet.add(grantedAuthority.getAuthority());
    }

    return String.join(",", authoritiesSet);
  }

  private String doGenerateToken(Map<String, Object> claims) throws InvalidKeyException {
    var expirationTimeLong = Long.parseLong(EXPIRATION_TIME);
    final var createdDate = new Date();
    final var expirationDate = new Date(createdDate.getTime() + expirationTimeLong * 1000);

    return Jwts.builder()
      .setIssuer("Demo application")
      .setSubject("Request authorization for demo application")
      .setClaims(claims)
      .setIssuedAt(createdDate)
      .setExpiration(expirationDate)
      .signWith(key)
      .compact();
  }

  private Boolean isTokenExpired(String token) throws ExpiredJwtException, UnsupportedJwtException,
    MalformedJwtException, SignatureException, IllegalArgumentException {

    return getAllClaimsFromToken(token).getExpiration().before(new Date());
  }
}
