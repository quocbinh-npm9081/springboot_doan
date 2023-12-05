package vn.eztek.springboot3starter.project.query;

import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.Query;

@Value(staticConstructor = "queryOf")
public class GetMyProjectQuery implements Query {

}
