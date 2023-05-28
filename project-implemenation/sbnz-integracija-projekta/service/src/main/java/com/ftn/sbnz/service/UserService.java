package com.ftn.sbnz.service;
import com.ftn.sbnz.dto.LoginDTO;
import com.ftn.sbnz.exception.UserIsBlockedException;
import com.ftn.sbnz.exception.UserNotFoundException;
import com.ftn.sbnz.model.User;
import com.ftn.sbnz.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User login(LoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User with this email not found!"));
        if(user.getPassword().equals(loginDTO.getPassword()) && !user.isBlocked())
            return user;
        if(user.isBlocked())
            throw new UserIsBlockedException("User is blocked exception");
        else
            throw new BadCredentialsException("Bad credentials");

    }

    public User getUserByEmail(String payerEmail) {
        return this.userRepository.findByEmail(payerEmail).orElseThrow(() ->
                new UserNotFoundException("User with this email not found!"));
    }

    public String getUserLoyaltyStatus(String email) {
        return this.getUserByEmail(email).getLoyaltyStatus().name();
    }
}
