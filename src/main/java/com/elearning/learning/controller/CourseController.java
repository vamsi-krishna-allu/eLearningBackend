package com.elearning.learning.controller;

import com.elearning.learning.model.FileResponse;
import com.elearning.learning.service.CourseService;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@RestController
@AllArgsConstructor
public class CourseController {

    private final CourseService courseService;

//    @GetMapping(value = "/getPdfFile")
//    public ResponseEntity<InputStreamResource> getFile() throws FileNotFoundException {
//
//        String filePath = "src/main/resources/Form16.pdf";
//        File file = new File(filePath);
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("content-disposition", "inline;filename=" +"Form16.pdf");
//
//        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
//
//        return ResponseEntity.ok()
//                .headers(headers)
//                .contentLength(file.length())
//                .contentType(MediaType.parseMediaType("application/pdf"))
//                .body(resource);
//    }

    @GetMapping(value = "/getPdfFile/{fileName}")
    public ResponseEntity<FileResponse> getPdf(@PathVariable("fileName") String fileName) {
        File file = new File("src\\main\\resources\\"+fileName);

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            // return IOUtils.toByteArray(fileInputStream);
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
    public ResponseEntity<List<String>> getAuthenticatedCourses(@RequestParam("username") String username){
        List<String> authenticatedCourses = courseService.getAuthenticatedCourses(username);
        return ResponseEntity.ok(authenticatedCourses);
    }

}
