package com.flightapp.flightapi.controller;


import com.flightapp.flightapi.dto.AirportResponse;
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
    public AirportResponse addAirport(@RequestBody Airport airport){
       airportService.addNewAirport(airport);
       return new AirportResponse(airport.getId(), airport.getCity());
    }

    @DeleteMapping("/{id}")
    public AirportResponse deleteAirport(@PathVariable Long id) {

        return airportService.deleteAirportById(id);
    }

}
