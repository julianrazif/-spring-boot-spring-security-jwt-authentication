package id.julianraziffigaro.demo.sbssja.security.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

public class JWTAuthenticationToken extends AbstractAuthenticationToken {

  private static final long serialVersionUID = 7892623798845693091L;

  private final String requestToken;
  private UserDetails userDetails;

  public JWTAuthenticationToken(String requestToken) {
    super(AuthorityUtils.NO_AUTHORITIES);
    this.requestToken = requestToken;
    super.setAuthenticated(true);
  }

  @Override
  public Object getCredentials() {
    return this.requestToken;
  }

  @Override
  public Object getPrincipal() {
    return this.userDetails;
  }

  @Override
  public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    Assert.isTrue(!isAuthenticated,
      "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
    super.setAuthenticated(false);
  }

  @Override
  public void eraseCredentials() {
    super.eraseCredentials();
    this.userDetails = null;
  }
}
