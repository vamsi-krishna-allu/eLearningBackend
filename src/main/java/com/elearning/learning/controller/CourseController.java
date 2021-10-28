package com.elearning.learning.controller;

import com.elearning.learning.model.FileResponse;
import com.elearning.learning.service.CourseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.persistence.Convert;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@RestController
@AllArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping(value = "/getPdfFile/{fileName}")
    public ResponseEntity<FileResponse> getPdf(@PathVariable("fileName") String fileName) {
        File file = new File("src\\main\\resources\\"+fileName);

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            String pdfBase64 = Base64.getEncoder().encodeToString(IOUtils.toByteArray(fileInputStream));
            FileResponse response = new FileResponse(fileName, pdfBase64);

            return new ResponseEntity<FileResponse>(response,
                    HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping(value = "/authenticatedCourses")
    public ResponseEntity<List<String>> getAuthenticatedCourses(@RequestParam("username") String username) throws JsonProcessingException {
        List<String> authenticatedCourses = courseService.getAuthenticatedCourses(username);
        return ResponseEntity.ok(authenticatedCourses);
    }

}
