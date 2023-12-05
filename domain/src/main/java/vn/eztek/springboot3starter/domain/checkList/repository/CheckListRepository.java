package vn.eztek.springboot3starter.domain.checkList.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.eztek.springboot3starter.domain.checkList.entity.CheckList;

public interface CheckListRepository extends JpaRepository<CheckList, UUID> {

  List<CheckList> findByTaskId(UUID taskId);
}
