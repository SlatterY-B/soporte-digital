package com.digital.service;

import com.digital.entities.Notification;
import com.digital.entities.User;

import java.util.List;

public interface NotificationService {

    Notification createNotification(String title, String message, User user, Notification.NotificationType type);

    List<Notification> getAllNotificationsByUser(User user);

    List<Notification> getUnReadNotificationByUser(User user);

    void markAsRead(Long notificationId);

    void markAllAsRead(User user);

    Notification createAndSaveNotification(String title, String message, User user, Notification.NotificationType type);


}
