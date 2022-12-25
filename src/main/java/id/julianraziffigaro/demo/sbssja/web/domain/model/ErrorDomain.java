package id.julianraziffigaro.demo.sbssja.web.domain.model;

import lombok.Data;

@Data
public class ErrorDomain implements BaseDomain {

  private static final long serialVersionUID = -5320168670067301708L;

  private final Integer errorCode;
  private final String message;
}
