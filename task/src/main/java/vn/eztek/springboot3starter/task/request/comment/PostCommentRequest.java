package vn.eztek.springboot3starter.task.request.comment;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCommentRequest {

  @NotBlank(message = "can-not-be-blank")
  String content;
  UUID parentId;

}
