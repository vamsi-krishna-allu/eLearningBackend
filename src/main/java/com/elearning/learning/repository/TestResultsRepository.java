package com.elearning.learning.repository;

import com.elearning.learning.entities.MockTestQuestions;
import com.elearning.learning.entities.UserTestResults;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TestResultsRepository extends CrudRepository<UserTestResults, Integer> {
    UserTestResults findByTestTypeAndUsername(String testType, String username);


}
