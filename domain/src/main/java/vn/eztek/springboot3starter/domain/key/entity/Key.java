package vn.eztek.springboot3starter.domain.key.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.eztek.springboot3starter.domain.shared.BaseEntity;
import vn.eztek.springboot3starter.domain.user.entity.User;

@Entity
@Table(name = "keys")
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
public class Key extends BaseEntity {

//  @Id
//  @GeneratedValue(generator = "system-uuid")
//  @GenericGenerator(name = "system-uuid", strategy = "uuid")
//  private String id;

  private String key;
  private ZonedDateTime expiredTime;
  private Boolean used;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private KeyType action;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

}
