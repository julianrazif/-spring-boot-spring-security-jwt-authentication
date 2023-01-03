package id.julianraziffigaro.demo.sbssja.web.service.impl;

import id.julianraziffigaro.demo.sbssja.security.service.PBKDF2Encoder;
import id.julianraziffigaro.demo.sbssja.web.domain.model.Action;
import id.julianraziffigaro.demo.sbssja.web.domain.model.UserDomain;
import id.julianraziffigaro.demo.sbssja.web.exception.*;
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
  public UserDetails login(UserDomain userDomain) {
    try {
      String username = userDomain.username();
      String password = userDomain.password();

      UserDetails user = userRepository.findByUsername(username);

      if (!pbkdf2Encoder.matches(password, user.getPassword())) {
        throw new LoginException(new DataNotFoundException("Wrong username or password!"));
      }

      return user;
    } catch (DataNotFoundException | InvalidFormatException ex) {
      throw new LoginException(ex.getMessage(), ex);
    }
  }

  @Override
  public UserDetails saveOrUpdate(Action action, UserDomain userDomain) {
    String username = userDomain.username();
    String password = pbkdf2Encoder.encode(userDomain.password());
    String role = userDomain.role();

    if (action == Action.getByName("ADD")) {
      try {
        return userRepository.save(new UserDomain(username, password, role));
      } catch (DatabaseException ex) {
        throw new RegisterException(ex.getMessage(), ex);
      }
    } else if (action == Action.getByName("EDIT")) {
      try {
        return userRepository.update(new UserDomain(username, password, role));
      } catch (DatabaseException ex) {
        throw new EditException(ex.getMessage(), ex);
      }
    } else {
      return null;
    }
  }
}
