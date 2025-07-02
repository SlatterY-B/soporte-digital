package com.digital.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "support_request_id", nullable = false)
    @JsonIgnoreProperties({"customer", "requestType", "requestStatus", "createdAt", "updatedAt"})
    private SupportRequest supportRequest;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="agent_id" , nullable = false)
    @JsonIgnoreProperties({"password", "email", "phoneNumber", "role", "createdAt", "updatedAt"})
    private User agent;

    @Column(name = "is_coordinator")
    private Boolean isCoordinator;

    @Column(name = "assigned_at")
    private Date assignedAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "updated_by_user_id")
    @JsonIgnoreProperties({"password", "email", "phoneNumber", "role", "createdAt", "updatedAt"})
    private User updatedByUser;

    @Column(name = "assignment_status")
    private AssignmentStatus assignmentStatus;




    public enum AssignmentStatus {
        PENDING,
        IN_PROGRESS,
        COMPLETED,
    }


    public Assignment(Long id) {
        this.id = id;
    }


}
