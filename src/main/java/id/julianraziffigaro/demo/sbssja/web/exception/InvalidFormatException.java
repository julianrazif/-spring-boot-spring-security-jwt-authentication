package id.julianraziffigaro.demo.sbssja.web.exception;

import java.io.Serial;

public class InvalidFormatException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = -3819780217007107751L;

  public InvalidFormatException(String message) {
    super(message);
  }
}
