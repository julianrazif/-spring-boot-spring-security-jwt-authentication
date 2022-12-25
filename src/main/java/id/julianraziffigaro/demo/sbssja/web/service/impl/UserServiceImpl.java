package id.julianraziffigaro.demo.sbssja.web.service.impl;

import id.julianraziffigaro.demo.sbssja.security.service.PBKDF2Encoder;
import id.julianraziffigaro.demo.sbssja.web.domain.model.UserDomain;
import id.julianraziffigaro.demo.sbssja.web.exception.DataNotFoundException;
import id.julianraziffigaro.demo.sbssja.web.exception.InvalidFormatException;
import id.julianraziffigaro.demo.sbssja.web.exception.LoginException;
import id.julianraziffigaro.demo.sbssja.web.repository.impl.UserRepositoryImpl;
import id.julianraziffigaro.demo.sbssja.web.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepositoryImpl userRepository;

  private final PBKDF2Encoder pbkdf2Encoder;

  public UserServiceImpl(UserRepositoryImpl userRepository,
                         PBKDF2Encoder pbkdf2Encoder) {
    this.userRepository = userRepository;
    this.pbkdf2Encoder = pbkdf2Encoder;
  }

  @Override
  public UserDetails login(UserDomain userDomain) throws LoginException {
    try {
      String username = userDomain.getUsername();
      String password = userDomain.getPassword();

      UserDetails user = userRepository.findByUsername(username);

      if (!pbkdf2Encoder.matches(password, user.getPassword())) {
        throw new LoginException(new DataNotFoundException("Wrong username or password!"));
      }

      return user;
    } catch (DataNotFoundException | InvalidFormatException ex) {
      throw new LoginException(ex.getMessage(), ex);
    }
  }
}
