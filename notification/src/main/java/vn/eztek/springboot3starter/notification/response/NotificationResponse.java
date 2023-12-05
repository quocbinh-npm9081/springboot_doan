package vn.eztek.springboot3starter.notification.response;


import com.fasterxml.jackson.databind.JsonNode;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.cqrs.CommandResult;
import vn.eztek.springboot3starter.shared.cqrs.QueryResult;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class NotificationResponse implements CommandResult, QueryResult {

  private UUID id;
  private String notificationType;
  private ZonedDateTime readAt;
  private ZonedDateTime createdDate;
  private String createdBy;
  private UUID userId;
  private ZonedDateTime lastModifiedDate;
  private String lastModifiedBy;
  private JsonNode metadata;
}
