package vn.eztek.springboot3starter.project.request;

import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddMembersToProjectRequest {

  @Size(min = 1, message = "required-at-least-one-email")
  List<String> emails;

}
