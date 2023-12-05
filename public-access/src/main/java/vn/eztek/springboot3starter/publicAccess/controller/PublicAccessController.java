package vn.eztek.springboot3starter.publicAccess.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.eztek.springboot3starter.publicAccess.aggregate.PublicAccessAggregate;
import vn.eztek.springboot3starter.publicAccess.command.InvitationMoveOnCommand;
import vn.eztek.springboot3starter.publicAccess.command.InvitationViaLinkCommand;
import vn.eztek.springboot3starter.publicAccess.query.GetProjectByKeyQuery;
import vn.eztek.springboot3starter.publicAccess.request.RegisteringUserViaLinkRequest;
import vn.eztek.springboot3starter.publicAccess.request.RegisteringUserViaMailRequest;
import vn.eztek.springboot3starter.publicAccess.response.InvitationViaLinkResponse;
import vn.eztek.springboot3starter.publicAccess.response.ProjectResponse;


@RestController
@RequestMapping("/api/public-access")
@RequiredArgsConstructor
public class PublicAccessController {

  private final ApplicationContext applicationContext;

  @PostMapping("/invitation-move-on")
  public ResponseEntity<Void> joinProjectViaGmail(
      @Valid @RequestBody RegisteringUserViaMailRequest request) {
    var command = InvitationMoveOnCommand.commandOf(request.getKey(), request.getFirstName(),
        request.getLastName(), request.getPassword(), request.getPhoneNumber(),
        request.getGender());

    var aggregate = new PublicAccessAggregate(applicationContext);
    aggregate.handle(command);

    return ResponseEntity.ok().build();
  }

  @PostMapping("/invitation-via-link")
  public ResponseEntity<InvitationViaLinkResponse> joinProjectViaLink(
      @Valid @RequestBody RegisteringUserViaLinkRequest request) {
    var command = InvitationViaLinkCommand.commandOf(request.getKey(), request.getUsername(),
        request.getFirstName(), request.getLastName(), request.getPassword(),
        request.getPhoneNumber(), request.getGender());

    var aggregate = new PublicAccessAggregate(applicationContext);
    InvitationViaLinkResponse res = aggregate.handle(command);

    return ResponseEntity.ok().body(res);
  }

  @GetMapping("/project-detail")
  public ResponseEntity<ProjectResponse> getProjectDetailByKey(@RequestParam String key) {
    var query = GetProjectByKeyQuery.queryOf(key);

    var aggregate = new PublicAccessAggregate(applicationContext);
    ProjectResponse result = aggregate.handle(query);

    return ResponseEntity.ok().body(result);
  }
}
