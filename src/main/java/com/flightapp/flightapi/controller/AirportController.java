package com.flightapp.flightapi.controller;


import com.flightapp.flightapi.converter.DtoConverter;
import com.flightapp.flightapi.dto.AirportResponse;
import com.flightapp.flightapi.entity.Airport;
import com.flightapp.flightapi.entity.Flight;
import com.flightapp.flightapi.service.AirportService;
import com.flightapp.flightapi.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/airport")
public class AirportController {

    private AirportService airportService;

    private FlightService flightService;

    @Autowired
    public AirportController(AirportService airportService, FlightService flightService) {
        this.airportService = airportService;
        this.flightService = flightService;
    }


    @GetMapping("/")
    public List<Airport> findAll(){
        return airportService.findAll();
    }

    @GetMapping("/{id}")
    public Airport findById(@PathVariable Long id){
        return  airportService.findById(id);
    }


    @PostMapping("")
    public AirportResponse addAirport(@RequestBody Airport airport){
       airportService.addNewAirport(airport);
       return DtoConverter.convertToAirportResponse(airport);
    }

    @DeleteMapping("/{id}")
    public AirportResponse deleteAirport(@PathVariable Long id) {
        return airportService.deleteAirportById(id);
    }

    @GetMapping("/{id}/flights")
    public List<Flight> getFlights(@PathVariable Long id){
        Airport airport = airportService.findById(id);
        if (airport != null){
            List<Flight> flights = flightService.findAll();
            if (flights != null) {
                return flights.stream()
                        .filter(flight -> flight.getArrivalAirport().equals(airport) || flight.getDepartureAirport().equals(airport))
                        .collect(Collectors.toList());
            } else {
                return Collections.emptyList();
            }
        } else {
            return Collections.emptyList();
        }

    }

}
