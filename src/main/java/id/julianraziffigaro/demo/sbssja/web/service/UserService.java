package id.julianraziffigaro.demo.sbssja.web.service;

import id.julianraziffigaro.demo.sbssja.web.domain.model.Action;
import id.julianraziffigaro.demo.sbssja.web.domain.model.UserDomain;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

  UserDetails login(UserDomain userDomain);

  UserDetails saveOrUpdate(Action action, UserDomain userDomain);
}
