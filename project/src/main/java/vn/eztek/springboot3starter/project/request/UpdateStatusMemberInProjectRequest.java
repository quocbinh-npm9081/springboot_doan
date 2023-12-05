package vn.eztek.springboot3starter.project.request;

import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProjectStatus;

@Getter
@Setter
public class UpdateStatusMemberInProjectRequest {

  UserProjectStatus status;

}
