package id.julianraziffigaro.demo.sbssja.security.config;

import lombok.EqualsAndHashCode;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.util.Assert;

import java.io.Serial;

@EqualsAndHashCode(callSuper = false)
public class JWTAuthenticationToken extends AbstractAuthenticationToken {

  @Serial
  private static final long serialVersionUID = 7892623798845693091L;

  private String requestToken;

  public JWTAuthenticationToken(String requestToken) {
    super(AuthorityUtils.NO_AUTHORITIES);
    this.requestToken = requestToken;
    super.setAuthenticated(false);
  }

  @Override
  public Object getCredentials() {
    return this.requestToken;
  }

  @Override
  public Object getPrincipal() {
    return this.requestToken;
  }

  @Override
  public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    Assert.isTrue(
      !isAuthenticated,
      "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead"
    );
    super.setAuthenticated(false);
  }

  @Override
  public void eraseCredentials() {
    super.eraseCredentials();
    this.requestToken = null;
  }
}
