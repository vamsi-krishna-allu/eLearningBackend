package com.elearning.learning.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserTestDetails {
    private List<String> alloweedMockTests;
    private List<String> submittedMockTests;
}
