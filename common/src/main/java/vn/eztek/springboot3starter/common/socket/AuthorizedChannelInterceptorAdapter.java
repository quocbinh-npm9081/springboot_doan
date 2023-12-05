package vn.eztek.springboot3starter.common.socket;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.common.exception.UnauthorizedException;
import vn.eztek.springboot3starter.common.security.jwt.JwtProvider;
import vn.eztek.springboot3starter.domain.task.repository.TaskRepository;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProjectStatus;
import vn.eztek.springboot3starter.domain.userproject.repository.UserProjectRepository;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;

@Component
@RequiredArgsConstructor
public class AuthorizedChannelInterceptorAdapter implements ChannelInterceptor {

  private final UserProjectRepository userProjectRepository;
  private final TaskRepository taskRepository;
  private final UserRepository userRepository;
  private final JwtProvider tokenProvider;

  @Override
  public Message<?> preSend(@NotNull Message<?> message, @NotNull MessageChannel messageChannel) {
    StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message,
        StompHeaderAccessor.class);
    if (accessor == null) {
      return message;
    }
    if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
      List<String> authorizationHeaders = accessor.getNativeHeader("Authorization");
      if (authorizationHeaders == null || authorizationHeaders.isEmpty()) {
        throw new UnauthorizedException("unable-to-get-token");
      }
      validateSubscription(authorizationHeaders.get(0), accessor.getDestination());
    }

    return message;
  }

  private void validateSubscription(String bearerToken, String destination) {
    if (!bearerToken.startsWith("Bearer ")) {
      throw new UnauthorizedException("invalid-token");
    }
    var username = tokenProvider.getUserNameFromJwtToken(bearerToken.substring(7));
    var user = userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(username)
        .orElseThrow(() -> new BadRequestException("user-not-found"));

    if (user.getStatus().equals(UserStatus.INACTIVE)) {
      throw new BadRequestException(
          "user-with-email-[%s]-has-been-deactivate-by-admin,-please-contact-to-admin-for-more-information".formatted(
              username));
    }

    String[] paths = destination.split("/");
    if (destination.contains("notifications")) {
      var id = UUID.fromString(paths[paths.length - 2]);
      if (user.getId().equals(id)) {
        return;
      }
      throw new AccessDeniedException("No-permission-to-subscribe-to-this-topic");
    }

    var id = UUID.fromString(paths[paths.length - 1]);
    if (destination.contains("projects")) {
      var members = userProjectRepository.findByProjectId(id);
      var memberJoinIds = members.stream()
          .filter(x -> x.getStatus().equals(UserProjectStatus.JOINED))
          .map(x -> x.getUser().getId()).toList();
      var isJoinedMember = memberJoinIds.contains(user.getId());
      if (!isJoinedMember) {
        throw new AccessDeniedException("No-permission-to-subscribe-to-this-topic");
      }
      return;
    }

    var task = taskRepository.findById(id)
        .orElseThrow(() -> new BadRequestException("task-not-found"));

    var members = userProjectRepository.findByProjectId(task.getProject().getId());
    var memberJoinIds = members.stream().filter(x -> x.getStatus().equals(UserProjectStatus.JOINED))
        .map(x -> x.getUser().getId()).toList();
    var isJoinedMember = memberJoinIds.contains(user.getId());
    if (!isJoinedMember) {
      throw new AccessDeniedException("No-permission-to-subscribe-to-this-topic");
    }
  }
}