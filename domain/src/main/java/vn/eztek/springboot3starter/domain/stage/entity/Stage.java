package vn.eztek.springboot3starter.domain.stage.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.eztek.springboot3starter.domain.project.entity.Project;
import vn.eztek.springboot3starter.domain.shared.Auditable;
import vn.eztek.springboot3starter.domain.task.entity.Task;

@Entity(name = "stages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Stage extends Auditable<String> implements Comparable<Stage> {

  @Column(nullable = false)
  private String name;

  @Column(name = "order_number", nullable = false)
  private Integer orderNumber;

  @ManyToOne
  @JoinColumn(name = "project_id")
  private Project project;


  public Stage(String name, Project project) {
    this.name = name;
    this.project = project;
  }

  @Override
  public int compareTo(Stage o) {
    return Integer.compare(orderNumber, o.orderNumber);
  }
}
