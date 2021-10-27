package com.elearning.learning.repository;

import com.elearning.learning.entities.MockTestQuestions;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MockTestRepository extends CrudRepository<MockTestQuestions, Integer> {
    List<MockTestQuestions> findByTestType(String testType);
}
