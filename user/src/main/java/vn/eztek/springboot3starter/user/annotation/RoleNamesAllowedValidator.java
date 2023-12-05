package vn.eztek.springboot3starter.user.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import vn.eztek.springboot3starter.domain.role.entity.RoleName;

public class RoleNamesAllowedValidator implements
    ConstraintValidator<RoleNamesAllowed, List<RoleName>> {

  private List<RoleName> expectedValues;
  private String returnMessage;

  @Override
  public void initialize(RoleNamesAllowed requiredIfChecked) {
    expectedValues = Arrays.asList(requiredIfChecked.values());
    returnMessage = requiredIfChecked.message().concat(expectedValues.toString());
  }

  @Override
  public boolean isValid(List<RoleName> anEnum, ConstraintValidatorContext context) {
    var valid = true;
    if (anEnum == null) {
      return true;
    }
    for (RoleName roleName : anEnum) {
      if (!expectedValues.contains(roleName)) {
        valid = false;
        break;
      }
    }
    if (!valid) {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(returnMessage).addConstraintViolation();
    }
    return valid;
  }


}
