package vn.eztek.springboot3starter.user.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import vn.eztek.springboot3starter.domain.role.entity.RoleName;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {RoleNamesAllowedValidator.class, RoleNameAllowedValidator.class})
public @interface RoleNamesAllowed {

  String message() default "field-value-should-be-from-list-of-";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  RoleName[] values();
}
