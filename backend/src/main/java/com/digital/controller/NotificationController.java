package com.digital.controller;


import com.digital.dto.NotificationRequestDTO;
import com.digital.entities.Notification;
import com.digital.entities.User;
import com.digital.service.NotificationServiceImpl;
import com.digital.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(value = "http://localhost:4200")
@RequiredArgsConstructor

public class NotificationController {

    private final NotificationServiceImpl notificationService;
    private final UserServiceImpl userService;


    @GetMapping("/notifications")
    public ResponseEntity<List<Notification>> getUserNotifications(Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());
        List<Notification> notifications = notificationService.getAllNotificationsByUser(user);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("notifications/unread")
    public ResponseEntity<Notification> getUnreadNotifications(Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());
        List<Notification> unread = notificationService.getUnReadNotificationByUser(user);
        if (unread.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(unread.getFirst());
        }
    }

    @PutMapping("/notifications/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/notifications/read-all")
    public ResponseEntity<Void> markAllAsRead(Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());
        notificationService.markAllAsRead(user);
        return ResponseEntity.noContent().build();
    }




    @PostMapping("/notifications/test-create")
    public ResponseEntity<Notification> createTestNotification(
            @RequestBody NotificationRequestDTO request,
            Authentication authentication) {

        User user = userService.findByEmail(authentication.getName());

        Notification notification = notificationService.createAndSaveNotification(
                request.getTitle(),
                request.getMessage(),
                user,
                Notification.NotificationType.valueOf(request.getType())
        );

        return ResponseEntity.ok(notification);
    }




}
