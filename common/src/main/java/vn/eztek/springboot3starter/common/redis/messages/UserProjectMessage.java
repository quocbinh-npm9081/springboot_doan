package vn.eztek.springboot3starter.common.redis.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProjectMessage {

  String firstName;
  String lastName;
  String username;
  String projectName;
  String projectId;
  String userId;
}
