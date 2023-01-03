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
    var userEntity = findFirstByUsername(username);
    return getUserDetails(userEntity);
  }

  @Override
  public UserDetails save(UserDomain userDomain) {
    String username = userDomain.username();

    var userEntity = findFirstByUsername(username);

    if (userEntity != null) {
      throw new DatabaseException(String.format("Duplicate username: %s", username));
    }

    String password = userDomain.password();
    String role = userDomain.role();

    userEntity = save(null, username, password, role);

    return getUserDetails(userEntity);
  }

  public UserDetails update(UserDomain userDomain) {
    String username = userDomain.username();

    var userEntity = userEntityRepository.findFirstByUsername(username);

    if (userEntity == null) {
      throw new DatabaseException(String.format("Can not find user with username: %s", username));
    }

    Long userId = userEntity.getId();
    String password = userDomain.password();
    String role = userDomain.role();

    userEntity = save(userId, username, password, role);

    return getUserDetails(userEntity);
  }

  private UserEntity findFirstByUsername(String username) {
    return userEntityRepository.findFirstByUsername(username);
  }

  private UserEntity save(Long id, String username, String password, String role) {
    return userEntityRepository.save(new UserEntity(id, username, password, role));
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
