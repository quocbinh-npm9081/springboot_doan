package vn.eztek.springboot3starter.domain.checkList.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.eztek.springboot3starter.domain.checkListItem.entity.CheckListItem;
import vn.eztek.springboot3starter.domain.shared.Auditable;

@Entity(name = "check_lists")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckList extends Auditable<String> {

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private UUID taskId;

  @OneToMany(mappedBy = "checkList", fetch = FetchType.EAGER)
  @Column(columnDefinition = "checkListItems", insertable = false, updatable = false)
  @OrderBy("createdDate ASC")
  List<CheckListItem> checkListItems;
}
