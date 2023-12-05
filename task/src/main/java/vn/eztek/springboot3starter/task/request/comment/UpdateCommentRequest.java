package vn.eztek.springboot3starter.task.request.comment;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCommentRequest {

  @NotBlank(message = "can-not-be-blank")
  String content;

}
