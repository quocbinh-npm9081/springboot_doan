package vn.eztek.springboot3starter.user.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import vn.eztek.springboot3starter.domain.role.entity.RoleName;

public class RoleNameAllowedValidator implements ConstraintValidator<RoleNamesAllowed, RoleName> {

  private List<RoleName> expectedValues;
  private String returnMessage;

  @Override
  public void initialize(RoleNamesAllowed requiredIfChecked) {
    expectedValues = Arrays.asList(requiredIfChecked.values());
    returnMessage = requiredIfChecked.message().concat(expectedValues.toString());
  }

  @Override
  public boolean isValid(RoleName anEnum, ConstraintValidatorContext context) {
    var valid = expectedValues.contains(anEnum);
    if (!valid) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(returnMessage).addConstraintViolation();
    }
    return valid;
  }

}
