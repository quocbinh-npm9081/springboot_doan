package vn.eztek.springboot3starter.project.specification;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.domain.project.entity.ProjectStatus;

@Getter
@Setter
public class ProjectCriteria {

  private List<ProjectStatus> statuses;
  private String keyword;

}
