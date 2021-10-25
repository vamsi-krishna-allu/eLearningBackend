package com.elearning.learning.repository;

import com.elearning.learning.entities.UserTestResults;
import org.springframework.data.repository.CrudRepository;

public interface TestResultsRepository extends CrudRepository<UserTestResults, Integer> {
    UserTestResults findByTestTypeAndUsername(String testType, String username);


}
