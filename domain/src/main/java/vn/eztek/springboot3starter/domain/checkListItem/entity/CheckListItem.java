package vn.eztek.springboot3starter.domain.checkListItem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.eztek.springboot3starter.domain.checkList.entity.CheckList;
import vn.eztek.springboot3starter.domain.shared.Auditable;
import vn.eztek.springboot3starter.domain.user.entity.User;

@Entity(name = "check_list_items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckListItem extends Auditable<String> {

  @Column(nullable = false)
  private String content;

  private Boolean isDone = false;

  private ZonedDateTime dueDate;

  @ManyToOne
  @JoinColumn(name = "assignee_id")
  private User assignee;

  @ManyToOne
  @JoinColumn(name = "check_list_id")
  @JsonIgnore
  private CheckList checkList;

}
