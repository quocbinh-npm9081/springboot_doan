package vn.eztek.springboot3starter.shared.socket.response;

import java.util.UUID;
import lombok.Data;

@Data
public class UserSocketData {

  UUID id;
  String firstName;
  String lastName;
  String email;
  String phoneNumber;
}
