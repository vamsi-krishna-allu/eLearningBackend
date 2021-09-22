package com.elearning.learning.repository;

import com.elearning.learning.entities.UserCourseDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCourseRepository extends CrudRepository<UserCourseDetails, Integer> {
    UserCourseDetails findByUsername(String username);
}
