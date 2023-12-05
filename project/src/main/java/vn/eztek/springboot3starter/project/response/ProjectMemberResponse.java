package vn.eztek.springboot3starter.project.response;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProjectStatus;

@Getter
@Setter
public class ProjectMemberResponse {

  UUID id;
  String firstName;
  String lastName;
  String email;
  String phoneNumber;
  UserProjectStatus status;

}
