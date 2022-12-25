package id.julianraziffigaro.demo.sbssja.web.repository.impl;

import id.julianraziffigaro.demo.sbssja.security.model.User;
import id.julianraziffigaro.demo.sbssja.web.domain.entity.UserEntity;
import id.julianraziffigaro.demo.sbssja.web.domain.model.UserDomain;
import id.julianraziffigaro.demo.sbssja.web.exception.DataNotFoundException;
import id.julianraziffigaro.demo.sbssja.web.exception.DatabaseException;
import id.julianraziffigaro.demo.sbssja.web.repository.UserEntityRepository;
import id.julianraziffigaro.demo.sbssja.web.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {

  private final UserEntityRepository userEntityRepository;

  @Override
  public UserDetails findByUsername(String username) {
    UserEntity userEntity = userEntityRepository.findFirstByUsername(username);

    if (userEntity == null) {
      throw new DataNotFoundException(String.format("Can not find user with username: %s", username));
    }

    return User.withUserDetails(
      new User(
        userEntity.getUsername(),
        userEntity.getPassword(),
        AuthorityUtils.NO_AUTHORITIES
      )
    ).build();
  }

  @Override
  public UserDetails save(UserDomain userDomain) {
    String username = userDomain.getUsername();
    String password = userDomain.getPassword();

    UserEntity userEntity = userEntityRepository.findFirstByUsername(username);

    if (userEntity != null) {
      throw new DatabaseException(String.format("Duplicate username: %s", username));
    }

    try {
      userEntity = userEntityRepository.save(new UserEntity(null, username, password));
    } catch (Exception ex) {
      throw new DatabaseException(String.format("Can not save the user to database with username: %s", username));
    }

    return User.withUserDetails(
      new User(
        userEntity.getUsername(),
        userEntity.getPassword(),
        AuthorityUtils.NO_AUTHORITIES
      )
    ).build();
  }

  public UserDetails update(UserDomain userDomain) {
    String username = userDomain.getUsername();
    String password = userDomain.getPassword();

    UserEntity userEntity = userEntityRepository.findFirstByUsername(username);

    if (userEntity == null) {
      throw new DatabaseException(String.format("Can not find user with username: %s", username));
    }

    Long userId = userEntity.getId();

    try {
      userEntity = userEntityRepository.save(new UserEntity(userId, username, password));
    } catch (Exception ex) {
      throw new DatabaseException(String.format("Can not update the user to database with username: %s", username));
    }

    return User.withUserDetails(
      new User(
        userEntity.getUsername(),
        userEntity.getPassword(),
        AuthorityUtils.NO_AUTHORITIES
      )
    ).build();
  }
}
