package vn.eztek.springboot3starter.invitation.query.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.invitation.entity.InvitationType;
import vn.eztek.springboot3starter.domain.invitation.repository.InvitationRepository;
import vn.eztek.springboot3starter.invitation.mapper.InvitationMapper;
import vn.eztek.springboot3starter.invitation.query.ListInvitationQuery;
import vn.eztek.springboot3starter.invitation.query.validator.ListInvitationQueryValidator;
import vn.eztek.springboot3starter.invitation.response.InvitationResponse;
import vn.eztek.springboot3starter.invitation.vo.InvitationAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.QueryHandler;
import vn.eztek.springboot3starter.shared.response.ListResponse;
import vn.eztek.springboot3starter.shared.util.DateUtils;

@Component
@RequiredArgsConstructor
public class ListInvitationQueryHandler implements
    QueryHandler<ListInvitationQuery, ListResponse<InvitationResponse>, InvitationAggregateId> {

  private final ListInvitationQueryValidator validator;
  private final InvitationMapper invitationMapper;
  private final InvitationRepository invitationRepository;

  @Override
  public ListResponse<InvitationResponse> handle(ListInvitationQuery query,
      InvitationAggregateId entityId) {
    // validating
    var validated = validator.validate(query);

    // handling
    var invitations = invitationRepository.findByUserIdAndUsedFalseAndExpiredTimeAfterAndAction(
        validated.getUser().getId(), DateUtils.currentZonedDateTime(),
        InvitationType.INVITE_TALENT_BY_MAIL);

    var invitationResponses = invitations.stream().map(invitationMapper::mapToInvitationResponse)
        .toList();

    // resulting
    return new ListResponse<>(invitationResponses);
  }

}
