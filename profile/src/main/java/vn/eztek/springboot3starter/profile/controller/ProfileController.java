package vn.eztek.springboot3starter.profile.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.eztek.springboot3starter.profile.aggregate.ProfileAggregate;
import vn.eztek.springboot3starter.profile.command.ChangePasswordCommand;
import vn.eztek.springboot3starter.profile.command.DeleteProfileCommand;
import vn.eztek.springboot3starter.profile.command.DeactivateProfileCommand;
import vn.eztek.springboot3starter.profile.command.RequestChangeEmailCommand;
import vn.eztek.springboot3starter.profile.command.UpdateProfileCommand;
import vn.eztek.springboot3starter.profile.query.GetMyProfileQuery;
import vn.eztek.springboot3starter.profile.request.UpdatePasswordRequest;
import vn.eztek.springboot3starter.profile.request.UpdateProfileRequest;
import vn.eztek.springboot3starter.profile.response.ProfileResponse;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

  private final ApplicationContext applicationContext;

  @GetMapping()
  @PreAuthorize("hasRole('VIEW_PROFILE')")
  public ResponseEntity<ProfileResponse> getMyProfile() {
    var query = GetMyProfileQuery.queryOf(
        SecurityContextHolder.getContext().getAuthentication().getName());
    var aggregate = new ProfileAggregate(applicationContext);
    ProfileResponse result = aggregate.handle(query);
    return ResponseEntity.ok().body(result);
  }

  @PostMapping("/change-password")
  @PreAuthorize("hasRole('UPDATE_PROFILE')")
  public ResponseEntity<Void> changePassword(@Valid @RequestBody UpdatePasswordRequest request) {
    var command = ChangePasswordCommand.commandOf(request.getOldPassword(),
        request.getNewPassword());
    var aggregate = new ProfileAggregate(applicationContext);
    aggregate.handle(command);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/deactivate-account")
  @PreAuthorize("hasRole('UPDATE_PROFILE')")
  public ResponseEntity<Void> deactivateAccount() {
    var command = DeactivateProfileCommand.commandOf();
    var aggregate = new ProfileAggregate(applicationContext);
    aggregate.handle(command);
    return ResponseEntity.ok().build();
  }

  @PutMapping()
  @PreAuthorize("hasRole('UPDATE_PROFILE')")
  public ResponseEntity<ProfileResponse> update(@Valid @RequestBody UpdateProfileRequest request) {
    var command = UpdateProfileCommand.commandOf(request.getFirstName(), request.getLastName(),
        request.getPhoneNumber(), request.getGender());
    var aggregate = new ProfileAggregate(applicationContext);
    ProfileResponse result = aggregate.handle(command);
    return ResponseEntity.ok().body(result);
  }

  @PostMapping("/request-change-email")
  @PreAuthorize("hasRole('UPDATE_PROFILE')")
  public ResponseEntity<Void> changeEmail() {
    var command = RequestChangeEmailCommand.commandOf();

    var aggregate = new ProfileAggregate(applicationContext);
    aggregate.handle(command);

    return ResponseEntity.ok().build();
  }

  @DeleteMapping()
  @PreAuthorize("hasRole('UPDATE_PROFILE')")
  public ResponseEntity<Void> deleteAccount() {
    var command = DeleteProfileCommand.commandOf();
    var aggregate = new ProfileAggregate(applicationContext);
    aggregate.handle(command);
    return ResponseEntity.ok().build();
  }

}
