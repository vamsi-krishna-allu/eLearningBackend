package com.elearning.learning.service;

import com.elearning.learning.entities.MockTestQuestions;
import com.elearning.learning.repository.MockTestRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MockTestsService {

    private final MockTestRepository mockTestRepository;

    public List<MockTestQuestions> getQuestions(String testType) {
        return mockTestRepository.findByTestType(testType);
    }
}
