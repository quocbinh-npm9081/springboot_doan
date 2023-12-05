package vn.eztek.springboot3starter.domain.invitation.repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.eztek.springboot3starter.domain.invitation.entity.Invitation;
import vn.eztek.springboot3starter.domain.invitation.entity.InvitationType;


public interface InvitationRepository extends JpaRepository<Invitation, UUID> {

  List<Invitation> findByUserIdAndUsedFalseAndExpiredTimeAfterAndAction(UUID userId,
      ZonedDateTime now, InvitationType action);

  Optional<Invitation> findByKeyAndUsedFalseAndExpiredTimeAfterAndAction(String key,
      ZonedDateTime now, InvitationType action);

  Optional<Invitation> findByIdAndUsedFalseAndExpiredTimeAfterAndAction(UUID id, ZonedDateTime now,
      InvitationType action);

  Optional<Invitation> findByInviterIdAndProjectIdAndUsedFalseAndExpiredTimeAfterAndAction(UUID userId,
      UUID projectId, ZonedDateTime now, InvitationType action);

  Optional<Invitation> findByUserIdAndProjectIdAndUsedFalseAndExpiredTimeAfterAndAction(UUID userId,
      UUID projectId, ZonedDateTime now, InvitationType action);

}
