package vn.eztek.springboot3starter.common.socket.impl;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import vn.eztek.springboot3starter.common.socket.MessageService;
import vn.eztek.springboot3starter.shared.socket.*;
import vn.eztek.springboot3starter.shared.socket.response.*;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

  private final SimpMessagingTemplate simpMessagingTemplate;
  private final Gson gson;

  String TOPIC_TASKS = "/topic/tasks/%s";
  String TOPIC_NOTIFICATIONS = "/topic/users/%s/notifications";
  String TOPIC_PROJECTS = "/topic/projects/%s";

  public void sendEvent(String id, SocketEventType type, SocketForward socket) {
    SocketResponse response;
    switch (socket.getType()) {
      case TASK_ASSIGNED -> response = SocketResponse.create(SocketResponseType.TASK_ASSIGNED,
              gson.fromJson(socket.getData(), AssignTaskSocketData.class));

      case TASK_UNASSIGNED -> response = SocketResponse.create(SocketResponseType.TASK_UNASSIGNED,
              gson.fromJson(socket.getData(), UnAssignTaskSocketData.class));

      case STAGE_UPDATED -> response = SocketResponse.create(SocketResponseType.STAGE_UPDATED,
              gson.fromJson(socket.getData(), UpdateStageSocketData.class));

      case TASK_UPDATED -> response = SocketResponse.create(SocketResponseType.TASK_UPDATED,
              gson.fromJson(socket.getData(), UpdateTaskSocketData.class));

      case TASK_DELETED -> response = SocketResponse.create(SocketResponseType.TASK_DELETED,
              gson.fromJson(socket.getData(), DeleteTaskSocketData.class));

      case TASK_POSITION_UPDATED -> response = SocketResponse.create(SocketResponseType.TASK_POSITION_UPDATED,
              gson.fromJson(socket.getData(), ListTaskSocketData.class));

      case TASK_ADDED -> response = SocketResponse.create(SocketResponseType.TASK_ADDED,
              gson.fromJson(socket.getData(), CreateTaskSocketData.class));

      case COMMENT_ADDED -> response = SocketResponse.create(SocketResponseType.COMMENT_ADDED,
              gson.fromJson(socket.getData(), CommentSocketData.class));

      case COMMENT_DELETED -> response = SocketResponse.create(SocketResponseType.COMMENT_DELETED,
              gson.fromJson(socket.getData(), DeleteCommentSocketData.class));

      case COMMENT_UPDATED -> response = SocketResponse.create(SocketResponseType.COMMENT_UPDATED,
              gson.fromJson(socket.getData(), CommentSocketData.class));

      case ATTACHMENT_DELETED -> response = SocketResponse.create(SocketResponseType.ATTACHMENT_DELETED,
              gson.fromJson(socket.getData(), DeleteAttachmentSocketData.class));

      case ATTACHMENT_UPDATED -> response = SocketResponse.create(SocketResponseType.ATTACHMENT_UPDATED,
              gson.fromJson(socket.getData(), UpdateAttachmentSocketData.class));

      case CHECK_LIST_ADDED -> response = SocketResponse.create(SocketResponseType.CHECK_LIST_ADDED,
              gson.fromJson(socket.getData(), CheckListSocketData.class));

      case CHECK_LIST_UPDATED -> response = SocketResponse.create(SocketResponseType.CHECK_LIST_UPDATED,
              gson.fromJson(socket.getData(), CheckListSocketData.class));

      case CHECK_LIST_DELETED -> response = SocketResponse.create(SocketResponseType.CHECK_LIST_DELETED,
              gson.fromJson(socket.getData(), DeleteCheckListSocketData.class));

      case CHECK_LIST_ITEM_ADDED -> response = SocketResponse.create(SocketResponseType.CHECK_LIST_ITEM_ADDED,
              gson.fromJson(socket.getData(), CheckListItemSocketData.class));

      case CHECK_LIST_ITEM_UPDATED -> response = SocketResponse.create(SocketResponseType.CHECK_LIST_ITEM_UPDATED,
              gson.fromJson(socket.getData(), CheckListItemSocketData.class));

      case CHECK_LIST_ITEM_DELETED -> response = SocketResponse.create(SocketResponseType.CHECK_LIST_ITEM_DELETED,
              gson.fromJson(socket.getData(), DeleteCheckListItemSocketData.class));

      case CHECK_LIST_ITEM_ASSIGNED -> response = SocketResponse.create(SocketResponseType.CHECK_LIST_ITEM_ASSIGNED,
              gson.fromJson(socket.getData(), AssignCheckListItemSocketData.class));

      case CHECK_LIST_ITEM_UNASSIGNED -> response = SocketResponse.create(SocketResponseType.CHECK_LIST_ITEM_UNASSIGNED,
              gson.fromJson(socket.getData(), UnAssignCheckListItemSocketData.class));

      case CHECK_LIST_ITEM_DUE_DATE_UPDATED ->
              response = SocketResponse.create(SocketResponseType.CHECK_LIST_ITEM_DUE_DATE_UPDATED,
                      gson.fromJson(socket.getData(), CheckListItemDueDateSocketData.class));

      case CHECK_LIST_ITEM_STATUS_UPDATED ->
              response = SocketResponse.create(SocketResponseType.CHECK_LIST_ITEM_STATUS_UPDATED,
                      gson.fromJson(socket.getData(), CheckListItemStatusDateSocketData.class));

      case LABEL_ADDED -> response = SocketResponse.create(SocketResponseType.LABEL_ADDED,
              gson.fromJson(socket.getData(), LabelSocketData.class));

      case LABEL_UPDATED -> response = SocketResponse.create(SocketResponseType.LABEL_UPDATED,
              gson.fromJson(socket.getData(), LabelSocketData.class));

      case LABEL_DELETED -> response = SocketResponse.create(SocketResponseType.LABEL_DELETED,
              gson.fromJson(socket.getData(), LabelSocketData.class));

      case TASK_LABEL_ADDED -> response = SocketResponse.create(SocketResponseType.TASK_LABEL_ADDED,
              gson.fromJson(socket.getData(), AssignLabelInTaskSocketData.class));

      case TASK_LABEL_REMOVED -> response = SocketResponse.create(SocketResponseType.TASK_LABEL_REMOVED,
              gson.fromJson(socket.getData(), UnAssignLabelInTaskSocketData.class));

      case LABEL_COLOR_REMOVED -> response = SocketResponse.create(SocketResponseType.LABEL_COLOR_REMOVED,
              gson.fromJson(socket.getData(), LabelSocketData.class));

      default -> response = SocketResponse.create(SocketResponseType.ATTACHMENT_ADDED,
              gson.fromJson(socket.getData(), AttachmentSocketData.class));
    }

    if (type.equals(SocketEventType.TASK)) {
      simpMessagingTemplate.convertAndSend(TOPIC_TASKS.formatted(id), response);
    } else {
      simpMessagingTemplate.convertAndSend(TOPIC_PROJECTS.formatted(id), response);
    }
  }

  @Override
  public void sendEvent(String id, SocketResponseType type, NotificationResponse response) {
    simpMessagingTemplate.convertAndSend(TOPIC_NOTIFICATIONS.formatted(id),
            SocketResponse.create(type, response));
  }
}
