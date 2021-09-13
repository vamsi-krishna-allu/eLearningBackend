package com.elearning.learning.controller;

import com.elearning.learning.model.EmailMessage;
import com.elearning.learning.service.ContactService;
import com.elearning.learning.service.CourseService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@AllArgsConstructor
public class CourseController {

    private final CourseService contactService;

    @GetMapping(value = "/getPdfFile/{FileName}", produces = "application/pdf")
    public ResponseEntity<InputStreamResource> getFile(@PathVariable String FileName) throws IOException {
        ClassPathResource pdfFile = new ClassPathResource("pdfSample.pdf");
        return ResponseEntity.ok().contentLength(pdfFile.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(pdfFile.getInputStream()));

    }

}
