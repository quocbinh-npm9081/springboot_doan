package vn.eztek.springboot3starter.invitation.controller;


import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import vn.eztek.springboot3starter.invitation.aggregate.InvitationAggregate;
import vn.eztek.springboot3starter.invitation.command.CheckMemberProjectCommand;
import vn.eztek.springboot3starter.invitation.command.CheckUserMatchInviteCommand;
import vn.eztek.springboot3starter.invitation.command.ConfirmInviteCommand;
import vn.eztek.springboot3starter.invitation.command.InviteTalentCommand;
import vn.eztek.springboot3starter.invitation.command.ResponseInviteCommand;
import vn.eztek.springboot3starter.invitation.query.ListInvitationQuery;
import vn.eztek.springboot3starter.invitation.request.CheckMemberProjectRequest;
import vn.eztek.springboot3starter.invitation.request.CheckUserMatchInvitationRequest;
import vn.eztek.springboot3starter.invitation.request.ConfirmInviteRequest;
import vn.eztek.springboot3starter.invitation.request.ResponseInviteRequest;
import vn.eztek.springboot3starter.invitation.request.TalentInviteRequest;
import vn.eztek.springboot3starter.invitation.response.CheckMemberProjectResponse;
import vn.eztek.springboot3starter.invitation.response.CheckUserMatchInviteResponse;
import vn.eztek.springboot3starter.invitation.response.ConfirmInviteResponse;
import vn.eztek.springboot3starter.invitation.response.InvitationResponse;
import vn.eztek.springboot3starter.shared.response.ListResponse;

@RestController
@RequestMapping("/api/invitations")
@RequiredArgsConstructor
public class InvitationController {

  private final ApplicationContext applicationContext;


  @PostMapping()
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('CREATE_INVITATION')")
  public ResponseEntity<Void> inviteTalents(@Valid @RequestBody TalentInviteRequest request) {
    var command = InviteTalentCommand.commandOf(request.getId(), request.getEmails());

    var aggregate = new InvitationAggregate(applicationContext);
    aggregate.handle(command);

    return ResponseEntity.ok().build();

  }

  @PostMapping("/{id}/response")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('UPDATE_INVITATION')")
  public ResponseEntity<Void> responseInvitation(@PathVariable("id") UUID id,
      @Valid @RequestBody ResponseInviteRequest request) {
    var command = ResponseInviteCommand.commandOf(id, request.getAccept());

    var aggregate = new InvitationAggregate(applicationContext);
    aggregate.handle(command);

    return ResponseEntity.ok().build();

  }

  @PostMapping("/confirm-link")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('UPDATE_INVITATION')")
  public ResponseEntity<ConfirmInviteResponse> confirmInvitation(
      @Valid @RequestBody ConfirmInviteRequest request) {
    var command = ConfirmInviteCommand.commandOf(request.getKey());

    var aggregate = new InvitationAggregate(applicationContext);
    ConfirmInviteResponse res = aggregate.handle(command);

    return ResponseEntity.ok().body(res);

  }

  @PostMapping("/check-user")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('VIEW_INVITATION') or hasRole('VIEW_MY_INVITATION')")
  public ResponseEntity<CheckUserMatchInviteResponse> checkUserMatchInvitation(
      @Valid @RequestBody CheckUserMatchInvitationRequest request) {
    var command = CheckUserMatchInviteCommand.commandOf(request.getKey());

    var aggregate = new InvitationAggregate(applicationContext);
    CheckUserMatchInviteResponse result = aggregate.handle(command);

    return ResponseEntity.ok(result);
  }

  @PostMapping("/check-member-project")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('VIEW_MY_PROJECT')")
  public ResponseEntity<CheckMemberProjectResponse> checkMemberProject(
      @Valid @RequestBody CheckMemberProjectRequest request) {
    var command = CheckMemberProjectCommand.commandOf(request.getKey());

    var aggregate = new InvitationAggregate(applicationContext);
    CheckMemberProjectResponse result = aggregate.handle(command);

    return ResponseEntity.ok(result);
  }

  @GetMapping("/my")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('VIEW_INVITATION') or hasRole('VIEW_MY_INVITATION')")
  public ResponseEntity<List<InvitationResponse>> getListInvitation() {
    var command = ListInvitationQuery.queryOf();

    var aggregate = new InvitationAggregate(applicationContext);
    ListResponse<InvitationResponse> result = aggregate.handle(command);

    return ResponseEntity.ok(result.getContent());

  }
}
