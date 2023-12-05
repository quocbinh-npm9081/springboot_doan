package vn.eztek.springboot3starter.shared.cqrs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public abstract class RandomUUID implements ValueObject<RandomUUID> {

  @NotNull(message = "{CANNOT_BE_NULL}")
  @Size(min = 16, max = 50)
  public final String id;

  public RandomUUID() {
    this.id = String.format(getPrefix(), UUID.randomUUID());
  }

  public RandomUUID(String id) {
    this.id = id;
  }

  @Override
  public boolean sameValueAs(RandomUUID other) {
    return other != null && this.id.equals(other.id);
  }

  protected abstract String getPrefix();

}
