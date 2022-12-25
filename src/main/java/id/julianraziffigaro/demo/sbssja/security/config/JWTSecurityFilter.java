package id.julianraziffigaro.demo.sbssja.security.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JWTSecurityFilter extends OncePerRequestFilter {

  private final AuthenticationManager authenticationManager;

  private final AuthenticationEntryPoint authenticationEntryPoint;

  private final JWTAuthenticationConverter authenticationConverter;

  private static final boolean IGNORE_FAILURE = false;

  public JWTSecurityFilter(AuthenticationManager authenticationManager,
                           AuthenticationEntryPoint authenticationEntryPoint) {
    Assert.notNull(authenticationManager, "authentication manager can not be null");
    Assert.notNull(authenticationEntryPoint, "authenticationEntryPoint can not be null");

    this.authenticationManager = authenticationManager;
    this.authenticationEntryPoint = authenticationEntryPoint;
    authenticationConverter = new JWTAuthenticationConverter();
  }

  @Override
  public void afterPropertiesSet() {
    Assert.notNull(this.authenticationManager, "An AuthenticationManager is required");

    if (!isIgnoreFailure()) {
      Assert.notNull(this.authenticationEntryPoint, "An AuthenticationEntryPoint is required");
    }
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    try {
      JWTAuthenticationToken authRequest = (JWTAuthenticationToken) this.authenticationConverter.convert(request);

      if (authRequest == null) {
        log.info("Did not process authentication request since failed");

        filterChain.doFilter(request, response);

        return;
      }

      String token = (String) authRequest.getCredentials();

      log.info(String.format("Found token '%s' in authorization header", token));

      Authentication authResult = this.authenticationManager.authenticate(authRequest);
      SecurityContext context = SecurityContextHolder.createEmptyContext();
      context.setAuthentication(authResult);
      SecurityContextHolder.setContext(context);

      log.info(String.format("Set SecurityContextHolder to %s", authResult));

      onSuccessfulAuthentication();
    } catch (AuthenticationException ex) {
      SecurityContextHolder.clearContext();
      log.info("Failed to process authentication request", ex);

      onUnsuccessfulAuthentication();

      if (IGNORE_FAILURE) {
        filterChain.doFilter(request, response);
      } else {
        this.authenticationEntryPoint.commence(request, response, ex);
      }

      return;
    }

    filterChain.doFilter(request, response);
  }

  protected void onSuccessfulAuthentication() {
    log.info("token berhasil di verifikasi");
  }

  protected void onUnsuccessfulAuthentication() {
    log.info("token gagal di verifikasi");
  }

  protected boolean isIgnoreFailure() {
    return IGNORE_FAILURE;
  }
}
