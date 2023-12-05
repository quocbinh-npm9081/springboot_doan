package vn.eztek.springboot3starter.domain.key.repository;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.eztek.springboot3starter.domain.key.entity.Key;
import vn.eztek.springboot3starter.domain.key.entity.KeyType;

public interface KeyRepository extends JpaRepository<Key, UUID> {

  Optional<Key> findByKey(String key);

  Optional<Key> findByKeyAndUsedFalseAndExpiredTimeAfterAndAction(String key, ZonedDateTime now,
      KeyType action);

  Optional<Key> findByUserIdAndUsedFalseAndExpiredTimeAfterAndAction(UUID userId,
      ZonedDateTime now, KeyType action);

  Optional<Key> findByKeyAndUsedFalseAndAction(String key, KeyType action);

}
