package id.julianraziffigaro.demo.sbssja.web.exception;

import java.io.Serial;

public class DataNotFoundException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 8637940891150391965L;

  public DataNotFoundException(String message) {
    super(message);
  }
}
