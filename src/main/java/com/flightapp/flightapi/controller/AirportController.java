package com.flightapp.flightapi.controller;


import com.flightapp.flightapi.entity.Airport;
import com.flightapp.flightapi.service.AirportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/airport")
public class AirportController {

    private AirportService airportService;

    @Autowired

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    @GetMapping("/")
    public List<Airport> findAll(){
        return airportService.findAll();
    }

    @GetMapping("/{id}")
    public Airport findById(@PathVariable Long id){
        return  airportService.findById(id);
    }

    @PostMapping("/addAirport")
    public Airport addAirport(@RequestBody Airport airport){
        return airportService.addNewAirport(airport);
    }

}
