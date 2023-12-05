package vn.eztek.springboot3starter.common.email.sendgrid;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Email;
import io.jsonwebtoken.lang.Collections;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.eztek.springboot3starter.common.email.EmailService;
import vn.eztek.springboot3starter.common.property.SendGridProperties;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;

@Service
@RequiredArgsConstructor
public class SendGridEmailServiceImpl implements EmailService {

  private final SendGridProperties sendGridProperties;

  @Override
  public void sendHtmlMessage(String to, String templateId, Map<String, String> templateData) {
    var sg = new SendGrid(sendGridProperties.getApiKey());
    sg.addRequestHeader("X-Mock", "true");

    try {
      var dynamicTemplatePersonalization = new DynamicTemplatePersonalization();
      dynamicTemplatePersonalization.setDynamicTemplateData(templateData);
      var mail = buildMail(to, templateId, dynamicTemplatePersonalization);

      var request = new Request();
      request.setMethod(Method.POST);
      request.setEndpoint("mail/send");
      request.setBody(mail.build());
      Response res = sg.api(request);
      if (res.getStatusCode() < 200 && res.getStatusCode() > 399) {
        throw new BadRequestException("send-mail-in-failure");
      }
    } catch (IOException ex) {
      // TODO
      throw new BadRequestException("send-mail-in-failure");
    }
  }

  @Override
  public void sendHtmlMessageWithAttachments(String to, String templateId,
      Map<String, String> templateData, List<MultipartFile> files) {
    var sg = new SendGrid(sendGridProperties.getApiKey());
    sg.addRequestHeader("X-Mock", "true");
    try {
      var dynamicTemplatePersonalization = new DynamicTemplatePersonalization();
      dynamicTemplatePersonalization.setDynamicTemplateData(templateData);
      var mail = buildMail(to, templateId, dynamicTemplatePersonalization);

      if (!Collections.isEmpty(files)) {
        files.forEach(file -> {
          try {
            var attachments = new Attachments();
            attachments.setFilename(file.getOriginalFilename());
            attachments.setType(file.getContentType());
            var attachmentContent = Base64.getEncoder().encodeToString(file.getBytes());
            attachments.setContent(attachmentContent);
            mail.addAttachments(attachments);
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
      }

      var request = new Request();
      request.setMethod(Method.POST);
      request.setEndpoint("mail/send");
      request.setBody(mail.build());
      sg.api(request);
    } catch (IOException ex) {
      // TODO
      throw new BadRequestException("send-mail-in-failure");
    }
  }

  private Mail buildMail(String to, String templateId,
      DynamicTemplatePersonalization dynamicTemplatePersonalization) throws IOException {
    var mail = new Mail();
    var fromEmail = new Email();
    fromEmail.setName(sendGridProperties.getName());
    fromEmail.setEmail(sendGridProperties.getFromMail());
    mail.setFrom(fromEmail);

    dynamicTemplatePersonalization.addTo(new Email(to));
    mail.addPersonalization(dynamicTemplatePersonalization);
    mail.setTemplateId(templateId);

    return mail;
  }

}

