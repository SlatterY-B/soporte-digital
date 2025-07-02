package com.digital.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationsTestRequest {

    static class NotificationTestRequest {
        public static String title;
        public static String message;
        public static Long userId;
        public static String type; // debe ser "ADMIN", "AGENT" o "CUSTOMER"

        public Long getUserId() {
            return userId;
        }

        public String getTitle() {
            return title;
        }

        public String getMessage() {
            return message;
        }

        public String getType() {
            return type;
        }


    }

}
