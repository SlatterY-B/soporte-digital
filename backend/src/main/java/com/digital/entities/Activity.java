package com.digital.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "activity_date")
    private Date activityDate;


    @Column(name = "activity_description")
    private String activityDescription;

    @Column(name = "hours_worked")
    private double hoursWorked;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "assignment"})
    @ManyToOne
    @JoinColumn(nullable = false, name = "assignment_id")
    private Assignment assignment;



}
