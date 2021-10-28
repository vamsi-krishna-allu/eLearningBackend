package com.elearning.learning.controller;

import com.elearning.learning.config.JwtTokenUtil;
import com.elearning.learning.model.JwtRequest;
import com.elearning.learning.model.JwtResponse;
import com.elearning.learning.model.Student;
import com.elearning.learning.service.JwtRegisterDetailsService;
import com.elearning.learning.service.JwtUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class JwtAuthenticationController {

    private AuthenticationManager authenticationManager;

    private JwtTokenUtil jwtTokenUtil;

    private JwtUserDetailsService userDetailsService;

    private final JwtRegisterDetailsService jwtRegisterDetailsService;

    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> saveUser(@RequestBody Student student) throws Exception {
         return ResponseEntity.ok(jwtRegisterDetailsService.save(student));
    }

    @PostMapping(value = "/changePassword")
    public ResponseEntity<?> updatePassword(@RequestBody Student student) throws Exception {
        return ResponseEntity.ok(jwtRegisterDetailsService.updatePassword(student));
    }
}
