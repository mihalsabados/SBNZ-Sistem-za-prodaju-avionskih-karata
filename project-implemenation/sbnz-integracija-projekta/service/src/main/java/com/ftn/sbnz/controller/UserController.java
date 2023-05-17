package com.ftn.sbnz.controller;
import com.ftn.sbnz.dto.LoginDTO;
import com.ftn.sbnz.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String,Boolean>> checkCredentials(@RequestBody LoginDTO loginDTO) {
        boolean credentialsValid = userService.login(loginDTO);
        Map<String, Boolean> response = new HashMap<>();
        response.put("credentialsValid", credentialsValid);

        if (!credentialsValid) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
}
