package com.example.biznes.user;

import com.example.biznes.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public void register(User user){
        user.setRole(Role.USER);
        userRepository.save(user);

    }
    public User findByEmail(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email);
    }
}
