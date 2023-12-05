package vn.eztek.springboot3starter.domain.label.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.eztek.springboot3starter.domain.label.entity.Label;

import java.util.List;
import java.util.UUID;

public interface LabelRepository extends JpaRepository<Label, UUID> {
  List<Label> findByTaskId(UUID taskId);

  List<Label> findByTaskIdAndIsMarkedTrue(UUID taskId);
}
