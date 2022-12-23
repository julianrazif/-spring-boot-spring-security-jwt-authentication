package id.julianraziffigaro.demo.sbssja.security.config;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;

public class JWTAuthenticationConverter implements AuthenticationConverter {

  private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource;

  private final JWTUtils jwtUtils;

  public JWTAuthenticationConverter(JWTUtils jwtUtils) {
    this(new JWTWebAuthenticationDetailsSource(), jwtUtils);
  }

  public JWTAuthenticationConverter(AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource, JWTUtils jwtUtils) {
    this.authenticationDetailsSource = authenticationDetailsSource;
    this.jwtUtils = jwtUtils;
  }

  public AuthenticationDetailsSource<HttpServletRequest, ?> getAuthenticationDetailsSource() {
    return this.authenticationDetailsSource;
  }

  public void setAuthenticationDetailsSource(AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
    Assert.notNull(authenticationDetailsSource, "AuthenticationDetailsSource required");
    this.authenticationDetailsSource = authenticationDetailsSource;
  }
  @Override
  public Authentication convert(HttpServletRequest request) {
    try {
      JWTWebAuthenticationDetails details = (JWTWebAuthenticationDetails) this.authenticationDetailsSource.buildDetails(request);

      if (details.getRequestToken() != null) {

        String token = details.getRequestToken();

        if (jwtUtils.validateToken(token)) {
          JWTAuthenticationToken result = new JWTAuthenticationToken(token);
          result.setDetails(details);
          return result;
        }
      }

      return null;
    } catch (Exception ex) {
      throw new BadCredentialsException("Invalid token", ex.getCause());
    }
  }
}
