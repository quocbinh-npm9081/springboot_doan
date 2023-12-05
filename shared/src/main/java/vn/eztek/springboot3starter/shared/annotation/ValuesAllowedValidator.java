package vn.eztek.springboot3starter.shared.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class ValuesAllowedValidator implements ConstraintValidator<ValuesAllowed, List<String>> {

  private List<String> expectedValues;
  private String returnMessage;

  @Override
  public void initialize(ValuesAllowed requiredIfChecked) {
    expectedValues = Arrays.asList(requiredIfChecked.values());
    returnMessage = requiredIfChecked.message().concat(expectedValues.toString());
  }

  @Override
  public boolean isValid(List<String> testValue, ConstraintValidatorContext context) {
    var valid = true;
    if (testValue == null) {
      return true;
    }
    for (String val : testValue) {
      if (!expectedValues.contains(val)) {
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
