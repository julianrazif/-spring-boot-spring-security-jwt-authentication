package id.julianraziffigaro.demo.sbssja.web.domain.model;

import lombok.Data;

@Data
public class UserDomain implements BaseDomain {

  private static final long serialVersionUID = -5324708375648684929L;

  private final String username;
  private final String password;
}
