package com.digital.entities.repository;

import com.digital.entities.Assignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {


    List<Assignment> findByAgentId(Long agentId);

    List<Assignment> findByAssignmentStatus(Assignment.AssignmentStatus assignmentStatus);

    List<Assignment> findBySupportRequestId(Long supportRequestId);

    List<Assignment> findByAgentIdAndAssignmentStatus(Long agentId, Assignment.AssignmentStatus assignmentStatus);


    @Query("SELECT a.supportRequest.id FROM Assignment a WHERE a.agent.id = :agentId")
    List<Long> findSupportRequestIdsByAgentId(@Param("agentId") Long agentId);


    int countByAgentIdAndAssignmentStatus(Long agentId, Assignment.AssignmentStatus assignmentStatus);


    @Query("SELECT a FROM Assignment a " +
            "JOIN FETCH a.supportRequest sr " +
            "JOIN FETCH sr.customer " +
            "WHERE a.id = :id")
    Optional<Assignment> findByIdWithSupportRequestAndCustomer(@Param("id") Long id);


    Page<Assignment> findAll(Pageable pageable);

    Page<Assignment> findByAgentId(Long agentId, Pageable pageable);


    Optional<Assignment> findFirstBySupportRequestId(Long supportRequestId);



}
