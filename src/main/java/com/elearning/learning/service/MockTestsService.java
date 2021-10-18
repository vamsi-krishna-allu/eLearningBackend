package com.elearning.learning.service;

import com.elearning.learning.entities.MockTestQuestions;
import com.elearning.learning.entities.UserCourseDetails;
import com.elearning.learning.model.TestResultRequest;
import com.elearning.learning.model.TestResultResponse;
import com.elearning.learning.repository.MockTestRepository;
import com.elearning.learning.repository.UserCourseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        List<UserCourseDetails> userCourseDetails = userCourseRepository.findByUsername(username);
        List<String> allowedTests = null;
        for(UserCourseDetails course: userCourseDetails) {
            allowedTests.add(course.getAllowedMockTest());
        }
        return allowedTests;
    }

    public TestResultResponse evaluateResults(TestResultRequest testResultRequest) {
        String username = testResultRequest.getUsername();
        String testName = testResultRequest.getTestName();
        List<MockTestQuestions> mockTestQuestions = mockTestRepository.findByTestType(testName);
        List<Integer> storedAnswers = new ArrayList<>();
        for(MockTestQuestions mockTestQuestion : mockTestQuestions) {
            storedAnswers.add(mockTestQuestion.getAnswer());
        }
        int i =0;
        int correctAnswers = 0;
        int wrongAnswers = 0;
        int unAnsweredQuestions = 0;
        int totalQuestions = 75;
        for(int givenAnswer : testResultRequest.getAnswer()) {
            if(givenAnswer == -1){
                unAnsweredQuestions = unAnsweredQuestions + 1;
            }
            else if(givenAnswer == storedAnswers.get(i)) {
                correctAnswers = correctAnswers + 1;
            }
            else if(givenAnswer != storedAnswers.get(i)) {
                wrongAnswers = wrongAnswers + 1;
            }
        }
        TestResultResponse testResultResponse = new TestResultResponse(correctAnswers, totalQuestions, totalQuestions - unAnsweredQuestions);

        return testResultResponse;
    }
}
