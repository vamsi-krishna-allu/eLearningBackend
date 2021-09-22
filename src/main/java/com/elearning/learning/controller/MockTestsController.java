package com.elearning.learning.controller;

import com.elearning.learning.entities.MockTestQuestions;
import com.elearning.learning.service.MockTestsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping(value = "/authenticatedTests")
    public ResponseEntity<List<String>> getAuthenticatedCourses(@RequestParam("username") String username){
        List<String> authenticatedMockTests = mockTestsService.getAuthenticatedMockTests(username);
        return ResponseEntity.ok(authenticatedMockTests);
    }

}
