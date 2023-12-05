package vn.eztek.springboot3starter.domain.task.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.eztek.springboot3starter.domain.attachment.entity.Attachment;
import vn.eztek.springboot3starter.domain.project.entity.Project;
import vn.eztek.springboot3starter.domain.shared.Auditable;
import vn.eztek.springboot3starter.domain.stage.entity.Stage;
import vn.eztek.springboot3starter.domain.user.entity.User;

@Entity(name = "tasks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Task extends Auditable<String> {

  @Column(nullable = false)
  private String title;

  @Column()
  private String description;

  @ManyToOne
  @JoinColumn(name = "project_id")
  private Project project;

  @ManyToOne
  @JoinColumn(name = "stage_id")
  private Stage stage;

  @ManyToOne
  @JoinColumn(name = "owner_id")
  private User owner;

  @ManyToOne
  @JoinColumn(name = "assignee_id")
  private User assignee;

  @Column
  private UUID nextId;

  @Column
  private UUID previousId;

  @OneToMany(mappedBy = "task", fetch = FetchType.EAGER)
  @Column(columnDefinition = "attachments", insertable = false, updatable = false)
  private List<Attachment> attachments = new ArrayList<>();

}
