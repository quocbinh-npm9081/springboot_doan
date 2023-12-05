package vn.eztek.springboot3starter.shared.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValuesAllowedValidator.class})
public @interface ValuesAllowed {

  String message() default "field-value-should-be-from-list-of-";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  String[] values();

}
