package id.julianraziffigaro.demo.sbssja.web.domain.model;

import lombok.Data;

@Data
public class AuthenticationDomain implements BaseDomain {

  private static final long serialVersionUID = -9155277202862022612L;

  private final String token;
}
