package vn.eztek.springboot3starter.shared.cqrs;

public abstract class QueryValidation<Q extends Query, QV extends QueryValidated> {

  public abstract QV validate(Q query);

}
