package vn.eztek.springboot3starter.notification.query.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.eztek.springboot3starter.domain.user.entity.UserStatus;
import vn.eztek.springboot3starter.domain.user.repository.UserRepository;
import vn.eztek.springboot3starter.notification.query.CountUnreadNotificationQuery;
import vn.eztek.springboot3starter.notification.query.validated.CountUnreadNotificationQueryValidated;
import vn.eztek.springboot3starter.shared.cqrs.QueryValidation;
import vn.eztek.springboot3starter.shared.exception.BadRequestException;
import vn.eztek.springboot3starter.shared.exception.NotFoundException;

@Component
@RequiredArgsConstructor
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CountUnreadNotificationQueryValidator extends
        QueryValidation<CountUnreadNotificationQuery, CountUnreadNotificationQueryValidated> {
    private final UserRepository userRepository;

    @Override
    public CountUnreadNotificationQueryValidated validate(CountUnreadNotificationQuery query) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = userRepository.findByUsernameIgnoreCaseAndDeletedAtNull(authentication.getName())
                .orElseThrow(() -> new NotFoundException("user-not-found"));
        if (user.getStatus().equals(UserStatus.INACTIVE)) {
            throw new BadRequestException("user-is-not-active");
        }
        return CountUnreadNotificationQueryValidated.validatedOf(user);
    }
}
