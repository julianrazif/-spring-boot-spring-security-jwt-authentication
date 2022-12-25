package id.julianraziffigaro.demo.sbssja.web.repository;

import id.julianraziffigaro.demo.sbssja.web.domain.model.UserDomain;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository {

  UserDetails findByUsername(String username);

  UserDetails save(UserDomain userDomain);
}
