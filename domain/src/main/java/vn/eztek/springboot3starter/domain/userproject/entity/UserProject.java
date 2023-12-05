package vn.eztek.springboot3starter.domain.userproject.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import vn.eztek.springboot3starter.domain.project.entity.Project;
import vn.eztek.springboot3starter.domain.shared.BaseEntity;
import vn.eztek.springboot3starter.domain.user.entity.User;

@Entity(name = "user_project")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class UserProject extends BaseEntity {

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  UserProjectStatus status;

  @OneToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "user_id")
  User user;

  @OneToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "project_id")
  Project project;

}
