package id.julianraziffigaro.demo.sbssja.web.domain.model;

import java.io.Serial;

public record UserDomain(String username, String password, String role) implements BaseDomain {

  @Serial
  private static final long serialVersionUID = -5324708375648684929L;
}
