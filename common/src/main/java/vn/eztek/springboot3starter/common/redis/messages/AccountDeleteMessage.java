package vn.eztek.springboot3starter.common.redis.messages;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.redis.MessageEventData;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDeleteMessage implements MessageEventData {

  String id;
  String name;
  String email;
  List<UserProjectMessage> userProjects;

  public static AccountDeleteMessage create(String id, String name, String email,
      List<UserProjectMessage> userProjects) {
    return new AccountDeleteMessage(id, name, email, userProjects);
  }

}
