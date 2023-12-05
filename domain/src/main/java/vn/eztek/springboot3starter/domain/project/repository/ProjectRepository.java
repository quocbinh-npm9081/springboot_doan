package vn.eztek.springboot3starter.domain.project.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.eztek.springboot3starter.domain.project.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, UUID>,
    JpaSpecificationExecutor<Project> {

  boolean existsByName(String name);

  Optional<Project> findByIdAndDeletedAtNull(UUID id);

  List<Project> findByOwnerId(UUID ownerId);

  List<Project> findByOwnerIdAndDeletedAtNotNull(UUID ownerId);
}
