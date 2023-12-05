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
public class ProjectRestoreMessage implements MessageEventData {

  UUID id;
  String name;
  String email;
  String username;
  List<UserProjectMessage> userProjects;

  public static ProjectRestoreMessage create(UUID id, String name, String email, String username,
      List<UserProjectMessage> userProjects) {
    return new ProjectRestoreMessage(id, name, email, username, userProjects);
  }
}
