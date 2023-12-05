package vn.eztek.springboot3starter.project.command.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.invitation.entity.InvitationType;
import vn.eztek.springboot3starter.domain.invitation.repository.InvitationRepository;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProjectStatus;
import vn.eztek.springboot3starter.domain.userproject.repository.UserProjectRepository;
import vn.eztek.springboot3starter.project.command.CreateSharedLinkCommand;
import vn.eztek.springboot3starter.project.command.validated.CreateSharedLinkCommandValidated;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;
import vn.eztek.springboot3starter.shared.util.DateUtils;


@Component
@RequiredArgsConstructor
public class CreateSharedLinkCommandValidator extends
    CommandValidation<CreateSharedLinkCommand, CreateSharedLinkCommandValidated> {

  private final UserRepository userRepository;
  private final InvitationRepository invitationRepository;
  private final UserProjectRepository userProjectRepository;

  @Override
  public CreateSharedLinkCommandValidated validate(CreateSharedLinkCommand command) {

    var userName = SecurityContextHolder.getContext().getAuthentication().getName();
    var existUser = userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(userName)
        .orElseThrow(() -> new NotFoundException("user-not-found"));

    if (!existUser.getStatus().equals(UserStatus.ACTIVE)) {
      throw new BadRequestException(
          "user-with-email-[%s]-has-been-deactivate-by-admin,-please-contact-to-admin-for-more-information".formatted(
              userName));
    }

    var members = userProjectRepository.findByProjectId(command.getProjectId());
    if (members.isEmpty()) {
      throw new BadRequestException("project-not-found");
    }
    var memberJoinIds = members.stream().filter(x -> x.getStatus().equals(UserProjectStatus.JOINED))
        .map(x -> x.getUser().getId()).toList();

    var isJoinedMember = memberJoinIds.contains(existUser.getId());
    if (!isJoinedMember) {
      throw new AccessDeniedException("you-can-not-delete-comment-in-this-project");
    }

    var invitation = invitationRepository.findByInviterIdAndProjectIdAndUsedFalseAndExpiredTimeAfterAndAction(
        existUser.getId(), command.getProjectId(), DateUtils.currentZonedDateTime(),
        InvitationType.INVITE_TALENT_BY_LINK).orElse(null);

    return CreateSharedLinkCommandValidated.validatedOf(existUser, members.get(0).getProject(),
        invitation);
  }

}
