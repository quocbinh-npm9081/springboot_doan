package vn.eztek.springboot3starter.auth.command.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.auth.command.CheckRegisteringUserCommand;
import vn.eztek.springboot3starter.auth.command.event.RegisteringUserCheckedEvent;
import vn.eztek.springboot3starter.auth.command.validator.CheckRegisteringUserCommandValidator;
import vn.eztek.springboot3starter.auth.mapper.AuthMapper;
import vn.eztek.springboot3starter.auth.response.CheckUserResponse;
import vn.eztek.springboot3starter.auth.service.AuthEventStoreService;
import vn.eztek.springboot3starter.auth.vo.AuthAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.CommandHandler;

@Component
@RequiredArgsConstructor
public class CheckRegisteringUserCommandHandler implements
    CommandHandler<CheckRegisteringUserCommand, CheckUserResponse,
        AuthAggregateId> {

  private final CheckRegisteringUserCommandValidator validator;
  private final AuthMapper authMapper;
  private final AuthEventStoreService eventStoreService;

  @Override
  public CheckUserResponse handle(CheckRegisteringUserCommand command, AuthAggregateId entityId) {
    // validating
    var validated = validator.validate(command);

    // event storing
    var event = RegisteringUserCheckedEvent.eventOf(entityId, validated.getIsRegisteringUser(),
        validated.getUserId().toString());
    eventStoreService.store(event);

    // resulting
    return authMapper.mapToCheckUserResponse(validated.getIsRegisteringUser());
  }

}
