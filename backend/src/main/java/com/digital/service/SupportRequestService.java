package com.digital.service;

import com.digital.entities.SupportRequest;

import java.util.Date;
import java.util.List;

public interface SupportRequestService {

    List<SupportRequest> findAllSupportRequests();

    SupportRequest findSupportRequestById(Long id);

    SupportRequest saveSupportRequest(SupportRequest supportRequest);

    void deleteSupportRequest(Long id);

    List<SupportRequest> findByCustomerId(Long customerId);

    List<SupportRequest> findByRequestStatus(SupportRequest.RequestStatus requestStatus);

    List<SupportRequest> findByCreatedAtBetween(Date startDate, Date endDate);

    List<SupportRequest> findByRequestType(SupportRequest.RequestType requestType);

    List<SupportRequest> findByCustomerIdAndRequestStatus(Long customerId, SupportRequest.RequestStatus requestStatus);

    List<SupportRequest> findByRequestTypeAndRequestStatus(SupportRequest.RequestType requestType, SupportRequest.RequestStatus requestStatus);

    List<SupportRequest> findByAgentId(Long agentId);



}
