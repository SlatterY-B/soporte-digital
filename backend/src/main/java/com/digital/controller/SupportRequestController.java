package com.digital.controller;


import com.digital.entities.Notification;
import com.digital.entities.SupportRequest;
import com.digital.entities.User;
import com.digital.service.NotificationService;
import com.digital.service.NotificationServiceImpl;
import com.digital.service.SupportRequestServiceImpl;
import com.digital.service.UserServiceImpl;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.digital.exception.ResourceNotFoundException;
import org.springframework.security.core.Authentication;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(value = "http://localhost:4200")
public class SupportRequestController {

    @Autowired
    private SupportRequestServiceImpl supportRequestService;


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

    private boolean isCustomer(Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());

        return user.getRole() == User.Role.CUSTOMER;
    }


    @GetMapping("/support-requests")
    public ResponseEntity<List<SupportRequest>> getAllSupportRequests(Authentication authentication) {

        if(isAdmin(authentication)) {

            return ResponseEntity.ok(supportRequestService.findAllSupportRequests());

        }else if(isAgent(authentication)) {

            User user = userService.findByEmail(authentication.getName());

            List<SupportRequest> supportRequests = supportRequestService.findByAgentId(user.getId());

            return ResponseEntity.ok(supportRequests);

        }else if(isCustomer(authentication)) {

            User user = userService.findByEmail(authentication.getName());

            List<SupportRequest> supportRequests = supportRequestService.findByCustomerId(user.getId());

            return ResponseEntity.ok(supportRequests);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

    }

    @PostMapping("/support-requests")
    public ResponseEntity<SupportRequest> createSupportRequest(@RequestBody SupportRequest supportRequest, Authentication authentication) {

        User user = userService.findByEmail(authentication.getName());

        if(!user.getRole().equals(User.Role.CUSTOMER)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        supportRequest.setCustomer(user);
        supportRequest.setCreatedAt(new Date());
        supportRequest.setRequestStatus(SupportRequest.RequestStatus.PENDING);

        SupportRequest savedSupportRequest = supportRequestService.saveSupportRequest(supportRequest);

        User admin = userService.findByEmail("flysoloxd@gmail.com");
        notificationService.createAndSaveNotification(
                "🛠️ New Support Request",
                "A new support request has been created by " + user.getFullName() + " a new support request has been created with title: " + savedSupportRequest.getTitle() +
                "with id: " + savedSupportRequest.getId(),
                admin,
                Notification.NotificationType.ADMIN
        );

        notificationService.createAndSaveNotification(
                "🛠️ Support Request Created",
                "Your support request with title: " + savedSupportRequest.getTitle() + " has been created successfully.",
                user,
                Notification.NotificationType.CUSTOMER
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(savedSupportRequest);

    }



    @GetMapping("/support-requests/{id}")
    public ResponseEntity<SupportRequest> getSupportRequestById(@PathVariable Long id, Authentication authentication) {

        SupportRequest supportRequest = supportRequestService.findSupportRequestById(id);

        User user = userService.findByEmail(authentication.getName());

        if(isAdmin(authentication)) {
            return ResponseEntity.ok(supportRequest);
        }

        if(isCustomer(authentication)  && supportRequest.getCustomer().equals(user.getId())) {
            return ResponseEntity.ok(supportRequest);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

    }

    @PutMapping("/support-requests/{id}")
    public ResponseEntity<SupportRequest> updateSupportRequest(@PathVariable Long id, @RequestBody SupportRequest supportRequest, Authentication authentication) {

        if(!(isAdmin(authentication) || isAgent(authentication))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        SupportRequest existingSupportRequest = supportRequestService.findSupportRequestById(id);

        existingSupportRequest.setRequestType(supportRequest.getRequestType());
        existingSupportRequest.setTitle(supportRequest.getTitle());
        existingSupportRequest.setDescription(supportRequest.getDescription());
        existingSupportRequest.setRequestStatus(supportRequest.getRequestStatus());

        this.supportRequestService.saveSupportRequest(existingSupportRequest);

        return ResponseEntity.ok(existingSupportRequest);

    }

    @DeleteMapping("/support-requests/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteSupportRequest(@PathVariable Long id, Authentication authentication) {

        if(!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        SupportRequest supportRequest = supportRequestService.findSupportRequestById(id);

        if(supportRequest == null) {
            throw new ResourceNotFoundException("Support Request not found with id: " + id);

        }

        supportRequestService.deleteSupportRequest(id);

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/support-requests/customer/{customerId}")
    public ResponseEntity<List<SupportRequest>>  getSupportRequestsByCustomerId(@PathVariable Long customerId, Authentication authentication) {

        if(isAdmin(authentication)) {
            return ResponseEntity.ok(supportRequestService.findByCustomerId(customerId));
        }

        if(isCustomer(authentication)) {
            User user = userService.findByEmail(authentication.getName());
            if(!user.getId().equals(customerId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.ok(supportRequestService.findByCustomerId(customerId));

        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

    }

    @GetMapping("/support-requests/status/{status}")
    public ResponseEntity <List<SupportRequest>> getSupportRequestsByStatus(@PathVariable SupportRequest.RequestStatus status, Authentication authentication) {

        if(!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        }

        return ResponseEntity.ok(supportRequestService.findByRequestStatus(status));
    }

    @GetMapping("/support-requests/date-range")
    public ResponseEntity<List<SupportRequest>>  getSupportRequestsByDateRange (
            @RequestParam("start") @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam("end") @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) Date endDate , Authentication authentication) {

        if(!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(supportRequestService.findByCreatedAtBetween(startDate, endDate));

    }

    @GetMapping("/support-requests/type/{type}")
    public ResponseEntity<List<SupportRequest>> getSupportRequestsByType(@PathVariable SupportRequest.RequestType type, Authentication authentication) {

        if(!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(supportRequestService.findByRequestType(type));
    }


    @GetMapping("/support-requests/customer/{customerId}/status/{status}")
    public ResponseEntity<List<SupportRequest>> getSupportRequestsByCustomerIdAndStatus(
            @PathVariable Long customerId,
            @PathVariable SupportRequest.RequestStatus status, Authentication authentication) {

        if(isAdmin(authentication)) {
            return ResponseEntity.ok(supportRequestService.findByCustomerIdAndRequestStatus(customerId, status));

        }

        if(isCustomer(authentication)) {
            User user = userService.findByEmail(authentication.getName());
            if(!user.getId().equals(customerId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.ok(supportRequestService.findByCustomerIdAndRequestStatus(customerId, status));

        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/support-requests/type/{type}/status/{status}")
    public  ResponseEntity<List<SupportRequest>> getSupportRequestsByTypeAndStatus(@PathVariable SupportRequest.RequestType type,
                                                                  @PathVariable SupportRequest.RequestStatus status, Authentication authentication) {

        if(!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        }
        return ResponseEntity.ok(supportRequestService.findByRequestTypeAndRequestStatus(type, status));
    }

    @GetMapping("/support-requests/page")
    public ResponseEntity<Page<SupportRequest>> getSupportRequestsPage(@RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "4") int size,
                                                                       Authentication authentication) {
        Pageable pageable = PageRequest.of(page, size);
        User user = userService.findByEmail(authentication.getName());

        if(isAdmin(authentication)) {

            return ResponseEntity.ok(supportRequestService.findAllSupportRequests(pageable));

        } else if (isAgent(authentication)) {
            return ResponseEntity.ok(supportRequestService.findAllByAgentId(user.getId(), pageable));
        } else if (isCustomer(authentication)) {
            return ResponseEntity.ok(supportRequestService.findAllByCustomerId(user.getId(), pageable));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/support-requests/unassigned")
    public ResponseEntity<List<SupportRequest>> getUnassignedSupportRequests() {
        List<SupportRequest> unassigned = supportRequestService.getUnassignedSupportRequests();
        return ResponseEntity.ok(unassigned);
    }


}
