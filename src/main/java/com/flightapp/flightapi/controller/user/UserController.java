package com.flightapp.flightapi.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "REST Apis for user controller", description = "Can get, add flights")
@RestController
@RequestMapping("/user")
public class UserController {

    @Operation(summary = "User Area")
    @GetMapping("/")
    public String getUser(){
        return "User area";
    }

}
