package vn.eztek.springboot3starter.domain.notification.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import vn.eztek.springboot3starter.domain.shared.Auditable;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "notifications")
@NoArgsConstructor
public class Notification extends Auditable<String> {

  @Column(nullable = false)
  private String notificationType;

  @Column(nullable = false)
  private UUID userId;

  private ZonedDateTime readAt;
  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "metadata", columnDefinition = "jsonb")
  private JsonNode metadata;

  private ZonedDateTime viewAt;
}
