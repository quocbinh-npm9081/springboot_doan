package vn.eztek.springboot3starter.task.command.validator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.task.entity.Task;
import vn.eztek.springboot3starter.domain.task.repository.TaskRepository;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProjectStatus;
import vn.eztek.springboot3starter.domain.userproject.repository.UserProjectRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;
import vn.eztek.springboot3starter.shared.util.CheckPropertyPatch;
import vn.eztek.springboot3starter.task.command.UpdateTaskCommand;
import vn.eztek.springboot3starter.task.command.validated.UpdateTaskCommandValidated;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UpdateTaskCommandValidator extends
    CommandValidation<UpdateTaskCommand, UpdateTaskCommandValidated> {

  private final UserRepository userRepository;
  private final TaskRepository taskRepository;
  private final ObjectMapper objectMapper;
  private final UserProjectRepository userProjectRepository;

  @Override
  public UpdateTaskCommandValidated validate(UpdateTaskCommand command) {
    var userName = SecurityContextHolder.getContext().getAuthentication().getName();

    var loggedInUser = userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(userName)
        .orElseThrow(() -> new NotFoundException("user-not-found"));

    if (!loggedInUser.getStatus().equals(UserStatus.ACTIVE)) {
      throw new BadRequestException(
          "user-with-email-[%s]-has-been-deactivate-by-admin,-please-contact-to-admin-for-more-information".formatted(
              userName));
    }

    var task = taskRepository.findById(command.getTaskId())
        .orElseThrow(() -> new NotFoundException("task-not-found"));

    boolean check = CheckPropertyPatch.checkFieldsViolation(command.getInput(),
        List.of("owner", "stage", "assignee", "project"));
    if (check) {
      throw new BadRequestException("fields-not-allow-to-update-please-use-another-endpoint");
    }
    JsonNode patched;
    Task taskPatched;
    try {
      patched = command.getInput().apply(objectMapper.convertValue(task, JsonNode.class));
      taskPatched = objectMapper.treeToValue(patched, Task.class);
    } catch (JsonPatchException | JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    var memberIds = userProjectRepository.findByProjectId(task.getProject().getId()).stream()
        .filter(x -> x.getStatus().equals(UserProjectStatus.JOINED))
        .map(x -> x.getUser().getId())
        .toList();

    if (!memberIds.contains(loggedInUser.getId())) {
      throw new AccessDeniedException("you-are-not-permission-update-task-in-this-project");
    }
    return UpdateTaskCommandValidated.validatedOf(loggedInUser, taskPatched);
  }

}
