package com.flightapp.flightapi.controller;


import com.flightapp.flightapi.dto.FlightResponse;
import com.flightapp.flightapi.dto.RoundTripFlightResponse;
import com.flightapp.flightapi.entity.Flight;
import com.flightapp.flightapi.service.AirportService;
import com.flightapp.flightapi.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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


    @PostMapping("")
    public FlightResponse addFlight(@RequestBody Flight flight) {
        if(flight.getArrivalDate() == null){
            flight.setArrivalDate(null);
            return flightService.addFlight(flight);
        }
        return flightService.addFlight(flight);
    }

}
