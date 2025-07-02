package com.digital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ActivityResponseDTO {

    private Date activityDate;
    private String activityDescription;
    private double hoursWorked;
    private Long assignmentId;
}
