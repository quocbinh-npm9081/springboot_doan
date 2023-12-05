package vn.eztek.springboot3starter.project.response;

import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.cqrs.CommandResult;
import vn.eztek.springboot3starter.shared.cqrs.QueryResult;

@Getter
@Setter
public class LinkShareResponse implements CommandResult, QueryResult {

  String key;
}
