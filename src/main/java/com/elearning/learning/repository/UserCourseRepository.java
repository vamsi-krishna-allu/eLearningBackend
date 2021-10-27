package com.elearning.learning.repository;

import com.elearning.learning.entities.UserCourseDetails;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserCourseRepository extends CrudRepository<UserCourseDetails, Integer> {
    UserCourseDetails findByUsername(String username);

    @Modifying
    @Transactional
    @Query("update UserCourseDetails u set u.allowedCourses = :allowedCourses, u.allowedMockTests = :allowedMockTests where u.username = :username")
    void updateAllowedCourseAndTests(@Param(value = "username") String username, @Param(value = "allowedCourses") String allowedCourses,
                                     @Param(value = "allowedMockTests") String allowedMockTests);

    @Modifying
    @Transactional
    @Query("update UserCourseDetails u set u.allowedMockTests = :allowedMockTests where u.username = :username")
    void updateAllowedTest(@Param(value = "username") String username,
                                     @Param(value = "allowedMockTests") String allowedMockTests);

    @Modifying
    @Transactional
    @Query("update UserCourseDetails u set u.submittedMockTests = :submittedMockTests where u.username = :username")
    void updateSubmittedTest(@Param(value = "username") String username,
                           @Param(value = "submittedMockTests") String submittedMockTests);
}
