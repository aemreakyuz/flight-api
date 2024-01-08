package com.flightapp.flightapi.controller;


import com.flightapp.flightapi.converter.DtoConverter;
import com.flightapp.flightapi.dto.AirportResponse;
import com.flightapp.flightapi.entity.Airport;
import com.flightapp.flightapi.entity.Flight;
import com.flightapp.flightapi.service.AirportService;
import com.flightapp.flightapi.service.FlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "REST Apis for airport controller", description = "Can get, add, delete airport")
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

    @Operation(summary = "Get all the airports")
    @ApiResponse(responseCode = "200", description = "If there are any airports it returns status 200 otherwise it returns 404")
    @GetMapping("/")
    public List<Airport> findAll(){
        return airportService.findAll();
    }

    @Operation(summary = "Search airport with its id")
    @ApiResponse(responseCode = "200", description = "If there is a match it returns status 200 otherwise it returns 404")
    @GetMapping("/{id}")
    public Airport findById(@PathVariable Long id){
        return  airportService.findById(id);
    }

    @Operation(summary = "Search airport with its id")
    @ApiResponse(responseCode = "200", description = "If there is a match it returns status 200 otherwise it returns 404")
    @PostMapping("")
    public AirportResponse addAirport(@RequestBody Airport airport){
       airportService.addNewAirport(airport);
       return DtoConverter.convertToAirportResponse(airport);
    }

    @Operation(summary = "Search airport with its id")
    @ApiResponse(responseCode = "200", description = "If there is a match it returns status 200 otherwise it returns 404")
    @DeleteMapping("/{id}")
    public AirportResponse deleteAirport(@PathVariable Long id) {
        return airportService.deleteAirportById(id);
    }

    @Operation(summary = "Search flights in an airport with airport name")
    @ApiResponse(responseCode = "200", description = "If there is a match it returns status 200 otherwise it returns empty list")
    @GetMapping("/{city}/flights")
    public List<Flight> getFlights(@PathVariable String city){
        Airport airport = airportService.findByCity(city);
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
