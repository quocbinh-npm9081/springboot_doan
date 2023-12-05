package vn.eztek.springboot3starter.common.redis.messages;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SendMailMessage {

  String email;
  String templateId;
  Map<String, String> templateData;

  public static SendMailMessage create(String email, String templateId,
      Map<String, String> templateData) {
    return new SendMailMessage(email, templateId, templateData);
  }
}
