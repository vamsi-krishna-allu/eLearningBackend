package com.elearning.learning.service;

import com.elearning.learning.entities.StudentDetails;
import com.elearning.learning.model.Student;
import com.elearning.learning.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JwtRegisterDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder bcryptEncoder;

    public StudentDetails save(Student student) {
        StudentDetails newUser = new StudentDetails();
        newUser.setUsername(student.getUsername());
        newUser.setPassword(bcryptEncoder.encode(student.getPassword()));
        return userRepository.save(newUser);
    }
}
