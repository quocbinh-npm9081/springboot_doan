package vn.eztek.springboot3starter.shared.cqrs;

public interface QueryHandler<Q extends Query, R extends QueryResult, ID> {

  R handle(Q query, ID entityId);

}
