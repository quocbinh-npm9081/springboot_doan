package vn.eztek.springboot3starter.project.controller;


import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import vn.eztek.springboot3starter.project.aggregate.ProjectAggregate;
import vn.eztek.springboot3starter.project.command.AddMembersToProjectCommand;
import vn.eztek.springboot3starter.project.command.CreateProjectCommand;
import vn.eztek.springboot3starter.project.command.CreateSharedLinkCommand;
import vn.eztek.springboot3starter.project.command.DeleteProjectCommand;
import vn.eztek.springboot3starter.project.command.DeleteSharedLinkCommand;
import vn.eztek.springboot3starter.project.command.RestoreProjectCommand;
import vn.eztek.springboot3starter.project.command.UpdateProjectCommand;
import vn.eztek.springboot3starter.project.command.UpdateStagesCommand;
import vn.eztek.springboot3starter.project.command.UpdateStatusMemberInProjectCommand;
import vn.eztek.springboot3starter.project.query.GetMyArchiveProjectQuery;
import vn.eztek.springboot3starter.project.query.GetMyProjectQuery;
import vn.eztek.springboot3starter.project.query.GetProjectByIdQuery;
import vn.eztek.springboot3starter.project.query.GetSharedLinkQuery;
import vn.eztek.springboot3starter.project.query.ListProjectMembersQuery;
import vn.eztek.springboot3starter.project.query.ListProjectQuery;
import vn.eztek.springboot3starter.project.query.ListStagesQuery;
import vn.eztek.springboot3starter.project.request.AddMembersToProjectRequest;
import vn.eztek.springboot3starter.project.request.CreateProjectRequest;
import vn.eztek.springboot3starter.project.request.UpdateProjectRequest;
import vn.eztek.springboot3starter.project.request.UpdateStagesRequest;
import vn.eztek.springboot3starter.project.request.UpdateStatusMemberInProjectRequest;
import vn.eztek.springboot3starter.project.response.LinkShareResponse;
import vn.eztek.springboot3starter.project.response.ProjectMemberResponse;
import vn.eztek.springboot3starter.project.response.ProjectResponse;
import vn.eztek.springboot3starter.project.response.StageResponse;
import vn.eztek.springboot3starter.project.specification.ProjectCriteria;
import vn.eztek.springboot3starter.shared.response.ListResponse;
import vn.eztek.springboot3starter.shared.response.PageResponse;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

  private final ApplicationContext applicationContext;

  @PreAuthorize("hasRole('CREATE_PROJECT')")
  @PostMapping()
  public ResponseEntity<ProjectResponse> create(@Valid @RequestBody CreateProjectRequest request) {
    var command = CreateProjectCommand.commandOf(request.getName(), request.getDescription());

    var aggregate = new ProjectAggregate(applicationContext);
    ProjectResponse result = aggregate.handle(command);

    return ResponseEntity.ok(result);
  }

  @GetMapping("/my")
  @PreAuthorize("hasRole('VIEW_MY_PROJECT') OR hasRole('VIEW_PROJECT')")
  public ResponseEntity<List<ProjectResponse>> getMyProject() {
    var query = GetMyProjectQuery.queryOf();

    var aggregate = new ProjectAggregate(applicationContext);
    ListResponse<ProjectResponse> result = aggregate.handle(query);

    return ResponseEntity.ok().body(result.getContent());
  }

  @GetMapping("/my/archive")
  @PreAuthorize("hasRole('VIEW_MY_PROJECT') OR hasRole('VIEW_PROJECT')")
  public ResponseEntity<List<ProjectResponse>> getMyArchiveProject() {
    var query = GetMyArchiveProjectQuery.queryOf();

    var aggregate = new ProjectAggregate(applicationContext);
    ListResponse<ProjectResponse> result = aggregate.handle(query);

    return ResponseEntity.ok().body(result.getContent());
  }

  @GetMapping("/{id}/share")
  @PreAuthorize("hasRole('VIEW_INVITATION')")
  public ResponseEntity<LinkShareResponse> getLinkShare(@PathVariable("id") UUID id) {
    var query = GetSharedLinkQuery.queryOf(id);

    var aggregate = new ProjectAggregate(applicationContext);
    LinkShareResponse result = aggregate.handle(query);

    return ResponseEntity.ok().body(result);
  }

  @PostMapping("/{id}/share")
  @PreAuthorize("hasRole('CREATE_INVITATION')")
  public ResponseEntity<LinkShareResponse> createLinkShare(@PathVariable("id") UUID id) {
    var query = CreateSharedLinkCommand.commandOf(id);

    var aggregate = new ProjectAggregate(applicationContext);
    LinkShareResponse result = aggregate.handle(query);

    return ResponseEntity.ok().body(result);
  }

  @DeleteMapping("/{id}/share")
  @PreAuthorize("hasRole('DELETE_INVITATION')")
  public ResponseEntity<Void> deleteLinkShare(@PathVariable("id") UUID id) {
    var query = DeleteSharedLinkCommand.commandOf(id);

    var aggregate = new ProjectAggregate(applicationContext);
    aggregate.handle(query);

    return ResponseEntity.ok().build();
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('VIEW_MY_PROJECT') OR hasRole('VIEW_PROJECT')")
  public ResponseEntity<ProjectResponse> getById(@PathVariable("id") UUID id) {
    var query = GetProjectByIdQuery.queryOf(id);

    var aggregate = new ProjectAggregate(applicationContext);
    ProjectResponse result = aggregate.handle(query);

    return ResponseEntity.ok().body(result);
  }

  @PostMapping("/{id}/restore")
  @PreAuthorize("hasRole('UPDATE_PROJECT')")
  public ResponseEntity<Void> restoreProject(@PathVariable("id") UUID id) {
    var query = RestoreProjectCommand.commandOf(id);

    var aggregate = new ProjectAggregate(applicationContext);
    aggregate.handle(query);

    return ResponseEntity.ok().build();
  }

  @PreAuthorize("hasRole('UPDATE_PROJECT')")
  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<ProjectResponse> update(@PathVariable("id") UUID id,
      @Valid @RequestBody UpdateProjectRequest request) {
    var command = UpdateProjectCommand.commandOf(id, request.getName(), request.getDescription(),
        request.getStatus());

    var aggregate = new ProjectAggregate(applicationContext);
    ProjectResponse res = aggregate.handle(command);

    return ResponseEntity.ok(res);
  }

  @PreAuthorize("hasRole('UPDATE_PROJECT')")
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
    var command = DeleteProjectCommand.commandOf(id);

    var aggregate = new ProjectAggregate(applicationContext);
    aggregate.handle(command);

    return ResponseEntity.ok().build();
  }

  @GetMapping()
  @PreAuthorize("hasRole('VIEW_PROJECT')")
  public ResponseEntity<PageResponse<ProjectResponse>> getProjects(@Valid ProjectCriteria criteria,
      Pageable pageable) {
    var query = ListProjectQuery.queryOf(criteria, pageable);

    var aggregate = new ProjectAggregate(applicationContext);
    PageResponse<ProjectResponse> result = aggregate.handle(query);

    return ResponseEntity.ok().body(result);
  }

  @PutMapping("/{id}/stages")
  @PreAuthorize("hasRole('UPDATE_PROJECT')")
  public ResponseEntity<List<StageResponse>> updateStages(@PathVariable UUID id,
      @Valid @RequestBody UpdateStagesRequest request) {
    var command = UpdateStagesCommand.commandOf(id, request.getStages());

    var aggregate = new ProjectAggregate(applicationContext);
    ListResponse<StageResponse> result = aggregate.handle(command);

    return ResponseEntity.ok(result.getContent());
  }

  @GetMapping("/{id}/stages")
  @PreAuthorize("hasRole('VIEW_MY_PROJECT') OR hasRole('VIEW_PROJECT')")
  public ResponseEntity<List<StageResponse>> getStages(@PathVariable UUID id) {
    var query = ListStagesQuery.queryOf(id);

    var aggregate = new ProjectAggregate(applicationContext);
    ListResponse<StageResponse> result = aggregate.handle(query);

    return ResponseEntity.ok(result.getContent());
  }

  @PostMapping("/{id}/members")
  @PreAuthorize("hasRole('UPDATE_PROJECT')")
  public ResponseEntity<Void> inviteMemberToProject(@PathVariable UUID id,
      @Valid @RequestBody AddMembersToProjectRequest request) {
    var query = AddMembersToProjectCommand.commandOf(id, request.getEmails());

    var aggregate = new ProjectAggregate(applicationContext);
    aggregate.handle(query);

    return ResponseEntity.ok().build();
  }

  @GetMapping("/{id}/members")
  @PreAuthorize("hasRole('VIEW_PROJECT') OR hasRole('VIEW_MY_PROJECT')")
  public ResponseEntity<List<ProjectMemberResponse>> getProjectMembers(@PathVariable UUID id) {
    var query = ListProjectMembersQuery.queryOf(id);

    var aggregate = new ProjectAggregate(applicationContext);
    ListResponse<ProjectMemberResponse> res = aggregate.handle(query);

    return ResponseEntity.ok(res.getContent());
  }

  @PutMapping("/{id}/members/{memberId}/status")
  @PreAuthorize("hasRole('UPDATE_PROJECT') ")
  public ResponseEntity<Void> updateStatusMemberInProject(@PathVariable UUID id,
      @PathVariable UUID memberId, @Valid @RequestBody UpdateStatusMemberInProjectRequest request) {
    var query = UpdateStatusMemberInProjectCommand.commandOf(id, memberId, request.getStatus());

    var aggregate = new ProjectAggregate(applicationContext);
    aggregate.handle(query);

    return ResponseEntity.ok().build();
  }
}
