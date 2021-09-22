package com.elearning.learning.service;

import com.elearning.learning.entities.MockTestQuestions;
import com.elearning.learning.entities.UserCourseDetails;
import com.elearning.learning.repository.MockTestRepository;
import com.elearning.learning.repository.UserCourseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MockTestsService {

    private final MockTestRepository mockTestRepository;
    private final UserCourseRepository userCourseRepository;

    public List<MockTestQuestions> getQuestions(String testType) {
        return mockTestRepository.findByTestType(testType);
    }

    public List<String> getAuthenticatedMockTests(String username) {
        UserCourseDetails userCourseDetails = userCourseRepository.findByUsername(username);
        return userCourseDetails.getAllowedMockTests();
    }
}
