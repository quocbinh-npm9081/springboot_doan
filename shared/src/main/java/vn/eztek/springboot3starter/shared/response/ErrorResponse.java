package vn.eztek.springboot3starter.shared.response;

import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ErrorResponse {

  private ZonedDateTime timestamp;
  private int status;
  private String errorCode;

}
