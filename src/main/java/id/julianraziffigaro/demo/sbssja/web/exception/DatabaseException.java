package id.julianraziffigaro.demo.sbssja.web.exception;

import java.io.Serial;

public class DatabaseException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = -4948816199367940197L;

  public DatabaseException(String message) {
    super(message);
  }
}
