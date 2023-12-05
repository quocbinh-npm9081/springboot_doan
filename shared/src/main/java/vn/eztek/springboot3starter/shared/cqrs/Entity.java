package vn.eztek.springboot3starter.shared.cqrs;

import java.io.Serializable;

/**
 * An entity, as explained in the DDD book.
 */
public interface Entity<E, ID extends Serializable> {

  /**
   * Entities compare by identity, not by attributes.
   *
   * @param other The other entity.
   * @return true if the identities are the same, regardles of other attributes.
   */
  boolean sameIdentityAs(E other);

  ID id();

}
