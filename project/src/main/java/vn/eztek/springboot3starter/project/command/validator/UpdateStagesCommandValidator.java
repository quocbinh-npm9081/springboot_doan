package vn.eztek.springboot3starter.project.command.validator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.project.repository.ProjectRepository;
import vn.eztek.springboot3starter.domain.stage.entity.Stage;
import vn.eztek.springboot3starter.domain.stage.repository.StageRepository;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.domain.userproject.entity.UserProjectStatus;
import vn.eztek.springboot3starter.domain.userproject.repository.UserProjectRepository;
import vn.eztek.springboot3starter.project.command.UpdateStagesCommand;
import vn.eztek.springboot3starter.project.command.validated.UpdateStagesCommandValidated;
import vn.eztek.springboot3starter.project.request.StageRequest;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;


@Component
@RequiredArgsConstructor
public class UpdateStagesCommandValidator extends
    CommandValidation<UpdateStagesCommand, UpdateStagesCommandValidated> {

  private final ProjectRepository projectRepository;
  private final UserRepository userRepository;
  private final StageRepository stageRepository;
  private final UserProjectRepository userProjectRepository;

  @Override
  public UpdateStagesCommandValidated validate(UpdateStagesCommand command) {
    var userName = SecurityContextHolder.getContext().getAuthentication().getName();

    var existedUser = userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(userName)
        .orElseThrow(() -> new NotFoundException("user-not-found"));
    if (!existedUser.getStatus().equals(UserStatus.ACTIVE)) {
      throw new BadRequestException("user-not-active");
    }
    var existedProject = projectRepository.findByIdAndDeletedAtNull(command.getProjectId())
        .orElseThrow(() -> new NotFoundException("project-not-found"));

    var members = userProjectRepository.findByProjectId(command.getProjectId());
    var memberJoinIds = members.stream().filter(x -> x.getStatus().equals(UserProjectStatus.JOINED))
        .map(x -> x.getUser().getId()).toList();

    var isJoinedMember = memberJoinIds.contains(existedUser.getId());
    if (!isJoinedMember) {
      throw new AccessDeniedException("you-can-not-update-stage-this-project");
    }

    Set<String> set = new HashSet<>(
        command.getStageList().stream().map(StageRequest::getName).toList());
    if (set.size() != command.getStageList().size()) {
      throw new BadRequestException("stage-name-duplicate");
    }

    var stages = stageRepository.findByProjectIdOrderByOrderNumberAsc(command.getProjectId());
    var requestNonNullIds = command.getStageList().stream().map(StageRequest::getId)
        .filter(Objects::nonNull)
        .toList();

    List<Stage> stagesPreUpdate = new ArrayList<>();
    List<Stage> deleteStages = new ArrayList<>();

    // add deleted stage to deleteStages
    for (var stage : stages) {
      if (!requestNonNullIds.contains(stage.getId())) {
        deleteStages.add(stage);
      }
    }

    // Check stage  exist and not exist
    for (var i : command.getStageList()) {
      if (i.getId() == null) {
        stagesPreUpdate.add(new Stage(i.getName(), existedProject));
      } else {
        var existStage = stageRepository.findById(i.getId())
            .orElseThrow(() -> new NotFoundException("stage-not-found"));
        existStage.setName(i.getName());
        stagesPreUpdate.add(existStage);
      }
    }

    return UpdateStagesCommandValidated.validatedOf(stagesPreUpdate, deleteStages, existedUser,
        existedProject);
  }
}
