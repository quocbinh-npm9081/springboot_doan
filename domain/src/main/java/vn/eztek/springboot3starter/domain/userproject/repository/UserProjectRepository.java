package vn.eztek.springboot3starter.domain.userproject.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.swing.text.html.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProject;

public interface UserProjectRepository extends JpaRepository<UserProject, UUID> {

  List<UserProject> findByProjectId(UUID projectId);

  Optional<UserProject> findByProjectIdAndUserId(UUID projectId, UUID userId);

  List<UserProject> findByProjectIdAndUserIdNot(UUID projectId, UUID userId);

  List<UserProject> findByUserId(UUID userId);

  List<UserProject> findByProjectOwnerIdAndUserIdNot(UUID ownerId, UUID userId);
}
