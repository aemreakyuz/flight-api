package com.flightapp.flightapi.controller.user;

import com.flightapp.flightapi.dto.RegisterUser;
import com.flightapp.flightapi.entity.user.ApplicationUser;
import com.flightapp.flightapi.service.authentication.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthenticationService authenticationService;

    @Autowired
    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ApplicationUser register(@RequestBody RegisterUser registerUser){
        return authenticationService
                .register(registerUser.fullName(), registerUser.email(), registerUser.password());
    }

}
