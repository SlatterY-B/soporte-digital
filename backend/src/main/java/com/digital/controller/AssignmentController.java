package com.digital.controller;

import com.digital.dto.AssignmentSummaryDTO;
import com.digital.entities.Assignment;
import com.digital.entities.Notification;
import com.digital.entities.User;
import com.digital.exception.ResourceNotFoundException;
import com.digital.service.AssignmentServiceImpl;
import com.digital.service.NotificationServiceImpl;
import com.digital.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.digital.dto.SupportRequestDTO;

import java.util.*;

@RestController
@CrossOrigin(value = "http://localhost:4200")
public class AssignmentController {


    @Autowired
    private AssignmentServiceImpl assignmentService;


    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private NotificationServiceImpl notificationService;


    private boolean isAdmin(Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());
        return user.getRole() == User.Role.ADMIN;
    }

    private boolean isAgent(Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());
        return user.getRole() == User.Role.AGENT;
    }


    @GetMapping("/assignments")
    public ResponseEntity<List<AssignmentSummaryDTO>> getAllAssignments(Authentication authentication) {

        if(isAdmin(authentication)) {
            return ResponseEntity.ok(
                    assignmentService.findAllAssignments().stream()
                            .map(assignment -> new AssignmentSummaryDTO(
                                    assignment.getIsCoordinator(),
                                    assignment.getAssignedAt(),
                                    assignment.getUpdatedAt(),
                                    assignment.getUpdatedByUser(),
                                    assignment.getAssignmentStatus(),
                                    new AssignmentSummaryDTO.SupportRequestDTO(
                                            assignment.getSupportRequest().getId(),
                                            assignment.getSupportRequest().getTitle(),
                                            assignment.getSupportRequest().getStatus().name()
                                    )

                            )).toList()

            );
        } else if (isAgent(authentication)) {
            User user = userService.findByEmail(authentication.getName());
            List<Assignment> assignments = assignmentService.findByAgentId(user.getId());
            return ResponseEntity.ok(
                    assignments.stream()
                            .map(assignment -> new AssignmentSummaryDTO(
                                    assignment.getIsCoordinator(),
                                    assignment.getAssignedAt(),
                                    assignment.getUpdatedAt(),
                                    assignment.getUpdatedByUser(),
                                    assignment.getAssignmentStatus(),
                                    new AssignmentSummaryDTO.SupportRequestDTO(
                                            assignment.getSupportRequest().getId(),
                                            assignment.getSupportRequest().getTitle(),
                                            assignment.getSupportRequest().getStatus().name()
                                    )
                            )).toList()
            );
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PostMapping("/assignments")
    public ResponseEntity<AssignmentSummaryDTO> addAssignment(@RequestBody Assignment assignment, Authentication authentication) {

        User user = userService.findByEmail(authentication.getName());

        if (!user.getRole().equals(User.Role.ADMIN) && !user.getRole().equals(User.Role.AGENT)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        }

        assignment.setAssignedAt(new Date());
        assignment.setUpdatedAt(new Date());
        assignment.setUpdatedByUser(user);
        assignment.setAssignmentStatus(Assignment.AssignmentStatus.PENDING);

        Assignment savedAssignment = assignmentService.saveAssignment(assignment);

        // Notify the agent about the new assignment
        User assignedAgent = savedAssignment.getAgent();
        if (assignedAgent != null) {
            notificationService.createAndSaveNotification(
                    "👷 New Assignment Assigned",
                    "You have been assigned a new support request: " + savedAssignment.getSupportRequest().getTitle() +
                            "ticket ID: " + savedAssignment.getSupportRequest().getId() +
                    "Assignment agent name: " + assignedAgent.getFullName() ,
                    assignedAgent,
                    Notification.NotificationType.AGENT
            );

        }

        int openTickets = assignmentService.countOpenAssignmentByAgent(assignedAgent.getId());
        if (openTickets > 4) {
            notificationService.createAndSaveNotification(
                    "🚨 Assignment Limit Reached",
                    "You have reached the limit of 4 open assignments. Please complete some before accepting new ones, Consider serving them",
                    assignedAgent,
                    Notification.NotificationType.AGENT
            );
        }

        if (assignment.getAgent() != null && assignment.getSupportRequest() != null) {
            User assignsAgent = assignment.getAgent();
            Long ticketId = assignment.getSupportRequest().getId();
            String ticketTitle = assignment.getSupportRequest().getTitle();

            notificationService.createAndSaveNotification(
                    "👷 New Assignment Assigned",
                    "You have been assigned a new support request: " + ticketTitle +
                            " (Ticket ID: " + ticketId + ") by " + user.getFullName(),
                    assignsAgent,
                    Notification.NotificationType.AGENT
            );
        }

        notificationService.createNotification(
                "New ticket assigned",
                "The ticket has been assigned to you #" + assignment.getSupportRequest().getId() + ":" + assignment.getSupportRequest().getTitle(),
                assignment.getAgent(),
                Notification.NotificationType.AGENT

        );

        notificationService.createNotification(
                 "Assigned Agent",
                "the agent" + assignment.getAgent().getFullName() + " has been assigned to the ticket #" + assignment.getSupportRequest().getId() + "." ,
                assignment.getSupportRequest().getCustomer() ,
                Notification.NotificationType.CUSTOMER
        );



        AssignmentSummaryDTO dto = new AssignmentSummaryDTO(
                savedAssignment.getIsCoordinator(),
                savedAssignment.getAssignedAt(),
                savedAssignment.getUpdatedAt(),
                savedAssignment.getUpdatedByUser(),
                savedAssignment.getAssignmentStatus(),
                null
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);

    }

    @GetMapping("/assignments/{id}")
    public ResponseEntity<Assignment> getAssignmentById(@PathVariable Long id, Authentication authentication) {

        Assignment assignment = assignmentService.findAssignmentById(id);
        User user = userService.findByEmail(authentication.getName());

        if (isAdmin(authentication)) {
            return ResponseEntity.ok(assignment);

        }

        if(isAgent(authentication) && assignment.getAgent().getId().equals(user.getId())) {
            return ResponseEntity.ok(assignment);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

    }


    @PutMapping("/assignments/{id}")
    public ResponseEntity<Assignment> updateAssignment(@PathVariable Long id, @RequestBody Assignment assignment, Authentication authentication) {

        Assignment existingAssignment = assignmentService.findAssignmentById(id);
        User user = userService.findByEmail(authentication.getName());

        if(isAdmin(authentication) || (isAgent(authentication) && existingAssignment.getAgent().getId().equals(user.getId()))) {
            existingAssignment.setAssignmentStatus(assignment.getAssignmentStatus());
            existingAssignment.setIsCoordinator(assignment.getIsCoordinator());
            existingAssignment.setUpdatedAt(new Date());
            existingAssignment.setUpdatedByUser(assignment.getUpdatedByUser());

            this.assignmentService.saveAssignment(existingAssignment);
            return ResponseEntity.ok(existingAssignment);

        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @Transactional
    @PutMapping("/assignments/{id}/status")
    public ResponseEntity<?> updateAssignmentStatus(@PathVariable Long id, @RequestParam Assignment.AssignmentStatus newStatus, Authentication authentication ) {

        User user = userService.findByEmail(authentication.getName());
        Assignment assignment = assignmentService.findAssignmentByIdWithSupportRequestAndCustomer(id);

        if (isAdmin(authentication)) {
            Assignment updated = assignmentService.updateAssignmentStatus(id, newStatus);
            return ResponseEntity.ok(updated);

        }
        if (isAgent(authentication)) {
            if (!assignment.getAgent().getId().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to update this assignment.");
            }
            if (newStatus == Assignment.AssignmentStatus.PENDING) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot update assignment status to PENDING.");
            }


            Assignment updated = assignmentService.updateAssignmentStatus(id, newStatus);

            //notify the agent about the status change

            if (newStatus == Assignment.AssignmentStatus.IN_PROGRESS) {
                System.out.println("📢 Customer: " + updated.getSupportRequest().getCustomer());
                notificationService.createAndSaveNotification(
                        "⏳ Ticket in progress",
                        "The ticket #" + updated.getSupportRequest().getId() + " is being attended to.",
                        updated.getSupportRequest().getCustomer(),
                        Notification.NotificationType.CUSTOMER
                );
            }

            if (newStatus == Assignment.AssignmentStatus.COMPLETED) {
                notificationService.createAndSaveNotification(
                        "✅ Ticket Completed",
                        "The ticket #" + updated.getSupportRequest().getId() + " has been completed.",
                        updated.getSupportRequest().getCustomer(),
                        Notification.NotificationType.CUSTOMER
                );                System.out.println("📢 Customer: " + updated.getSupportRequest().getCustomer());

            }
//
//            if (newStatus == Assignment.AssignmentStatus.CANCELED) {
//                notificationService.createNotification(
//                        "❌ Ticket Canceled",
//                        "The ticket #" + assignment.getSupportRequest().getId() + " has been canceled.",
//                        assignment.getSupportRequest().getCustomer(),
//                        Notification.NotificationType.CUSTOMER
//                );
//            }

            return ResponseEntity.ok(updated);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to update this assignment.");
    }




    @DeleteMapping("/assignments/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteAssignment(@PathVariable Long id, Authentication authentication) {

        if(!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Assignment assignment = assignmentService.findAssignmentById(id);

        if(assignment == null) {
            throw new ResourceNotFoundException("Assignment not found id: " + id);
        }

        assignmentService.deleteAssignment(assignment.getId());

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);

    }


    @GetMapping("/assignments/agent/{agentId}")
    public ResponseEntity<List<Assignment>> getAssignmentsByAgent(@PathVariable Long agentId, Authentication authentication) {

        if(isAdmin(authentication)) {
            List<Assignment> assignments = assignmentService.findByAgentId(agentId);
            if(assignments.isEmpty()) {
                return ResponseEntity.ok(Collections.emptyList());
            }
            return ResponseEntity.ok(assignments);
        }

        User user = userService.findByEmail(authentication.getName());
        if(isAgent(authentication) && user.getId().equals(agentId)) {
            List<Assignment> assignments = assignmentService.findByAgentId(user.getId());
            if(assignments.isEmpty()) {
                return ResponseEntity.ok(Collections.emptyList());

            }
            return ResponseEntity.ok(assignments);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

    }


    @GetMapping("/assignments/status/{assignmentStatus}")
    public ResponseEntity<List<Assignment>> getAssignmentsByStatus(@PathVariable Assignment.AssignmentStatus assignmentStatus,Authentication authentication) {

        if(!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        }
        List<Assignment> assignments = assignmentService.findByAssignmentStatus(assignmentStatus);

        if (assignments.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        return ResponseEntity.ok(assignments);
    }


    @GetMapping("/assignments/support-request/{supportRequestId}")
    public ResponseEntity<List<Assignment>> getAssignmentsBySupportRequest(@PathVariable Long supportRequestId, Authentication authentication) {

        if(!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<Assignment> assignments = assignmentService.findBySupportRequestId(supportRequestId);

        if (assignments.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/assignments/agent/{agentId}/status/{assignmentStatus}")
    public ResponseEntity<List<Assignment>> getAssignmentsByAgentAndStatus(@PathVariable Long agentId, @PathVariable Assignment.AssignmentStatus assignmentStatus, Authentication authentication) {
        if(!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<Assignment> assignments = assignmentService.findByAgentIdAndAssignmentStatus(agentId, assignmentStatus);

        if (assignments.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(assignments);
    }

   @GetMapping("/assignments/page")
   public ResponseEntity<Page<AssignmentSummaryDTO>> getAssignmentsPage(@RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "5") int size,
                                                                        Authentication authentication) {
        Pageable pageable = PageRequest.of(page, size);
        User user = userService.findByEmail(authentication.getName());

        Page<Assignment> assignmentPage;

        if (isAdmin(authentication)) {
            assignmentPage = assignmentService.findAllAssignments(pageable);
        } else if (isAgent(authentication)) {
            assignmentPage = assignmentService.findByAgentId(user.getId(), pageable);

        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Page<AssignmentSummaryDTO> dtoPage = assignmentPage.map(assignment -> new AssignmentSummaryDTO(
                assignment.getIsCoordinator(),
                assignment.getAssignedAt(),
                assignment.getUpdatedAt(),
                assignment.getUpdatedByUser(),
                assignment.getAssignmentStatus(),
                new AssignmentSummaryDTO.SupportRequestDTO(
                        assignment.getSupportRequest().getId(),
                        assignment.getSupportRequest().getTitle(),
                        assignment.getSupportRequest().getStatus().name()
                )
        ));

        return ResponseEntity.ok(dtoPage);

   }

    @GetMapping("/assignments/by-support-request/{supportRequestId}")
    public ResponseEntity<Assignment> getAssignmentBySupportRequestId(@PathVariable Long supportRequestId) {
        Optional<Assignment> assignment = assignmentService.findFirstBySupportRequestId(supportRequestId);
        return assignment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PutMapping("/assignments/{requestId}/assign")
    public ResponseEntity<?> assignAgentToSupportRequest(
            @PathVariable Long requestId,
            @RequestBody Map<String, Long> body,
            Authentication authentication) {

        if (!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Long agentId = body.get("agentId");
        Assignment assignment = assignmentService.assignAgentToRequest(requestId, agentId, authentication);

        return ResponseEntity.ok(assignment);
    }



}



