package com.digital.service;


import com.digital.entities.Notification;
import com.digital.entities.User;
import com.digital.entities.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements  NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public Notification createNotification(String title, String message, User user, Notification.NotificationType type) {
        return notificationRepository.save(new Notification(title, message, user, type));
    }

    @Override
    public List<Notification> getAllNotificationsByUser(User user) {
        return notificationRepository.findByUserOrderByTimestampDesc(user);
    }

    @Override
    public List<Notification> getUnReadNotificationByUser(User user) {
        return notificationRepository.findByUserAndReadFalseOrderByTimestampDesc(user);
    }

    @Override
    public void markAsRead(Long notificationId) {
        Optional<Notification> optional = notificationRepository.findById(notificationId);
        optional.ifPresent(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });

    }

    @Override
    public void markAllAsRead(User user) {
        List<Notification> unread = notificationRepository.findByUserAndReadFalseOrderByTimestampDesc(user);

        for (Notification notification : unread) {
            notification.setRead(true);
        }
        notificationRepository.saveAll(unread);

    }

//    public void createAndSaveNotification(String title, String message, User user, Notification.NotificationType type ) {
//        Notification notification = new Notification(title, message, user, type);
//        notificationRepository.save(notification);
//    }

    public Notification createAndSaveNotification(String title, String message, User user, Notification.NotificationType type) {
        System.out.println("⚠️ createAndSaveNotification ejecutado con: " + title);
        return notificationRepository.save(new Notification(title, message, user, type));
    }


    public void SendNotificationToAgent(Long agentId, String s) {
        // This method should implement the logic to send a notification to an agent
        // For example, you might want to create a Notification object and save it
        // using the notificationRepository.
        // Here is a simple implementation:

        User agent = new User(); // Fetch the agent by ID from your user repository
        agent.setId(agentId); // Set the agent ID

        Notification notification = new Notification();
        notification.setTitle("New Assignment");
        notification.setMessage(s);
        notification.setUser(agent);
        notification.setType(Notification.NotificationType.AGENT);

        notificationRepository.save(notification);
    }
}
