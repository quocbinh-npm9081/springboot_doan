package vn.eztek.springboot3starter.task.command.validated.comment;

import lombok.Value;
import vn.eztek.springboot3starter.domain.comment.entity.Comment;
import vn.eztek.springboot3starter.domain.task.entity.Task;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.shared.cqrs.CommandValidated;

@Value(staticConstructor = "validatedOf")
public class UpdateCommentCommandValidated implements CommandValidated {

  User loggedInUser;
  Task task;
  Comment comment;
}
