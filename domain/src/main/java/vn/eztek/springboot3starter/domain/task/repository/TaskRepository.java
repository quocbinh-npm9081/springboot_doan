package vn.eztek.springboot3starter.domain.task.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.eztek.springboot3starter.domain.task.entity.Task;

public interface TaskRepository extends JpaRepository<Task, UUID> {

  List<Task> findByProjectId(UUID projectId);

  Boolean existsByNextId(UUID nextId);

  Boolean existsByPreviousId(UUID previousId);

  List<Task> findByStageId(UUID stageId);

  Task findByStageIdAndNextIdNull(UUID stageId);

}
