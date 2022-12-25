package id.julianraziffigaro.demo.sbssja.web.exception;

public class LoginException extends RuntimeException {

  private static final long serialVersionUID = -4816858732046285934L;

  public LoginException(String message, Throwable throwable) {
    super(message, throwable);
  }

  public LoginException(Throwable throwable) {
    super(throwable);
  }
}
