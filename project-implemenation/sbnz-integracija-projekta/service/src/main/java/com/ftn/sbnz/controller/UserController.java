package com.ftn.sbnz.controller;
import com.ftn.sbnz.dto.LoginDTO;
import com.ftn.sbnz.exception.UserIsBlockedException;
import com.ftn.sbnz.model.User;
import com.ftn.sbnz.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import lombok.*;

import javax.websocket.server.PathParam;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> checkCredentials(@RequestBody LoginDTO loginDTO) {
        try {
            User user = userService.login(loginDTO);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UserIsBlockedException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        catch (BadCredentialsException e){
            return ResponseEntity.badRequest().build();
        }

    }

    @GetMapping("/get-status/{email}")
    public ResponseEntity<?> getUserLoyaltyStatus(@PathVariable String email) {
        String loyaltyStatus = userService.getUserLoyaltyStatus(email);
        return new ResponseEntity<>(loyaltyStatus, HttpStatus.OK);
    }
}
