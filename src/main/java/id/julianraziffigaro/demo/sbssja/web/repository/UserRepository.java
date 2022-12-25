package id.julianraziffigaro.demo.sbssja.web.repository;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository {

  UserDetails findByUsername(String username);
}
