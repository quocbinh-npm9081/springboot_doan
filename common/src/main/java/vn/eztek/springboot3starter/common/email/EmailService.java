package vn.eztek.springboot3starter.common.email;

import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

public interface EmailService {

  void sendHtmlMessage(String to, String templateId, Map<String, String> templateData);

  void sendHtmlMessageWithAttachments(String to, String templateId,
      Map<String, String> templateData, List<MultipartFile> files);

}
