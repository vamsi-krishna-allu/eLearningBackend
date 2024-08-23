package com.elearning.learning.service;

import com.elearning.learning.entities.UserCourseDetails;
import com.elearning.learning.model.CourseMapper;
import com.elearning.learning.repository.UserCourseRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class CourseService {

    private final UserCourseRepository userCourseRepository;

    public List<String> getAuthenticatedCourses(String username) throws JsonProcessingException {
        UserCourseDetails userCourseDetails = userCourseRepository.findByUsername(username);
        List<String> allowedCourses = new ArrayList<>();
        if(userCourseDetails.getAllowedCourses() != null && !userCourseDetails.getAllowedCourses().isEmpty()) {
            ObjectMapper mapper = new ObjectMapper();
            List<CourseMapper> courseList = mapper.readValue(userCourseDetails.getAllowedCourses(), new TypeReference<List<CourseMapper>>(){});
            for(CourseMapper course : courseList) {
                if((new Date(course.getEndTime())).after(new Date())) {
                    allowedCourses.add(course.getId());
                }
            }
        }
        return allowedCourses;
    }
}
