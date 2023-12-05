package vn.eztek.springboot3starter.common.redis.messages;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.redis.MessageEventData;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountRestoreMessage implements MessageEventData {

  UUID id;
  String email;
  String name;
  List<UserProjectMessage> userProjects;

  public static AccountRestoreMessage create(UUID id, String email, String name,
      List<UserProjectMessage> userProjects) {
    return new AccountRestoreMessage(id, email, name, userProjects);
  }

}
