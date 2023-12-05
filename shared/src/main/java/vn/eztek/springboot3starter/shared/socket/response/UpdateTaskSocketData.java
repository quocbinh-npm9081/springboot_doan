package vn.eztek.springboot3starter.shared.socket.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.eztek.springboot3starter.shared.socket.SocketResponseData;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTaskSocketData implements SocketResponseData {

  private String title;
  private String description;
  private Boolean hasDescription;

  public void setDescription(String description) {
    this.description = description;
    this.hasDescription = description != null && !description.isBlank();
  }
}
