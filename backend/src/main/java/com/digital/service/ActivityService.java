package com.digital.service;

import com.digital.entities.Activity;

import java.util.List;

public interface ActivityService {

    Activity findActivityById(Long id);

    Activity saveActivity(Activity activity);

    List<Activity> findByAssignmentId(Long assignmentId);

    List<Activity> findAllActivities();
}
