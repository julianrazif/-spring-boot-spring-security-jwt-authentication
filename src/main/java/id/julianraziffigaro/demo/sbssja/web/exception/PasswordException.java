package id.julianraziffigaro.demo.sbssja.web.exception;

import java.io.Serial;

public class PasswordException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = -1355210308228591005L;

  public PasswordException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
