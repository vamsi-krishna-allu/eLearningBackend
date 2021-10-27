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
    private Integer questionNumber;
    private String testType;
    @Column(columnDefinition = "LONGTEXT")
    private String question;
    @Column(columnDefinition = "LONGTEXT")
    private String optionsA;
    @Column(columnDefinition = "LONGTEXT")
    private String optionsB;
    @Column(columnDefinition = "LONGTEXT")
    private String optionsC;
    @Column(columnDefinition = "LONGTEXT")
    private String optionsD;
    private Integer answer;

}
