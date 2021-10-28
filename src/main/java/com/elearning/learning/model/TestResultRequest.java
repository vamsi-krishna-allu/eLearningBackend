package com.elearning.learning.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TestResultRequest implements Serializable {

    private static final long serialVersionUID = 5926468583005150707L;

    private String username;
    private String testName;
    private List<Integer> answer;
    private Integer timeTaken;
    private String startTime;
    private String endTime;
}
