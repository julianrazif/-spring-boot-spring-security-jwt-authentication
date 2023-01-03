package id.julianraziffigaro.demo.sbssja.security.config;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;

import javax.servlet.http.HttpServletRequest;

public class JWTAuthenticationConverter implements AuthenticationConverter {

  private final AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource;

  public JWTAuthenticationConverter() {
    this(new JWTWebAuthenticationDetailsSource());
  }

  public JWTAuthenticationConverter(AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
    this.authenticationDetailsSource = authenticationDetailsSource;
  }

  @Override
  public Authentication convert(HttpServletRequest request) {
    JWTWebAuthenticationDetails details = (JWTWebAuthenticationDetails)
      this.authenticationDetailsSource.buildDetails(request);

    if (details.getRequestToken() != null) {
      String token = details.getRequestToken();
      var result = new JWTAuthenticationToken(token);
      result.setDetails(details);

      return result;
    }

    return null;
  }
}
