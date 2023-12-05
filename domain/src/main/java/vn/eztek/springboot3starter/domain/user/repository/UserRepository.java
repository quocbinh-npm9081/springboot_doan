package vn.eztek.springboot3starter.domain.user.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;

public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

  Optional<User> findByIdAndDeletedAtNull(UUID id);

  Optional<User> findByUsernameIgnoreCaseAndDeletedAtNull(String username);

  Optional<User> findByUsernameIgnoreCase(String username);

  Boolean existsByUsernameIgnoreCaseAndDeletedAtNull(String username);

  Boolean existsByUsernameIgnoreCase(String username);

  Boolean existsByUsernameAndStatusAndDeletedAtNull(String username, UserStatus status);

  Optional<User> findByUsernameContainingIgnoreCase(String username);

}
