package com.elearning.learning.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class FileResponse implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;
    private String fileName;
    private String fileContent;
}
