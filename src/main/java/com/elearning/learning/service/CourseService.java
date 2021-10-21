package com.elearning.learning.service;

import com.elearning.learning.entities.UserCourseDetails;
import com.elearning.learning.repository.UserCourseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class CourseService {

    private final UserCourseRepository userCourseRepository;

    public List<String> getAuthenticatedCourses(String username) {
        UserCourseDetails userCourseDetails = userCourseRepository.findByUsername(username);
        List<String> allowedCourses = null;
        if(userCourseDetails.getAllowedCourses() != null && !userCourseDetails.getAllowedCourses().isEmpty()) {
            if(userCourseDetails.getAllowedCourses().indexOf(",") != -1){
                allowedCourses = Arrays.asList(userCourseDetails.getAllowedCourses().split(","));
            } else {
                allowedCourses.add(userCourseDetails.getAllowedCourses());
            }
        }
        return allowedCourses;
    }
}
