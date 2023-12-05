package vn.eztek.springboot3starter.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestAccessController {

  @GetMapping("/all")
  public String allAccess() {
    return "Public Content.";
  }

  @GetMapping("/manage-users")
  @PreAuthorize("hasRole('MANAGE_USERS')")
  public String manageUsers() {
    return "Manage Users.";
  }

  @GetMapping("/manage-projects")
  @PreAuthorize("hasRole('CREATE_ANY_PROJECTS') or hasRole('VIEW_ALL_PROJECTS')")
  public String manageProjects() {
    return "Manage Projects.";
  }

}