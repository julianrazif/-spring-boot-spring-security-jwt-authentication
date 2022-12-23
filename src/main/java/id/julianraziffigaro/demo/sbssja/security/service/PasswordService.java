package id.julianraziffigaro.demo.sbssja.security.service;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service("passwordService")
public class PasswordService {

  private final String SECRETE = "this is long long long long long long long secrete keyword";

  private final Integer iteration = 33;

  private final Integer keyLength = 256;

  private final String SALT = "this is long long long long long long long secrete salt";

  private static final byte[] ivBytes = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};

  private SecretKeySpec secretKeySpec;

  private Cipher cipher;

  @PostConstruct
  private void init() {
    try {
      SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
      PBEKeySpec pbeKeySpec = new PBEKeySpec(SECRETE.toCharArray(), SALT.getBytes(), iteration, keyLength);
      SecretKey secretKey = secretKeyFactory.generateSecret(pbeKeySpec);

      secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
      cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  public String encode(String plainText) {
    try {
      cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(ivBytes));
      byte[] encryptedTextBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(encryptedTextBytes);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  public String decode(String encryptedText) {
    try {
      cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(ivBytes));
      return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedText)));
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }
}
