package vn.eztek.springboot3starter.domain.checkListItem.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.eztek.springboot3starter.domain.checkListItem.entity.CheckListItem;

public interface CheckListItemRepository extends JpaRepository<CheckListItem, UUID> {

  void deleteAllByCheckListId(UUID checkListId);
}
