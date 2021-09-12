package com.elearning.learning.controller;

import com.elearning.learning.entities.MockTestQuestions;
import com.elearning.learning.service.MockTestsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class MockTestsController {

    private final MockTestsService mockTestsService;

    @GetMapping("/questions/{testType}")
    public ResponseEntity<List<MockTestQuestions>> getQuestions(@PathVariable(value = "testType") String testType) {
        List<MockTestQuestions> mockTestQuestions = mockTestsService.getQuestions(testType);
        return ResponseEntity.ok().body(mockTestQuestions);
    }

}
