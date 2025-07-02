package com.digital.dto;

import com.digital.entities.Assignment;
import com.digital.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;

@Data
@AllArgsConstructor
public class AssignmentSummaryDTO {

    private Boolean isCordinator;
    private Date assignedAt;
    private Date updatedAt;
    private User updatedByUser;
    private Assignment.AssignmentStatus assignmentStatus;

    private SupportRequestDTO supportRequestDTO;




    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SupportRequestDTO {
        private Long id;
        private String title;
        private String status;
    }
}
