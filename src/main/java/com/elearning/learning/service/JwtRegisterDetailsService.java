package com.elearning.learning.service;

import com.elearning.learning.entities.StudentDetails;
import com.elearning.learning.model.Student;
import com.elearning.learning.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public String updatePassword(Student student) throws Exception {
        StudentDetails studentDetails = userRepository.findByUsernameAndPassword(student.getUsername(), bcryptEncoder.encode(student.getPassword()));
        if(studentDetails == null){
            throw new Exception("Incorrect your username or password");
        }
        return userRepository.updatePassword(studentDetails.getId(), bcryptEncoder.encode(student.getPassword()));
    }
}
