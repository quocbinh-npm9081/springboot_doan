package vn.eztek.springboot3starter.domain.attachment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.eztek.springboot3starter.domain.shared.Auditable;
import vn.eztek.springboot3starter.domain.task.entity.Task;

@Entity
@Table(name = "attachments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Attachment extends Auditable<String> {

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String originalName;

  @ManyToOne
  @JoinColumn(name = "task_id")
  @JsonIgnore
  private Task task;

}
