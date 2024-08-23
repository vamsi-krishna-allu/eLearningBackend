package com.elearning.learning.repository;

import com.elearning.learning.entities.StudentDetails;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<StudentDetails, Integer> {
    StudentDetails findByUsername(String username);

    StudentDetails findByUsernameAndPassword(String username, String password);

    @Modifying
    @Transactional
    @Query("update StudentDetails sd set sd.password = :password where sd.id = :id")
    String updatePassword(@Param(value = "id") long id, @Param(value = "password") String password);
}
