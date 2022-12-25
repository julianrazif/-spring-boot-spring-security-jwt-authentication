package id.julianraziffigaro.demo.sbssja.web.repository.impl;

import id.julianraziffigaro.demo.sbssja.security.model.User;
import id.julianraziffigaro.demo.sbssja.web.domain.entity.UserEntity;
import id.julianraziffigaro.demo.sbssja.web.domain.model.UserDomain;
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
    UserEntity userEntity = findFirstByUsername(username);
    return getUserDetails(userEntity);
  }

  @Override
  public UserDetails save(UserDomain userDomain) {
    String username = userDomain.username();
    String password = userDomain.password();
    String role = userDomain.role();

    UserEntity userEntity = findFirstByUsername(username);

    if (userEntity != null) {
      throw new DatabaseException(String.format("Duplicate username: %s", username));
    }

    userEntity = save(null, username, password, role);

    return getUserDetails(userEntity);
  }

  public UserDetails update(UserDomain userDomain) {
    String username = userDomain.username();
    String password = userDomain.password();
    String role = userDomain.role();

    UserEntity userEntity = userEntityRepository.findFirstByUsername(username);

    if (userEntity == null) {
      throw new DatabaseException(String.format("Can not find user with username: %s", username));
    }

    Long userId = userEntity.getId();

    userEntity = save(userId, username, password, role);

    return getUserDetails(userEntity);
  }

  private UserEntity findFirstByUsername(String username) {
    UserEntity userEntity;

    try {
      userEntity = userEntityRepository.findFirstByUsername(username);
    } catch (Exception ex) {
      throw new DatabaseException(String.format("Can not find the user in database with username: %s", username));
    }

    return userEntity;
  }

  private UserEntity save(Long id, String username, String password, String role) {
    UserEntity userEntity;

    try {
      userEntity = userEntityRepository.save(new UserEntity(id, username, password, role));
    } catch (Exception ex) {
      throw new DatabaseException(String.format("Can not save or update the user to database with username: %s", username));
    }

    return userEntity;
  }

  private static UserDetails getUserDetails(UserEntity userEntity) {
    return User.withUserDetails(
      new User(
        userEntity.getUsername(),
        userEntity.getPassword(),
        AuthorityUtils.commaSeparatedStringToAuthorityList(userEntity.getRole())
      )
    ).build();
  }
}
