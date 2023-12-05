package vn.eztek.springboot3starter.user.specification;

import jakarta.persistence.criteria.Join;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import vn.eztek.springboot3starter.domain.role.entity.Role;
import vn.eztek.springboot3starter.domain.role.entity.RoleName;
import vn.eztek.springboot3starter.domain.user.entity.User;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.user.query.AutoCompleteSearchQuery;


public class UserSpecifications {

  public static Specification<User> getFilter(UserCriteria criteria) {
    Specification<User> specification = Specification.where(null);
    List<RoleName> allowedRoles = List.of(RoleName.AGENCY, RoleName.PROJECT_MANAGER);

    if (criteria.getStatuses() != null) {
      specification = specification.and(statusIn(criteria.getStatuses()));
    }
    if (criteria.getKeyword() != null) {
      var nameSpecification = usernameContains(criteria.getKeyword()).or(
          firstNameContains(criteria.getKeyword()).or(lastNameContains(criteria.getKeyword())));
      specification = specification.and(nameSpecification);
    }

    var roles = criteria.getRoles() == null ? allowedRoles : criteria.getRoles();
    specification = specification.and(hasRoles(roles));

    return specification;
  }

  public static Specification<User> getFilter(AutoCompleteSearchQuery query) {
    // add filter name
    Specification<User> specification = Specification.where(null);
    var nameSpecification = usernameContains(query.getName()).or(
        firstNameContains(query.getName()).or(lastNameContains(query.getName())));
    specification = specification.and(nameSpecification);
    // add filter is not deleted
    specification = specification.and(deletedNull());
    // add filter role
    specification = specification.and(hasRole(query.getRoleName()));
    return specification;


  }

  static Specification<User> statusIn(List<UserStatus> statuses) {
    return (user, cq, cb) -> user.get("status").in(statuses);
  }

  static Specification<User> deletedNull() {
    return (user, cq, cb) -> user.get("deletedAt").isNull();
  }

  static Specification<User> usernameContains(String keyword) {
    return (user, cq, cb) -> cb.like(cb.lower(user.get("username")),
        "%" + keyword.toLowerCase() + "%");
  }

  static Specification<User> firstNameContains(String keyword) {
    return (user, cq, cb) -> cb.like(cb.lower(user.get("firstName")),
        "%" + keyword.toLowerCase() + "%");
  }

  static Specification<User> lastNameContains(String keyword) {
    return (user, cq, cb) -> cb.like(cb.lower(user.get("lastName")),
        "%" + keyword.toLowerCase() + "%");
  }

  static Specification<User> hasRoles(List<RoleName> roles) {
    return (root, query, cb) -> {
      Join<Role, User> join = root.join("role");
      return join.get("name").in(roles);
    };
  }

  static Specification<User> hasRole(RoleName role) {
    return (root, query, cb) -> {
      Join<Role, User> join = root.join("role");
      return join.get("name").in(role);
    };
  }


}
