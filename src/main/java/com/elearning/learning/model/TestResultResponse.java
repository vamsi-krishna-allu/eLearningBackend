package com.elearning.learning.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class TestResultResponse implements Serializable {

    private static final long serialVersionUID = 5926468583005150707L;

    private int marksScored;
    private int totalMarks;
    private int attemptedQuestions;
    private int timeTaken;
    private String startTime;
    private String endTime;
}
