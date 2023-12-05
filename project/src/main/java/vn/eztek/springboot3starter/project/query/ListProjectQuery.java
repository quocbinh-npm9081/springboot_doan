package vn.eztek.springboot3starter.project.query;

import lombok.Value;
import org.springframework.data.domain.Pageable;
import vn.eztek.springboot3starter.project.specification.ProjectCriteria;
import vn.eztek.springboot3starter.shared.cqrs.Query;

@Value(staticConstructor = "queryOf")
public class ListProjectQuery implements Query {

  ProjectCriteria criteria;
  Pageable pageable;
}
