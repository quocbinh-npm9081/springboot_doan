package vn.eztek.springboot3starter.project.query;


import java.util.UUID;
import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.Query;

@Value(staticConstructor = "queryOf")
public class ListStagesQuery implements Query {

  UUID projectId;

}
