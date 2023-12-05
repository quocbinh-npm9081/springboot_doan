package vn.eztek.springboot3starter.domain.label.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.eztek.springboot3starter.domain.shared.Auditable;

import java.util.UUID;

@Entity(name = "labels")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Label extends Auditable<String> {
  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private UUID taskId;

  private Boolean isMarked = false;

  private String color;
}
