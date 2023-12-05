package vn.eztek.springboot3starter.domain.event.entity;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "eventStore")
@Getter
@Setter
public class EventStore implements Serializable {

  public static final String CREATED_DATE = "createdDate";

  @Serial
  private static final long serialVersionUID = 1L;

  @MongoId(FieldType.OBJECT_ID)
  private ObjectId id;

  @Field("id")
  private String eventId;

  private String userId;

  @Enumerated(EnumType.STRING)
  private EventAction action;

  private String eventData;

  @CreatedDate
  private Long createdDate;

  @LastModifiedDate
  private Long modifiedDate;

}
