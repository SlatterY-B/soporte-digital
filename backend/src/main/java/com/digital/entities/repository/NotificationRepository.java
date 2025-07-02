package com.digital.entities.repository;

import com.digital.entities.Notification;
import com.digital.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserOrderByTimestampDesc(User user);

    List<Notification> findByUserAndReadFalse(User user);


    List<Notification> findByUserAndReadFalseOrderByTimestampDesc(User user);

    //Notification createAndSaveNotification(String title, String message, User user, Notification.NotificationType type);

}
