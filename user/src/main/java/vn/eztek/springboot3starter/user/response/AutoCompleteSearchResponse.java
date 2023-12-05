package vn.eztek.springboot3starter.user.response;

import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.shared.cqrs.QueryResult;

@Getter
@Setter
public class AutoCompleteSearchResponse implements QueryResult {

  String firstName;
  String lastName;
  String username;
  UserStatus status;
}
