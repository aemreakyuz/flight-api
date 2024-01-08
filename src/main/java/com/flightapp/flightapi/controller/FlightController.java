package com.flightapp.flightapi.controller;


import com.flightapp.flightapi.dto.FlightResponse;
import com.flightapp.flightapi.dto.RoundTripFlightResponse;
import com.flightapp.flightapi.entity.Flight;
import com.flightapp.flightapi.service.AirportService;
import com.flightapp.flightapi.service.FlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "REST Apis for flight controller", description = "Can get, add, update, delete flights")
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


    @Operation(summary = "Search flights. One-way or Two-way", description = "Send the necessary details. If arrival date is not sent. It will return a one way ticket. Otherwise it will return two tickets: First one is for leaving and the second one is for return ticket")
    @ApiResponse(responseCode = "200", description = "If the necessary parameters are valid it returns status code 200")
    @PostMapping("/search")
    public List<?> searchFlights(@RequestParam(name = "departureDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate, @RequestParam(name = "arrivalDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate arrivalDate, @RequestParam(name = "departureAirport") String departureAirport, @RequestParam(name = "arrivalAirport") String arrivalAirport
    ) {
        if(arrivalDate != null){
            List<RoundTripFlightResponse> returnValues = flightService.searchFlightsWithReturn(departureDate, arrivalDate, departureAirport, arrivalAirport);
            return returnValues;
        }
        else{
            List<FlightResponse> returnValues = flightService.searchFlightsOneWay(departureDate, departureAirport, arrivalAirport);
            return returnValues;
        }
    }

    @Operation(summary = "Add One Way or Two Way Flights", description = "Send flight object and it will be added")
    @ApiResponse(responseCode = "200", description = "If Flight object is correctly sent it returns status code 200")
    @PostMapping("")
    public List< ? > addFlight(@RequestBody Flight flight) {
        if(flight.getArrivalDate() == null){
            flight.setArrivalDate(null);
            return flightService.addFlight(flight);
        }
        return flightService.addFlight(flight);
    }

    @Operation(summary = "Update flight info", description = "Send the desired flight object. some fields may be null")
    @PutMapping("/{id}")
    public FlightResponse update(@PathVariable Long id, @RequestBody Flight flightDetails){
       return flightService.updateFlight(id, flightDetails);
    }


}
