package vn.eztek.springboot3starter.domain.privilege.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.eztek.springboot3starter.domain.shared.BaseEntity;

@Entity
@Table(name = "privileges")
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
public class Privilege extends BaseEntity {

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PrivilegeName name;

  public Privilege(UUID id, PrivilegeName name) {
    super();
    this.setId(id);
    this.name = name;
  }
}
