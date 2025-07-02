package com.digital.entities;

import jakarta.persistence.*;
import lombok.*;

import java.net.ProtocolFamily;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
@Table(name = "support_request")
public class SupportRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name = "description_support", length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_type")
    private RequestType requestType;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_status")
    private RequestStatus requestStatus;

    @Column(name = "created_at")
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    public SupportRequest(Long requestId) {
    }

    public RequestStatus getStatus() {
        return requestStatus;
    }


    public enum RequestType {
       BUG,
        TRAINING,
        REQUEST
    }

    public enum RequestStatus {
        PENDING,
        IN_PROGRESS,
        COMPLETED,
    }
}
