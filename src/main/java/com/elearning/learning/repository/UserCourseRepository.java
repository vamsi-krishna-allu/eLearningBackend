package com.elearning.learning.repository;

import com.elearning.learning.entities.UserCourseDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCourseRepository extends CrudRepository<UserCourseDetails, Integer> {
    List<UserCourseDetails> findByUsername(String username);
}
