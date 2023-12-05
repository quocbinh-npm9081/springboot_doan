package vn.eztek.springboot3starter.auth.response;

import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import vn.eztek.springboot3starter.domain.role.entity.RoleName;
import vn.eztek.springboot3starter.shared.cqrs.CommandResult;

@Getter
@Setter
public class JwtResponse implements CommandResult {

  private String accessToken;
  private String refreshToken;
  private String type = "Bearer";
  private UUID id;
  private String firstName;
  private String lastName;
  private String username;
  private RoleName role;
  private List<String> authorities;

  public JwtResponse(String accessToken, String refreshToken, UUID id, String firstName,
      String lastName, String username, RoleName role,
      List<String> authorities) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.id = id;
    this.role = role;
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
    this.authorities = authorities;

  }

}
