package vn.eztek.springboot3starter.common.redis.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.redis.MessageEventData;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FolderDeleteMessage implements MessageEventData {

  String folderPath;

  public static FileDeleteMessage create(String folderPath) {
    return new FileDeleteMessage(folderPath);
  }

}
