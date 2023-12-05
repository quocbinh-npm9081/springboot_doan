package vn.eztek.springboot3starter.notification.aggregate;

import org.springframework.context.ApplicationContext;
import vn.eztek.springboot3starter.notification.command.MarkAsReadNotificationCommand;
import vn.eztek.springboot3starter.notification.command.MarkAsViewNotificationCommand;
import vn.eztek.springboot3starter.notification.command.handler.MarkAsReadNotificationCommandHandler;
import vn.eztek.springboot3starter.notification.command.handler.MarkAsViewNotificationCommandHandler;
import vn.eztek.springboot3starter.notification.query.CountUnreadNotificationQuery;
import vn.eztek.springboot3starter.notification.query.ListNotificationQuery;
import vn.eztek.springboot3starter.notification.query.handler.CountUnreadNotificationQueryHandler;
import vn.eztek.springboot3starter.notification.query.handler.ListNotificationQueryHandler;
import vn.eztek.springboot3starter.notification.vo.NotificationAggregateId;
import vn.eztek.springboot3starter.shared.cqrs.AggregateRoot;

public class NotificationAggregate extends
        AggregateRoot<NotificationAggregate, NotificationAggregateId> {

    public NotificationAggregate(ApplicationContext applicationContext) {
        super(applicationContext, new NotificationAggregateId());
    }

    @Override
    protected AggregateRootBehavior initialBehavior() {
        var behaviorBuilder = new AggregateRootBehaviorBuilder();
        behaviorBuilder.setCommandHandler(MarkAsReadNotificationCommand.class,
                getCommandHandler(MarkAsReadNotificationCommandHandler.class));
        behaviorBuilder.setCommandHandler(MarkAsViewNotificationCommand.class,
                getCommandHandler(MarkAsViewNotificationCommandHandler.class));

        behaviorBuilder.setQueryHandler(ListNotificationQuery.class,
                getQueryHandler(ListNotificationQueryHandler.class));
        behaviorBuilder.setQueryHandler(CountUnreadNotificationQuery.class,
                getQueryHandler(CountUnreadNotificationQueryHandler.class));
        return behaviorBuilder.build();
    }

    @Override
    public boolean sameIdentityAs(NotificationAggregate other) {
        return other != null && entityId.sameValueAs(other.entityId);
    }

    @Override
    public NotificationAggregateId id() {
        return entityId;
    }
}
