package id.julianraziffigaro.demo.sbssja.web.domain.dto.request;

import id.julianraziffigaro.demo.sbssja.web.domain.dto.BaseDTO;
import id.julianraziffigaro.demo.sbssja.web.domain.model.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestDTO<T extends BaseDomain> implements BaseDTO {

  private static final long serialVersionUID = 6434406715290495677L;

  private T data;
}
