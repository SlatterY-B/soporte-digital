package com.digital.controller;


import com.digital.dto.ActivityResponseDTO;
import com.digital.entities.Activity;
import com.digital.entities.Assignment;
import com.digital.entities.User;
import com.digital.exception.ResourceNotFoundException;
import com.digital.service.ActivityServiceImpl;
import com.digital.service.AssignmentServiceImpl;
import com.digital.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(value = "http://localhost:4200")
public class ActivityController {

    @Autowired
    private ActivityServiceImpl activityService;

    @Autowired
    private AssignmentServiceImpl assignmentService;

    @Autowired
    private UserServiceImpl userService;

    private boolean isAdmin(Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());
        return user.getRole() == User.Role.ADMIN;
    }

    private boolean isAgent(Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());
        return user.getRole() == User.Role.AGENT;
    }

    private ActivityResponseDTO convertToDTO(Activity activity) {
        ActivityResponseDTO activityDTO = new ActivityResponseDTO();
        activityDTO.setActivityDate(activity.getActivityDate());
        activityDTO.setActivityDescription(activity.getActivityDescription());
        activityDTO.setHoursWorked(activity.getHoursWorked());
        activityDTO.setAssignmentId(activity.getAssignment().getId());
        return activityDTO;
    }


    @GetMapping("/activity")
    public ResponseEntity<List<ActivityResponseDTO>> getAllActivity(Authentication authentication) {

        if (!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<Activity> activities = activityService.findAllActivities();
        List<ActivityResponseDTO> activityDTOs = activities.stream().map(activity -> convertToDTO(activity)).collect(Collectors.toList());

        return ResponseEntity.ok(activityDTOs);
    }




    @GetMapping("/activity/{id}")
    public ResponseEntity<Activity> getActivity(@PathVariable Long id, Authentication authentication) {

        Activity activity = activityService.findActivityById(id);
        User user = userService.findByEmail(authentication.getName());

        if(activity == null) {
            throw new ResourceNotFoundException("activity not found id: " + id);
        }

        if(isAdmin(authentication)) {
            return ResponseEntity.ok(activity);
        }

        if(isAgent(authentication) && activity.getAssignment().getAgent().getId().equals(user.getId())) {
            return  ResponseEntity.ok(activity);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();


    }



    @PostMapping("/activity")
    public ResponseEntity<ActivityResponseDTO> addActivity(@RequestBody Activity activity, Authentication authentication) {

        User user = userService.findByEmail(authentication.getName());

        if(isAdmin(authentication)) {
           Activity saved = activityService.saveActivity(activity);
           return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(saved));

        }

        if(isAgent(authentication)) {
            Assignment assignment = assignmentService.findAssignmentById(activity.getAssignment().getId());
            if(assignment.getAgent().getId().equals(user.getId())) {
                Activity saved = activityService.saveActivity(activity);
                return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(saved));
            }else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }



    @GetMapping("/activity/assigned/{id}")
    public ResponseEntity<List<ActivityResponseDTO>> getActivityByAssigned(@PathVariable Long id, Authentication authentication) {


        User user = userService.findByEmail(authentication.getName());
        Assignment assignment = assignmentService.findAssignmentById(id);

        if (isAdmin(authentication)) {
            return ResponseEntity.ok(activityService.findByAssignmentId(id).stream().map(this::convertToDTO).toList());

        }

        if (isAgent(authentication) && assignment.getAgent().getId().equals(user.getId())) {

            return ResponseEntity.ok(activityService.findByAssignmentId(id).stream().map(this::convertToDTO).toList());

        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

    }

}
