package com.digital.service;

import com.digital.entities.SupportRequest;
import com.digital.entities.repository.AssignmentRepository;
import com.digital.entities.repository.SupportRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SupportRequestServiceImpl implements SupportRequestService {

    @Autowired
    private SupportRequestRepository supportRequestRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Override
    public List<SupportRequest> findAllSupportRequests() {
        return supportRequestRepository.findAll();
    }

    @Override
    public SupportRequest findSupportRequestById(Long id) {
        return supportRequestRepository.findById(id).orElseThrow(() -> new RuntimeException("Support request not found"));
    }

    @Override
    public SupportRequest saveSupportRequest(SupportRequest supportRequest) {
        return supportRequestRepository.save(supportRequest);
    }

    @Override
    public void deleteSupportRequest(Long id) {

        SupportRequest supportRequest = findSupportRequestById(id);
        supportRequestRepository.delete(supportRequest);

    }

    @Override
    public List<SupportRequest> findByCustomerId(Long customerId) {
        return supportRequestRepository.findAllByCustomerId(customerId);
    }

    @Override
    public List<SupportRequest> findByRequestStatus(SupportRequest.RequestStatus requestStatus) {
        return supportRequestRepository.findByRequestStatus(requestStatus);
    }

    @Override
    public List<SupportRequest> findByCreatedAtBetween(Date startDate, Date endDate) {
        return supportRequestRepository.findByCreatedAtBetween(startDate, endDate);
    }

    @Override
    public List<SupportRequest> findByRequestType(SupportRequest.RequestType requestType) {
        return supportRequestRepository.findByRequestType(requestType);
    }

    @Override
    public List<SupportRequest> findByCustomerIdAndRequestStatus(Long customerId, SupportRequest.RequestStatus requestStatus) {
        return supportRequestRepository.findByCustomerIdAndRequestStatus(customerId, requestStatus);
    }

    @Override
    public List<SupportRequest> findByRequestTypeAndRequestStatus(SupportRequest.RequestType requestType, SupportRequest.RequestStatus requestStatus) {
        return supportRequestRepository.findByRequestTypeAndRequestStatus(requestType, requestStatus);
    }


    @Override
    public List<SupportRequest> findByAgentId(Long agentId) {
        // 1️⃣ Obtiene todas las ids de requests asignadas a este Agent
        List<Long> requestIds = assignmentRepository.findSupportRequestIdsByAgentId(agentId);

        // 2️⃣ Si no hay ids asignadas, retorna una lista vacía
        if (requestIds.isEmpty()) {
            return List.of();
        }

        // 3️⃣ Si hay ids, obtiene todas las SupportRequest correspondientes
        return supportRequestRepository.findAllByIdIn(requestIds);
    }

    public Page<SupportRequest> findAllSupportRequests(Pageable pageable) {
        return supportRequestRepository.findAll(pageable);
    }

    public Page<SupportRequest> findAllByAgentId(Long agentId, Pageable pageable) {
        return supportRequestRepository.findAllByAgentId(agentId, pageable);
    }

    public Page<SupportRequest> findAllByCustomerId(Long customerId, Pageable pageable) {
        return supportRequestRepository.findAllByCustomerId(customerId, pageable);
    }

    public List<SupportRequest> getUnassignedSupportRequests() {
        return supportRequestRepository.findUnassignedSupportRequests();
    }

}
