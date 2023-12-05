package vn.eztek.springboot3starter.domain.event.repository;

import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import vn.eztek.springboot3starter.domain.event.entity.EventAction;
import vn.eztek.springboot3starter.domain.event.entity.EventStore;
import vn.eztek.springboot3starter.domain.role.entity.RoleName;

@Repository
public interface EventStoreRepository extends MongoRepository<EventStore, String> {

  Optional<EventStore> findFirst1ByActionAndUserId(EventAction action, String userId, Sort sort);

}
