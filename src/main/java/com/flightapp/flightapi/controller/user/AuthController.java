package com.flightapp.flightapi.controller.user;

import com.flightapp.flightapi.dto.RegisterUser;
import com.flightapp.flightapi.entity.user.ApplicationUser;
import com.flightapp.flightapi.service.authentication.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "REST Apis for authorization controller", description = "Can register user")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthenticationService authenticationService;

    @Autowired
    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Operation(summary = "User register area. ")
    @PostMapping("/register")
    public ApplicationUser register(@RequestBody RegisterUser registerUser){
        return authenticationService
                .register(registerUser.fullName(), registerUser.email(), registerUser.password());
    }

    @Operation(summary = "Get all the users if the user is Admin")
    @GetMapping("/users")
    public List<ApplicationUser> getUserList(){
        return authenticationService.getUserList();
    }


}
