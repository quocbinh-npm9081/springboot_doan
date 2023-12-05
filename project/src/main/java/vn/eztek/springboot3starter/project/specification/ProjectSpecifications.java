package vn.eztek.springboot3starter.project.specification;

import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import vn.eztek.springboot3starter.domain.project.entity.Project;
import vn.eztek.springboot3starter.domain.project.entity.ProjectStatus;

public class ProjectSpecifications {

  public static Specification<Project> getFilter(ProjectCriteria criteria) {
    Specification<Project> specification = Specification.where(null);

    if (criteria.getStatuses() != null) {
      specification = specification.and(statusIn(criteria.getStatuses()));
    }
    if (criteria.getKeyword() != null) {
      var nameSpecification = nameProjectContains(criteria.getKeyword());
      specification = specification.and(nameSpecification);
    }

    return specification;
  }

  static Specification<Project> statusIn(List<ProjectStatus> statuses) {
    return (project, cq, cb) -> project.get("status").in(statuses);
  }

  static Specification<Project> nameProjectContains(String keyword) {
    return (project, cq, cb) -> cb.like(cb.lower(project.get("name")),
        "%" + keyword.toLowerCase() + "%");
  }

}
