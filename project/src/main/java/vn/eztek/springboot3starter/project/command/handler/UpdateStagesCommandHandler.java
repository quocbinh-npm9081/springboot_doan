package vn.eztek.springboot3starter.project.command.handler;


import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.common.redis.RedisMessagePublisher;
import vn.eztek.springboot3starter.common.redis.messages.SocketEventMessage;
import vn.eztek.springboot3starter.domain.stage.entity.Stage;
import vn.eztek.springboot3starter.domain.stage.repository.StageRepository;
import vn.eztek.springboot3starter.domain.task.repository.TaskRepository;
import vn.eztek.springboot3starter.project.command.UpdateStagesCommand;
import vn.eztek.springboot3starter.project.command.event.StagesUpdatedEvent;
import vn.eztek.springboot3starter.project.command.validator.UpdateStagesCommandValidator;
import vn.eztek.springboot3starter.project.mapper.ProjectMapper;
import vn.eztek.springboot3starter.project.response.StageResponse;
import vn.eztek.springboot3starter.project.service.ProjectEventService;
import vn.eztek.springboot3starter.project.vo.ProjectAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;
import vn.eztek.springboot3starter.shared.response.ListResponse;
import vn.eztek.springboot3starter.shared.socket.SocketEventType;
import vn.eztek.springboot3starter.shared.socket.SocketForward;
import vn.eztek.springboot3starter.shared.socket.SocketResponseType;
import vn.eztek.springboot3starter.shared.socket.response.UpdateStageSocketData;

@Component
@RequiredArgsConstructor
public class UpdateStagesCommandHandler implements
    CommandHandler<UpdateStagesCommand, ListResponse<StageResponse>, ProjectAggregateId> {

  private final StageRepository stageRepository;
  private final UpdateStagesCommandValidator validator;
  private final ProjectMapper projectMapper;
  private final ProjectEventService projectEventService;
  private final TaskRepository taskRepository;
  private final RedisMessagePublisher redisMessagePublisher;
  private final Gson gson;

  @Override
  public ListResponse<StageResponse> handle(UpdateStagesCommand command,
      ProjectAggregateId entityId) {
    // validating
    var validated = validator.validate(command);

    // handling
    List<Stage> stageListSave = new ArrayList<>();
    for (int i = 0; i < validated.getStagesPreUpdate().size(); i++) {
      stageListSave.add(projectMapper.mapToStage(validated.getStagesPreUpdate().get(i), i));
    }
    stageListSave = stageRepository.saveAll(stageListSave);

    if (!validated.getDeleteStages().isEmpty()) {
      for (var item : validated.getDeleteStages()) {
        var tasks = taskRepository.findByStageId(item.getId());
        if (!tasks.isEmpty()) {
          int newIndex = item.getOrderNumber() > stageListSave.size() ? stageListSave.size() - 1
              : Math.max(item.getOrderNumber() - 1, 0);
          var newStage = stageListSave.get(newIndex);
          var lastTask = taskRepository.findByStageIdAndNextIdNull(newStage.getId());

          tasks = tasks.stream().peek(task -> {
            if (lastTask != null && task.getPreviousId() == null) {
              lastTask.setNextId(task.getId());
              task.setPreviousId(lastTask.getId());
            }
            task.setStage(newStage);
          }).toList();

          taskRepository.saveAll(tasks);
        }
      }
      // delete stage;
      stageRepository.deleteAll(validated.getDeleteStages());
    }

    var event = StagesUpdatedEvent.eventOf(entityId, command.getProjectId().toString(),
        validated.getUser().getId().toString());

    projectEventService.store(event);

    // sort stage
    Collections.sort(stageListSave);

    var socketForward = SocketForward.create(SocketResponseType.STAGE_UPDATED, gson.toJson(
        UpdateStageSocketData.create(
            stageListSave.stream().map(projectMapper::mapToStageSocketData).toList())));

    redisMessagePublisher.publish(
        SocketEventMessage.create(command.getProjectId().toString(), SocketEventType.PROJECT,
            socketForward));

    var res = stageListSave.stream().map(projectMapper::mapToStageResponse).toList();
    return new ListResponse<>(res);
  }
}
