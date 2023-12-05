package vn.eztek.springboot3starter.user.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.eztek.springboot3starter.domain.role.entity.RoleName;
import vn.eztek.springboot3starter.shared.response.ListResponse;
import vn.eztek.springboot3starter.shared.response.PageResponse;
import vn.eztek.springboot3starter.user.aggregate.UserAggregate;
import vn.eztek.springboot3starter.user.annotation.RoleNamesAllowed;
import vn.eztek.springboot3starter.user.command.CreateUserCommand;
import vn.eztek.springboot3starter.user.command.UpdateUserCommand;
import vn.eztek.springboot3starter.user.command.UpdateUserStatusCommand;
import vn.eztek.springboot3starter.user.query.AutoCompleteSearchQuery;
import vn.eztek.springboot3starter.user.query.GetUserByIdQuery;
import vn.eztek.springboot3starter.user.query.ListUsersQuery;
import vn.eztek.springboot3starter.user.request.CreateUserRequest;
import vn.eztek.springboot3starter.user.request.UpdateStatusRequest;
import vn.eztek.springboot3starter.user.request.UpdateUserRequest;
import vn.eztek.springboot3starter.user.response.AutoCompleteSearchResponse;
import vn.eztek.springboot3starter.user.response.UserResponse;
import vn.eztek.springboot3starter.user.specification.UserCriteria;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {

  private final ApplicationContext applicationContext;

  @GetMapping()
  @PreAuthorize("hasRole('VIEW_USER')")
  public ResponseEntity<PageResponse<UserResponse>> list(@Valid UserCriteria criteria,
      Pageable pageable) {
    var query = ListUsersQuery.queryOf(criteria, pageable);

    var aggregate = new UserAggregate(applicationContext);
    PageResponse<UserResponse> result = aggregate.handle(query);

    return ResponseEntity.ok().body(result);
  }

  @GetMapping("/autocomplete")
  public ResponseEntity<List<AutoCompleteSearchResponse>> autoCompleteSearch(
      @RequestParam(required = false) String keyword,
      @RequestParam @RoleNamesAllowed(values = {RoleName.AGENCY, RoleName.TALENT}) RoleName role) {
    var query = AutoCompleteSearchQuery.queryOf(keyword, role);

    var aggregate = new UserAggregate(applicationContext);
    ListResponse<AutoCompleteSearchResponse> res = aggregate.handle(query);

    return ResponseEntity.ok(res.getContent());
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('VIEW_USER')")
  public ResponseEntity<UserResponse> getById(@PathVariable("id") UUID id) {
    var query = GetUserByIdQuery.queryOf(id);

    var aggregate = new UserAggregate(applicationContext);
    UserResponse result = aggregate.handle(query);

    return ResponseEntity.ok().body(result);
  }

  @PostMapping
  @PreAuthorize("hasRole('CREATE_USER')")
  public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest request) {
    var command = CreateUserCommand.commandOf(request.getFirstName(), request.getLastName(),
        request.getUsername(), request.getPhoneNumber(), request.getGender(), request.getPassword(),
        request.getRole(), request.getPrivileges());

    var aggregate = new UserAggregate(applicationContext);
    UserResponse result = aggregate.handle(command);

    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('UPDATE_USER')")
  public ResponseEntity<UserResponse> update(@PathVariable("id") UUID id,
      @Valid @RequestBody UpdateUserRequest request) {
    var command = UpdateUserCommand.commandOf(id, request.getFirstName(), request.getLastName(),
        request.getPhoneNumber(), request.getGender(), request.getRole(), request.getPrivileges());

    var aggregate = new UserAggregate(applicationContext);
    UserResponse result = aggregate.handle(command);

    return ResponseEntity.ok().body(result);
  }

  @PutMapping("/{id}/status")
  @PreAuthorize("hasRole('UPDATE_USER')")
  public ResponseEntity<UserResponse> updateStatus(@PathVariable("id") UUID id,
      @Valid @RequestBody UpdateStatusRequest request) {
    var command = UpdateUserStatusCommand.commandOf(id, request.getStatus());

    var aggregate = new UserAggregate(applicationContext);

    UserResponse result = aggregate.handle(command);
    return ResponseEntity.ok().body(result);
  }

}
