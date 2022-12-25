package id.julianraziffigaro.demo.sbssja.web.repository.impl;

import id.julianraziffigaro.demo.sbssja.security.model.User;
import id.julianraziffigaro.demo.sbssja.web.domain.entity.UserEntity;
import id.julianraziffigaro.demo.sbssja.web.exception.DataNotFoundException;
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
}
