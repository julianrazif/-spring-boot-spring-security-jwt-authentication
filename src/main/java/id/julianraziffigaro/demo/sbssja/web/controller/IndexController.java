package id.julianraziffigaro.demo.sbssja.web.controller;

import id.julianraziffigaro.demo.sbssja.security.config.JWTUtils;
import id.julianraziffigaro.demo.sbssja.web.domain.dto.BaseDTO;
import id.julianraziffigaro.demo.sbssja.web.domain.dto.response.ResponseDTO;
import id.julianraziffigaro.demo.sbssja.web.domain.model.Action;
import id.julianraziffigaro.demo.sbssja.web.domain.model.AuthenticationDomain;
import id.julianraziffigaro.demo.sbssja.web.domain.model.ErrorDomain;
import id.julianraziffigaro.demo.sbssja.web.domain.model.UserDomain;
import id.julianraziffigaro.demo.sbssja.web.exception.EditException;
import id.julianraziffigaro.demo.sbssja.web.exception.LoginException;
import id.julianraziffigaro.demo.sbssja.web.exception.PasswordException;
import id.julianraziffigaro.demo.sbssja.web.exception.RegisterException;
import id.julianraziffigaro.demo.sbssja.web.service.impl.UserServiceImpl;
import io.jsonwebtoken.security.InvalidKeyException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class IndexController extends BaseController {

  private final UserServiceImpl userService;

  private final JWTUtils jwtUtils;

  @GetMapping(value = "/")
  public String getIndex() {
    return "Hello World!";
  }

  @PostMapping(value = "/login")
  public ResponseEntity<BaseDTO> login(@RequestBody BaseDTO requestDTO) {
    try {
      UserDetails user = userService.login((UserDomain) requestDTO.getData());

      String token = jwtUtils.generateToken(user);

      return restResponse(
        HttpStatus.OK,
        new ResponseDTO<>(
          HttpStatus.OK.value(),
          new AuthenticationDomain(token)
        )
      );
    } catch (LoginException ex) {
      return restResponse(
        HttpStatus.BAD_REQUEST,
        new ResponseDTO<>(
          HttpStatus.BAD_REQUEST.value(),
          new ErrorDomain(ex.hashCode(), ex.getMessage())
        )
      );
    } catch (PasswordException | InvalidKeyException ex) {
      return restResponse(
        HttpStatus.INTERNAL_SERVER_ERROR,
        new ResponseDTO<>(
          HttpStatus.INTERNAL_SERVER_ERROR.value(),
          new ErrorDomain(ex.hashCode(), ex.getMessage())
        )
      );
    }
  }

  @PostMapping(value = "/register",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BaseDTO> register(@RequestBody BaseDTO requestDTO) {
    try {
      UserDetails user = userService.saveOrUpdate(Action.ADD, (UserDomain) requestDTO.getData());

      return restResponse(
        HttpStatus.CREATED,
        new ResponseDTO<>(
          HttpStatus.CREATED.value(),
          new UserDomain(user.getUsername(), null, jwtUtils.populateAuthorities(user.getAuthorities()))
        )
      );
    } catch (PasswordException | RegisterException ex) {
      return restResponse(
        HttpStatus.INTERNAL_SERVER_ERROR,
        new ResponseDTO<>(
          HttpStatus.INTERNAL_SERVER_ERROR.value(),
          new ErrorDomain(ex.hashCode(), ex.getMessage())
        )
      );
    }
  }

  @PostMapping(value = "/update",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BaseDTO> update(@RequestBody BaseDTO requestDTO) {
    try {
      UserDetails user = userService.saveOrUpdate(Action.EDIT, (UserDomain) requestDTO.getData());

      return restResponse(
        HttpStatus.OK,
        new ResponseDTO<>(
          HttpStatus.OK.value(),
          new UserDomain(user.getUsername(), null, jwtUtils.populateAuthorities(user.getAuthorities()))
        )
      );
    } catch (PasswordException | EditException ex) {
      return restResponse(
        HttpStatus.INTERNAL_SERVER_ERROR,
        new ResponseDTO<>(
          HttpStatus.INTERNAL_SERVER_ERROR.value(),
          new ErrorDomain(ex.hashCode(), ex.getMessage())
        )
      );
    }
  }
}
