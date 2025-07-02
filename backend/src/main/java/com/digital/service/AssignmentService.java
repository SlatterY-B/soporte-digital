package com.digital.service;

import com.digital.entities.Assignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AssignmentService {

    List<Assignment> findAllAssignments();

    Assignment findAssignmentById(Long id);

    Assignment saveAssignment(Assignment assignment);

    void deleteAssignment(Long id);

    List<Assignment> findByAgentId(Long agentId);

    List<Assignment> findByAssignmentStatus(Assignment.AssignmentStatus assignmentStatus);

    List<Assignment> findBySupportRequestId(Long supportRequestId);

    List<Assignment> findByAgentIdAndAssignmentStatus(Long agentId, Assignment.AssignmentStatus assignmentStatus);

    Page<Assignment> findAllAssignments(Pageable pageable);

    Page<Assignment> findByAgentId(Long agentId, Pageable pageable);



}
