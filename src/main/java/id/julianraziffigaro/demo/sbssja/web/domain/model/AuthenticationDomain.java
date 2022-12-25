package id.julianraziffigaro.demo.sbssja.web.domain.model;

import java.io.Serial;

public record AuthenticationDomain(String token) implements BaseDomain {

  @Serial
  private static final long serialVersionUID = -9155277202862022612L;
}
