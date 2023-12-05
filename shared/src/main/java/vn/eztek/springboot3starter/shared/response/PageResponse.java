package vn.eztek.springboot3starter.shared.response;


import java.util.List;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import vn.eztek.springboot3starter.shared.cqrs.QueryResult;

public class PageResponse<T> extends PageImpl<T> implements QueryResult {

  public PageResponse(List<T> content, Pageable pageable, long total) {
    super(content, pageable, total);
  }

}
