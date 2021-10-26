package com.elearning.learning.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user_test_results")
@Getter
@Setter
public class UserTestResults {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String username;
    @Column
    private String testType;
    @Column
    private Integer attemptedQuestions;
    @Column
    private Integer correctAnswers;
    @Column
    private Integer timeTaken;
    
}
