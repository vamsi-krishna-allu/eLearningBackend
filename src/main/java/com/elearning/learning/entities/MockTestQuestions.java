package com.elearning.learning.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "mock_test_questions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MockTestQuestions {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String testType;
    private String question;
    private String optionsA;
    private String optionsB;
    private String optionsC;
    private String optionsD;
    private String answer;

}
