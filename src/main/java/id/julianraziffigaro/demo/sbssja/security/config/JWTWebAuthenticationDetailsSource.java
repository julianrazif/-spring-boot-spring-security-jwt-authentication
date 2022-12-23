package id.julianraziffigaro.demo.sbssja.security.config;

import org.springframework.security.authentication.AuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;

public class JWTWebAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, JWTWebAuthenticationDetails> {

  @Override
  public JWTWebAuthenticationDetails buildDetails(HttpServletRequest context) {
    return new JWTWebAuthenticationDetails(context);
  }
}
