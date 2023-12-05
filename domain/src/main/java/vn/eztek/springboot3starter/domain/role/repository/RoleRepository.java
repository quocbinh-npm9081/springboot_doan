package vn.eztek.springboot3starter.domain.role.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.eztek.springboot3starter.domain.role.entity.Role;
import vn.eztek.springboot3starter.domain.role.entity.RoleName;

public interface RoleRepository extends JpaRepository<Role, UUID> {

  Optional<Role> findByName(RoleName roleName);

}
