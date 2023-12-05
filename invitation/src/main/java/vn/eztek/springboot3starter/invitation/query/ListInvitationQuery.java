package vn.eztek.springboot3starter.invitation.query;

import lombok.Value;
import vn.eztek.springboot3starter.shared.cqrs.Query;

@Value(staticConstructor = "queryOf")
public class ListInvitationQuery implements Query {

}
