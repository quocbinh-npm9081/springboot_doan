package vn.eztek.springboot3starter.user.request;

import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;

@Getter
@Setter
public class UpdateStatusRequest {

  UserStatus status;
}
