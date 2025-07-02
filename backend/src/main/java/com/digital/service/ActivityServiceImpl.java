package com.digital.service;

import com.digital.entities.Activity;
import com.digital.entities.repository.ActivityRepository;
import com.digital.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityRepository activityRepository;


    @Override
    public Activity findActivityById(Long id) {
        return activityRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("Activity not found"));
    }

    @Override
    public Activity saveActivity(Activity activity) {
        return activityRepository.save(activity);
    }

    @Override
    public List<Activity> findByAssignmentId(Long assignmentId) {
        return activityRepository.findByAssignmentId(assignmentId);
    }

    @Override
    public List<Activity> findAllActivities() {
        return activityRepository.findAll();
    }
}
