package vn.eztek.springboot3starter.domain.comment.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.eztek.springboot3starter.domain.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, UUID> {

  List<Comment> findByParentCommentId(UUID parentId);
  List<Comment> findAllByTaskIdAndParentCommentIdNull(UUID taskId);
  List<Comment> findAllByTaskId(UUID taskId);
}
