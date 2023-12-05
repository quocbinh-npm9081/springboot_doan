package vn.eztek.springboot3starter.notification.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.eztek.springboot3starter.notification.aggregate.NotificationAggregate;
import vn.eztek.springboot3starter.notification.command.MarkAsReadNotificationCommand;
import vn.eztek.springboot3starter.notification.command.MarkAsViewNotificationCommand;
import vn.eztek.springboot3starter.notification.query.CountUnreadNotificationQuery;
import vn.eztek.springboot3starter.notification.query.ListNotificationQuery;
import vn.eztek.springboot3starter.notification.request.MarkAsReadNotificationRequest;
import vn.eztek.springboot3starter.notification.response.CountNotificationResponse;
import vn.eztek.springboot3starter.notification.response.NotificationResponse;
import vn.eztek.springboot3starter.shared.response.PageResponse;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final ApplicationContext applicationContext;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PageResponse<NotificationResponse>> getListInvitation(Pageable pageable) {
        var command = ListNotificationQuery.queryOf(pageable);

        var aggregate = new NotificationAggregate(applicationContext);
        PageResponse<NotificationResponse> result = aggregate.handle(command);

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @PostMapping("/mark-as-read")
    public ResponseEntity<Void> markAsRead(
            @Valid @RequestBody MarkAsReadNotificationRequest request) {
        var command = MarkAsReadNotificationCommand.commandOf(request.getNotificationIds());
        var aggregate = new NotificationAggregate(applicationContext);
        aggregate.handle(command);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/mark-as-view")
    public ResponseEntity<Void> markAsView() {
        var command = MarkAsViewNotificationCommand.commandOf();
        var aggregate = new NotificationAggregate(applicationContext);
        aggregate.handle(command);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/count-unread")
    public ResponseEntity<CountNotificationResponse> countUnread() {
        var query = CountUnreadNotificationQuery.queryOf();
        var aggregate = new NotificationAggregate(applicationContext);
        CountNotificationResponse result = aggregate.handle(query);
        return ResponseEntity.ok().body(result);
    }

}
