package com.digital.entities.repository;

import com.digital.entities.SupportRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SupportRequestRepository extends JpaRepository<SupportRequest, Long> {

    List<SupportRequest> findAllByCustomerId(Long customerId);


    List<SupportRequest> findByRequestStatus(SupportRequest.RequestStatus requestStatus);

    List<SupportRequest> findByCreatedAtBetween(Date startDate, Date endDate);

    List<SupportRequest>  findByRequestType(SupportRequest.RequestType requestType);

    List<SupportRequest> findByCustomerIdAndRequestStatus(Long customerId, SupportRequest.RequestStatus requestStatus);

    List<SupportRequest> findByRequestTypeAndRequestStatus(SupportRequest.RequestType requestType, SupportRequest.RequestStatus requestStatus);

    List<SupportRequest> findAllByIdIn(List<Long> ids);

    Page<SupportRequest> findAll(Pageable pageable);

    //Page<SupportRequest> findAllByAgentId(Long agentId, Pageable pageable);

    Page<SupportRequest> findAllByCustomerId(Long customerId, Pageable pageable);

    @Query("SELECT sr FROM SupportRequest sr " +
            "JOIN Assignment a ON a.supportRequest.id = sr.id " +
            "WHERE a.agent.id = :agentId")
    Page<SupportRequest> findAllByAgentId(@Param("agentId") Long agentId, Pageable pageable);


    @Query("SELECT sr FROM SupportRequest sr WHERE sr.id NOT IN (SELECT a.supportRequest.id FROM Assignment a)")
    List<SupportRequest> findUnassignedSupportRequests();



}
