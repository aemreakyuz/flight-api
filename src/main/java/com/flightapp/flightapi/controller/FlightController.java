package com.flightapp.flightapi.controller;


import com.flightapp.flightapi.entity.Flight;
import com.flightapp.flightapi.service.AirportService;
import com.flightapp.flightapi.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flights")
public class FlightController {

    private FlightService flightService;
    private AirportService airportService;


    @Autowired
    public FlightController(FlightService flightService, AirportService airportService) {
        this.flightService = flightService;
        this.airportService = airportService;
    }


    @PostMapping("/addFlight")
    public Flight addFlight(@RequestBody Flight flight) {
        return flightService.addFlight(flight);
    }
}
