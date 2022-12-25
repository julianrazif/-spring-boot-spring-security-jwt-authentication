package id.julianraziffigaro.demo.sbssja.web.controller;

import id.julianraziffigaro.demo.sbssja.web.domain.dto.BaseDTO;
import id.julianraziffigaro.demo.sbssja.web.domain.dto.response.ResponseDTO;
import id.julianraziffigaro.demo.sbssja.web.domain.model.BaseDomain;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {

  protected ResponseEntity<BaseDTO> restResponse(HttpStatus httpStatus, ResponseDTO<? extends BaseDomain> responseDTO) {
    return ResponseEntity.status(httpStatus).body(responseDTO);
  }
}
