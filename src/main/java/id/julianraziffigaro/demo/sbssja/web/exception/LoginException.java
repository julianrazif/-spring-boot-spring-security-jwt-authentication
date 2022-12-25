package id.julianraziffigaro.demo.sbssja.web.exception;

import java.io.Serial;

public class LoginException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = -4816858732046285934L;

  public LoginException(String message, Throwable throwable) {
    super(message, throwable);
  }

  public LoginException(Throwable throwable) {
    super(throwable);
  }
}
