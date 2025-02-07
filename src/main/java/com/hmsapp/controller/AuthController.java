package com.hmsapp.controller;

import com.hmsapp.entity.User;
import com.hmsapp.payload.JwtToken;
import com.hmsapp.payload.LoginDto;
import com.hmsapp.payload.ProfileDto;
import com.hmsapp.payload.UserDto;
import com.hmsapp.repository.UserRepository;
import com.hmsapp.service.JWTService;
import com.hmsapp.service.OTPService;
import com.hmsapp.service.SMSService;
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
    private OTPService otpService;
    private UserRepository userRepository;
    private SMSService smsservice;
    private JWTService jwtService;

    public AuthController(UserService userService, OTPService otpService, UserRepository userRepository, SMSService smsservice, JWTService jwtService) {
        this.userService = userService;
        this.otpService = otpService;
        this.userRepository = userRepository;
        this.smsservice = smsservice;
        this.jwtService = jwtService;
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
    @PostMapping("/login-otp")
    public ResponseEntity<?> loginWithOtp(@RequestParam String mobile) {
        Optional<User> byMobile = userRepository.findByMobile(mobile);
        if(byMobile.isPresent()) {
            String otp = otpService.generateOTP(mobile);

            if(otp!=null){
                smsservice.sendSMS("+917676150798", "Your One Time Password (OTP) is: " + otp);  // sending otp to user's mobile number'
                return new ResponseEntity<>("Check your mobile for the OPT.",HttpStatus.OK);
            }
        }

        return new ResponseEntity<>("Invalid mobile number",HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String mobile,
    @RequestParam String otp) {
        Optional<User> byMobile = userRepository.findByMobile(mobile);
        if(byMobile.isPresent()) {
            User user = byMobile.get();
            if (otpService.verifyOTP(mobile, otp)) {
                String token = jwtService.generateToken(user.getUsername());
                return "Login successful\nHere is your token:\n" + token;
            } else {
                return "Invalid attempt";
            }
        }
        return "Invalid mobile number";
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
