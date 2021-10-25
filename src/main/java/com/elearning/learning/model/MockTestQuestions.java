package com.elearning.learning.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MockTestQuestions {
    private Integer id;
    private Integer questionNumber;
    private String testType;
    private String question;
    private String optionsA;
    private String optionsB;
    private String optionsC;
    private String optionsD;
    private Integer answer;
}
