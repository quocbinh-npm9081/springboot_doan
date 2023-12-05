package vn.eztek.springboot3starter.domain.stage.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.eztek.springboot3starter.domain.stage.entity.Stage;

public interface StageRepository extends JpaRepository<Stage, UUID> {

  List<Stage> findByProjectIdOrderByOrderNumberAsc(UUID id);

}
