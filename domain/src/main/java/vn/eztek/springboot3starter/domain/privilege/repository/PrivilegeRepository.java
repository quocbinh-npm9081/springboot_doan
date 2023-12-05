package vn.eztek.springboot3starter.domain.privilege.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.eztek.springboot3starter.domain.privilege.entity.Privilege;
import vn.eztek.springboot3starter.domain.privilege.entity.PrivilegeName;

public interface PrivilegeRepository extends JpaRepository<Privilege, UUID> {

  Optional<Privilege> findByName(PrivilegeName roleName);

}
