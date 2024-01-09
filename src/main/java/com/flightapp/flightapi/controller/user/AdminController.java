package com.flightapp.flightapi.controller.user;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "REST Apis for admin controller", description = "Can get, add, delete, update airport and flights")
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Operation(summary = "Admin area")
    @GetMapping("/")
    public String admin(){
        return "This is admin area";
    }

}
