package com.elearning.learning.controller;

import com.elearning.learning.service.CourseService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping(value = "/getPdfFile")
    public ResponseEntity<InputStreamResource> getFile() throws FileNotFoundException {

        String filePath = "src/main/resources/Form16.pdf";
        File file = new File(filePath);
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-disposition", "inline;filename=" +"Form16.pdf");

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(resource);
    }

    @GetMapping(value = "/authenticatedCourses")
    public ResponseEntity<List<String>> getAuthenticatedCourses(@RequestParam("username") String username){
        List<String> authenticatedCourses = courseService.getAuthenticatedCourses(username);
        return ResponseEntity.ok(authenticatedCourses);
    }

}
