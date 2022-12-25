package id.julianraziffigaro.demo.sbssja.web.service;

import id.julianraziffigaro.demo.sbssja.web.domain.model.UserDomain;
import id.julianraziffigaro.demo.sbssja.web.exception.DataNotFoundException;
import id.julianraziffigaro.demo.sbssja.web.exception.InvalidFormatException;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

  UserDetails login(UserDomain userDomain) throws DataNotFoundException, InvalidFormatException;
}
