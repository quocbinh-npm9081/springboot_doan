package vn.eztek.springboot3starter.domain.comment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.eztek.springboot3starter.domain.shared.Auditable;
import vn.eztek.springboot3starter.domain.user.entity.User;

@Entity(name = "comments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends Auditable<String> {

  @Column(nullable = false)
  private String content;

  @Column(nullable = false)
  private UUID taskId;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Column(nullable = false)
  private UUID parentCommentId;

  @Column
  private Integer countReply = 0;

}
