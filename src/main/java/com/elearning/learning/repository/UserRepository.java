package com.elearning.learning.repository;

import com.elearning.learning.entities.StudentDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<StudentDetails, Integer> {
    StudentDetails findByUsername(String username);
}
