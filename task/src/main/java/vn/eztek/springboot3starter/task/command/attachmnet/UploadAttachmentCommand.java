package vn.eztek.springboot3starter.task.command.attachmnet;

import java.util.UUID;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;
import vn.eztek.springboot3starter.shared.cqrs.Command;

@Value(staticConstructor = "commandOf")
public class UploadAttachmentCommand implements Command {

  UUID taskId;
  MultipartFile attachment;
}
