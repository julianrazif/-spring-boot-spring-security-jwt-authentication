package id.julianraziffigaro.demo.sbssja.web.exception;

import java.io.Serial;

public class EditException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 2749868236979777224L;

  public EditException(String message, Throwable throwable) {
    super(message, throwable);
  }

  public EditException(Throwable throwable) {
    super(throwable);
  }
}
