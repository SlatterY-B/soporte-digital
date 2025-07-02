package com.digital.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestDTO {

    private Long userId;
    private String title;
    private String message;
    private String type;



}
