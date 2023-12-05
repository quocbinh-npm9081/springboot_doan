package vn.eztek.springboot3starter.shared.response;


import java.util.List;
import lombok.Getter;
import vn.eztek.springboot3starter.shared.cqrs.CommandResult;
import vn.eztek.springboot3starter.shared.cqrs.QueryResult;

@Getter
public class ListResponse<T> implements QueryResult, CommandResult {

  List<T> content;

  public ListResponse(List<T> content) {
    this.content = content;
  }

}
