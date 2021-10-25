package com.elearning.learning.service;

import com.elearning.learning.constants.MockTests;
import com.elearning.learning.model.MockTestQuestions;
import com.elearning.learning.entities.UserCourseDetails;
import com.elearning.learning.entities.UserTestResults;
import com.elearning.learning.model.TestResultRequest;
import com.elearning.learning.model.TestResultResponse;
import com.elearning.learning.model.UserTestDetails;
import com.elearning.learning.repository.TestResultsRepository;
import com.elearning.learning.repository.UserCourseRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class MockTestsService {

    private final TestResultsRepository testResultsRepository;
    private final UserCourseRepository userCourseRepository;

    public List<MockTestQuestions> getQuestions(String testType) {
        File file = new File(
                this.getClass().getClassLoader().getResource("src\\main\\resources\\mockTests"+testType+".json").getFile()
        );
        ObjectMapper mapper = new ObjectMapper();
        List<MockTestQuestions> mockTestQuestions = null;
        try {
            mockTestQuestions = mapper.readValue(file, new TypeReference<List<MockTestQuestions>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mockTestQuestions;
    }

    public UserTestDetails getAuthenticatedMockTests(String username) {
        UserCourseDetails userCourseDetails = userCourseRepository.findByUsername(username);
        List<String> allowedTests = null;
        List<String> submittedTests = null;
        if(userCourseDetails.getAllowedMockTests() != null && !userCourseDetails.getAllowedMockTests().isEmpty()) {
            if(userCourseDetails.getAllowedMockTests().indexOf(",") != -1){
                allowedTests = Arrays.asList(userCourseDetails.getAllowedMockTests().split(","));
            } else {
                allowedTests.add(userCourseDetails.getAllowedMockTests());
            }
        }
        if(userCourseDetails.getSubmittedMockTests() != null && !userCourseDetails.getSubmittedMockTests().isEmpty()) {
            if(userCourseDetails.getSubmittedMockTests().indexOf(",") != -1){
                submittedTests = Arrays.asList(userCourseDetails.getSubmittedMockTests().split(","));
            } else {
                submittedTests.add(userCourseDetails.getAllowedMockTests());
            }
        }
        UserTestDetails userTestDetails = new UserTestDetails(allowedTests, submittedTests);
        return userTestDetails;
    }

    public TestResultResponse evaluateResults(TestResultRequest testResultRequest) {
        String username = testResultRequest.getUsername();
        String testName = testResultRequest.getTestName();
        List<Integer> storedAnswers = MockTests.getAnswers(testName);
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

        UserTestResults userTestResults = new UserTestResults();
        userTestResults.setUsername(username);
        userTestResults.setTestType(testName);
        userTestResults.setCorrectAnswers(correctAnswers);
        userTestResults.setAttemptedQuestions(totalQuestions - unAnsweredQuestions);
        testResultsRepository.save(userTestResults);

        UserCourseDetails userCourseDetails = userCourseRepository.findByUsername(username);
        String submittedTest = testName;
        if(!StringUtils.isEmpty(userCourseDetails.getSubmittedMockTests())) {
            submittedTest = userCourseDetails.getSubmittedMockTests().concat(",").concat(testName);
        }
        userCourseRepository.updateSubmittedTest(username, submittedTest);

        return testResultResponse;
    }

    public TestResultResponse getResults(String username, String testType) {
        UserTestResults userTestResults = testResultsRepository.findByTestTypeAndUsername(testType, username);
        TestResultResponse testResultResponse = new TestResultResponse(userTestResults.getCorrectAnswers(), 75, userTestResults.getAttemptedQuestions());

        return testResultResponse;
    }
}
