package id.julianraziffigaro.demo.sbssja.web.exception;

import java.io.Serial;

public class RegisterException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = -7118417069457639286L;

  public RegisterException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
