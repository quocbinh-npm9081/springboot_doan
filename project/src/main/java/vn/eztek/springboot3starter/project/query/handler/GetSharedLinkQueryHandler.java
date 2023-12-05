package vn.eztek.springboot3starter.project.query.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.invitation.entity.InvitationType;
import vn.eztek.springboot3starter.domain.invitation.repository.InvitationRepository;
import vn.eztek.springboot3starter.project.mapper.ProjectMapper;
import vn.eztek.springboot3starter.project.query.GetSharedLinkQuery;
import vn.eztek.springboot3starter.project.query.validator.GetSharedLinkQueryValidator;
import vn.eztek.springboot3starter.project.response.LinkShareResponse;
import vn.eztek.springboot3starter.project.vo.ProjectAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.QueryHandler;
import vn.eztek.springboot3starter.shared.util.DateUtils;

@Component
@RequiredArgsConstructor
public class GetSharedLinkQueryHandler
    implements QueryHandler<GetSharedLinkQuery, LinkShareResponse, ProjectAggregateId> {

  private final GetSharedLinkQueryValidator validator;
  private final ProjectMapper projectMapper;
  private final InvitationRepository invitationRepository;

  @Override
  public LinkShareResponse handle(GetSharedLinkQuery query, ProjectAggregateId entityId) {

    var validated = validator.validate(query);

    var invitation = invitationRepository.findByInviterIdAndProjectIdAndUsedFalseAndExpiredTimeAfterAndAction(
        validated.getUserId(), query.getId(), DateUtils.currentZonedDateTime(),
        InvitationType.INVITE_TALENT_BY_LINK).orElse(null);

    var key = invitation == null ? null : invitation.getKey();
    return projectMapper.mapToLinkShareResponse(key);
  }

}
