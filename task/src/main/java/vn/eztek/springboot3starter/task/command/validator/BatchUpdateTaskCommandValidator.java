package vn.eztek.springboot3starter.task.command.validator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.stage.entity.Stage;
import vn.eztek.springboot3starter.domain.stage.repository.StageRepository;
import vn.eztek.springboot3starter.domain.task.entity.Task;
import vn.eztek.springboot3starter.domain.task.repository.TaskRepository;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProjectStatus;
import vn.eztek.springboot3starter.domain.userproject.repository.UserProjectRepository;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;
import vn.eztek.springboot3starter.task.command.BatchUpdateTaskCommand;
import vn.eztek.springboot3starter.task.command.validated.BatchUpdateTaskCommandValidated;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BatchUpdateTaskCommandValidator extends
    CommandValidation<BatchUpdateTaskCommand, BatchUpdateTaskCommandValidated> {

  private final UserRepository userRepository;
  private final StageRepository stageRepository;
  private final UserProjectRepository userProjectRepository;
  private final TaskRepository taskRepository;

  @Override
  public BatchUpdateTaskCommandValidated validate(BatchUpdateTaskCommand command) {

    var username = SecurityContextHolder.getContext().getAuthentication().getName();
    var loggedInUser = userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(username)
        .orElseThrow(() -> new NotFoundException("user-not-found"));
    if (!loggedInUser.getStatus().equals(UserStatus.ACTIVE)) {
      throw new BadRequestException(
          "user-with-email-[%s]-has-been-deactivate-by-admin,-please-contact-to-admin-for-more-information".formatted(
              username));
    }

    Set<UUID> projectIds = new HashSet<>();
    Map<Task, Stage> tasks = new HashMap<>();
    for (var item : command.getTasks()) {
      var task = taskRepository.findById(item.getId())
          .orElseThrow(() -> new NotFoundException("task-not-found"));

      projectIds.add(task.getProject().getId());
      if (projectIds.size() > 1) {
        throw new BadRequestException("tasks-are-not-in-the-same-project");
      }

      var stage = stageRepository.findById(item.getStageId())
          .orElseThrow(() -> new NotFoundException("stage-not-found"));

      if (item.getPreTaskId() != null) {
        taskRepository.findById(item.getPreTaskId())
            .orElseThrow(() -> new NotFoundException("previous-task-not-found"));
      }

      if (item.getNextTaskId() != null) {
        taskRepository.findById(item.getNextTaskId())
            .orElseThrow(() -> new NotFoundException("next-task-not-found"));
      }
      tasks.put(task, stage);
    }

    Iterator<UUID> iterator = projectIds.iterator();
    var member = userProjectRepository.findByProjectId(iterator.next());
    var memberJoinIds = member.stream()
        .filter(x -> x.getStatus().equals(UserProjectStatus.JOINED)).map(x -> x.getUser().getId())
        .toList();

    var isJoinedMember = memberJoinIds.contains(loggedInUser.getId());
    if (!isJoinedMember) {
      throw new AccessDeniedException("you-can-not-update-task-for-this-project");
    }

    return BatchUpdateTaskCommandValidated.validatedOf(loggedInUser, tasks);
  }

}
