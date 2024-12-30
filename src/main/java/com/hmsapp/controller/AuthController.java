package com.hmsapp.controller;

import com.hmsapp.entity.User;
import com.hmsapp.payload.JwtToken;
import com.hmsapp.payload.LoginDto;
import com.hmsapp.payload.ProfileDto;
import com.hmsapp.payload.UserDto;
import com.hmsapp.repository.UserRepository;
import com.hmsapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    //used for user sign-up
    @PostMapping("/sign-up")
    public ResponseEntity<?> createUser(@RequestBody UserDto dto) {
       return userService.createUser(dto);
    }
    @PostMapping("/property/sign-up")
    public ResponseEntity<?> createPropertyOwner(@RequestBody UserDto dto) {
       return userService.createPropertyOwner(dto);
    }
    @PostMapping("/blog/sign-up")
    public ResponseEntity<?> createBlogManager(@RequestBody UserDto dto) {
       return userService.createBlogManager(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        String token = userService.verifyLogin(loginDto);
        JwtToken jwtToken = new JwtToken();
        jwtToken.setToken(token);
        jwtToken.setType("JWT");
        if(token != null) {
            return new  ResponseEntity<>(jwtToken,HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping
    public ResponseEntity<ProfileDto> userProfile(
            @AuthenticationPrincipal User user
    ) {
        ProfileDto profile = new ProfileDto();
        profile.setName(user.getName());
        profile.setEmail(user.getEmail());
        profile.setUsername(user.getUsername());
        return new ResponseEntity<>(profile,HttpStatus.OK);
    }
}
