package vn.eztek.springboot3starter.task.request.attachment;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAttachmentRequest {

  @NotBlank(message = "id-cannot-be-blank")
  String name;
}
