package id.julianraziffigaro.demo.sbssja.security.config;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

public class JWTAuthenticationManager implements AuthenticationManager {

  private final JWTUtils jwtUtils;

  public JWTAuthenticationManager(JWTUtils jwtUtils) {
    this.jwtUtils = jwtUtils;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String token = (String) authentication.getCredentials();

    if (token == null) {
      throw new UsernameNotFoundException("401 Unauthorized");
    }

    Claims claims = jwtUtils.getAllClaimsFromToken(token);
    String username = claims.getSubject();

    List<String> rolesMap = claims.get("role", List.class);
    return new UsernamePasswordAuthenticationToken(username, null, rolesMap.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
  }
}
