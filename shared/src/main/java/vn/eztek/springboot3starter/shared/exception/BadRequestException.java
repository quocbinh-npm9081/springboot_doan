package vn.eztek.springboot3starter.shared.exception;

import java.io.Serial;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 1L;

  public BadRequestException(String message) {
    super(message);
  }

}
