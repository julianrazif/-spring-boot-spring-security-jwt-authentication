package id.julianraziffigaro.demo.sbssja.security.service;

import org.springframework.security.crypto.password.PasswordEncoder;

public class PBKDF2Encoder implements PasswordEncoder {

  private final PasswordService passwordService;

  public PBKDF2Encoder(PasswordService passwordService) {
    this.passwordService = passwordService;
  }

  @Override
  public String encode(CharSequence cs) {
    return passwordService.encode(cs.toString());
  }

  @Override
  public boolean matches(CharSequence cs, String string) {
    return encode(cs).equals(string);
  }
}
