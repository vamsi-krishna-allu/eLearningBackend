package com.elearning.learning.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "user_courses")
@Getter
@Setter
public class UserCourseDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String username;
    @ElementCollection
    @Column
    private List<String> allowedCourses;
    @ElementCollection
    @Column
    private List<String> allowedMockTests;


}
