package com.ftn.sbnz.service;
import com.ftn.sbnz.dto.LoginDTO;
import com.ftn.sbnz.exception.UserNotFoundException;
import com.ftn.sbnz.model.User;
import com.ftn.sbnz.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean login(LoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User with this email not found!"));
        return user.getPassword().equals(loginDTO.getPassword());
    }
}
