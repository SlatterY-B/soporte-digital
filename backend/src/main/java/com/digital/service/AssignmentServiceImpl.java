package com.digital.service;

import com.digital.entities.Activity;
import com.digital.entities.Assignment;
import com.digital.entities.SupportRequest;
import com.digital.entities.User;
import com.digital.entities.repository.AssignmentRepository;
import com.digital.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class AssignmentServiceImpl implements AssignmentService {


    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private ActivityServiceImpl activityService;

    @Autowired
    private NotificationServiceImpl notificationService;

    @Autowired
    private UserServiceImpl userService;


    @Override
    public List<Assignment> findAllAssignments() {
        return assignmentRepository.findAll();
    }

    @Override
    public Assignment findAssignmentById(Long id) {
        return assignmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Assignment not found"));
    }

    @Override
    public Assignment saveAssignment(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

    @Override
    public void deleteAssignment(Long id) {

        Assignment assignment = findAssignmentById(id);
        assignmentRepository.delete(assignment);

    }

    @Override
    public List<Assignment> findByAgentId(Long agentId) {
        return assignmentRepository.findByAgentId(agentId);
    }

    @Override
    public List<Assignment> findByAssignmentStatus(Assignment.AssignmentStatus assignmentStatus) {
        return assignmentRepository.findByAssignmentStatus(assignmentStatus);
    }

    @Override
    public List<Assignment> findBySupportRequestId(Long supportRequestId) {
        return assignmentRepository.findBySupportRequestId(supportRequestId);
    }

    @Override
    public List<Assignment> findByAgentIdAndAssignmentStatus(Long agentId, Assignment.AssignmentStatus assignmentStatus) {
        return assignmentRepository.findByAgentIdAndAssignmentStatus(agentId, assignmentStatus);
    }


    @Transactional
    public Assignment updateAssignmentStatus(Long assignmentId, Assignment.AssignmentStatus newStatus) {
        Assignment assignment = findAssignmentByIdWithSupportRequestAndCustomer(assignmentId);
        assignment.setAssignmentStatus(newStatus);
        assignment.setUpdatedAt(new java.util.Date());

        Assignment updated = assignmentRepository.save(assignment);

//        if (newStatus == Assignment.AssignmentStatus.COMPLETED) {
//            Activity activity = Activity.builder()
//                    .activityDate(new java.util.Date())
//                    .activityDescription("Support request completed by agent: " + assignment.getAgent().getEmail())
//                    .hoursWorked(calculateHoursWorked(assignment.getAssignedAt(), new java.util.Date()))
//                    .assignment(updated)
//                    .build();
//
//            activityService.saveActivity(activity);
//        }
        // notify agent if assignment is completed and has an agent assigned

        if (newStatus == Assignment.AssignmentStatus.COMPLETED && assignment.getAgent() != null) {
            notifyAgentIfResolvedMultiple(assignment.getAgent().getId());
        }

        return updated;
    }

    private double calculateHoursWorked(java.util.Date start, java.util.Date end) {
        if (start == null || end == null) {
            return 0.0;
        }
        long millis = end.getTime() - start.getTime();
        double  hours = millis / (1000.0 * 60 * 60.0);
        return Math.round(hours * 100.0) / 100.0;
    }


    public int countOpenAssignmentByAgent(Long id) {
        List<Assignment> assignments = assignmentRepository.findByAgentIdAndAssignmentStatus(id, Assignment.AssignmentStatus.PENDING);
        return assignments.size();
    }

    public int countCompletedAssignmentsByAgent(Long agentId) {
        return assignmentRepository.countByAgentIdAndAssignmentStatus(agentId, Assignment.AssignmentStatus.COMPLETED);
    }

    public void notifyAgentIfResolvedMultiple(Long agentId) {
        int resolvedCount = countOpenAssignmentByAgent(agentId);

        if (resolvedCount > 2) {
            notificationService.SendNotificationToAgent(agentId,
                    "✅ You have resolved several tickets" +
                    "You have resolved " + resolvedCount + " tickets. Great job! Keep up the good work!"
            );
        }
    }

    public Assignment findAssignmentByIdWithSupportRequestAndCustomer(Long id) {
        return assignmentRepository.findByIdWithSupportRequestAndCustomer(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id " + id));
    }

    public Page<Assignment> findAllAssignments(Pageable pageable) {
        return assignmentRepository.findAll(pageable);
    }

    public Page<Assignment> findByAgentId(Long agentId, Pageable pageable) {
        return assignmentRepository.findByAgentId(agentId, pageable);
    }

    public Optional<Assignment> findFirstBySupportRequestId(Long supportRequestId) {
        return assignmentRepository.findFirstBySupportRequestId(supportRequestId);
    }

    //test

    public Assignment assignAgentToRequest(Long requestId, Long agentId, Authentication auth) {
        User adminUser = userService.findByEmail(auth.getName());

        Assignment assignment = new Assignment();
        assignment.setSupportRequest(new SupportRequest(requestId));
        assignment.setAgent(userService.findById(agentId));
        assignment.setAssignedAt(new Date());
        assignment.setUpdatedAt(new Date());
        assignment.setUpdatedByUser(adminUser);
        assignment.setAssignmentStatus(Assignment.AssignmentStatus.PENDING);

        return assignmentRepository.save(assignment);
    }





}
