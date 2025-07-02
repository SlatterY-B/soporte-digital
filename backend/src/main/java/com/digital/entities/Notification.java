package com.digital.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Data
@AllArgsConstructor
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    @Column(length = 1000)
    private String message;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "is_read")
    private boolean read = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private NotificationType type;


    public Notification() {
        this.timestamp = LocalDateTime.now();
    }

    public Notification(String title, String message, User user, NotificationType type) {
        this.title = title;
        this.message = message;
        this.user = user;
        this.type = type;
        this.timestamp = LocalDateTime.now();
        this.read = false;
    }

    public enum NotificationType {
        ADMIN,
        AGENT,
        CUSTOMER
    }
}
