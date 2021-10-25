package com.elearning.learning.controller;

import com.elearning.learning.model.MockTestQuestions;
import com.elearning.learning.model.TestResultRequest;
import com.elearning.learning.model.TestResultResponse;
import com.elearning.learning.model.UserTestDetails;
import com.elearning.learning.service.MockTestsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class MockTestsController {

    private final MockTestsService mockTestsService;

    @GetMapping("/questions")
    public ResponseEntity<List<MockTestQuestions>> getQuestions(@RequestParam("testType") String testType) {
        List<MockTestQuestions> mockTestQuestions = mockTestsService.getQuestions(testType);
        return ResponseEntity.ok().body(mockTestQuestions);
    }

    @GetMapping("/authenticatedTests")
    public ResponseEntity<UserTestDetails> getAuthenticatedCourses(@RequestParam("username") String username){
        UserTestDetails authenticatedMockTests = mockTestsService.getAuthenticatedMockTests(username);
        return ResponseEntity.ok(authenticatedMockTests);
    }

    @PostMapping("/evaluateAnswers")
    public ResponseEntity<TestResultResponse> evaluateResults(@RequestBody TestResultRequest testResultRequest) {
        TestResultResponse testResultResponse = mockTestsService.evaluateResults(testResultRequest);
        return ResponseEntity.ok().body(testResultResponse);
    }

    @GetMapping("/showResults")
    public ResponseEntity<TestResultResponse> evaluateResults(@RequestParam("username") String username, @RequestParam("testType") String testType) {
        TestResultResponse testResultResponse = mockTestsService.getResults(username, testType);
        return ResponseEntity.ok().body(testResultResponse);
    }



}
