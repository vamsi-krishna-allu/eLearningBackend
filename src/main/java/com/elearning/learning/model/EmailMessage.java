package com.elearning.learning.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmailMessage {
    private String message;
    private String username;
    private String mailId;
    private String subject;
}
