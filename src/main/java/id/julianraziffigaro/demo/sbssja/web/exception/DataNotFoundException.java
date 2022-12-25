package id.julianraziffigaro.demo.sbssja.web.exception;

public class DataNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 8637940891150391965L;

  public DataNotFoundException(String message) {
    super(message);
  }
}
