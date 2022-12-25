package id.julianraziffigaro.demo.sbssja.security.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

@Component("jwtAuthenticationManager")
public class JWTAuthenticationManager implements AuthenticationManager {

  private final JWTUtils jwtUtils;

  public JWTAuthenticationManager(JWTUtils jwtUtils) {
    this.jwtUtils = jwtUtils;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String token = (String) authentication.getCredentials();

    try {
      if (Boolean.FALSE.equals(jwtUtils.validateToken(token))) {
        throw new BadCredentialsException("Token expired!");
      }

      Claims claims = jwtUtils.getAllClaimsFromToken(token);
      String username = String.valueOf(claims.get("username", String.class));
      String authorities = String.valueOf(claims.get("authorities", String.class));

      UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
        username,
        token,
        AuthorityUtils.commaSeparatedStringToAuthorityList(authorities)
      );

      JWTWebAuthenticationDetails details = (JWTWebAuthenticationDetails) authentication.getDetails();
      usernamePasswordAuthenticationToken.setDetails(details);

      return usernamePasswordAuthenticationToken;
    } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException |
             IllegalArgumentException ex) {
      throw new BadCredentialsException("Invalid token", ex.getCause());
    }
  }
}
