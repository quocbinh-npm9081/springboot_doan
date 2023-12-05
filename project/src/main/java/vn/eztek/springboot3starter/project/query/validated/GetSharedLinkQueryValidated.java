package vn.eztek.springboot3starter.project.query.validated;

import java.util.UUID;
import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.QueryValidated;

@Value(staticConstructor = "validatedOf")
public class GetSharedLinkQueryValidated implements QueryValidated {

  UUID userId;

}
