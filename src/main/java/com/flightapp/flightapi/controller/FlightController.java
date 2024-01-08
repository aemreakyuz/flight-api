package com.flightapp.flightapi.controller;


import com.flightapp.flightapi.dto.FlightResponse;
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


//    @GetMapping("/search")
//    public String searchFlights(@RequestParam(name = "departureAirport") String departureAirport, @RequestParam(name = "arrivalAirport") String arrivalAirport,
//                                      @RequestParam(name = "departureDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate, @RequestParam(name = "arrivalDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate arrivalDate) {
//       List<Flight> returnValues = flightService.searchFlights(departureAirport, arrivalAirport, departureDate, arrivalDate);
//      return returnValues.size() + "";
//    }

    @GetMapping("/search")
    public List<Flight> searchFlights(@RequestParam(name = "departureAirport") String departureAirport, @RequestParam(name = "arrivalAirport") String arrivalAirport,
                                      @RequestParam(name = "departureDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate) {


return null;
    }

    @PostMapping("")
    public FlightResponse addFlight(@RequestBody Flight flight) {
        return flightService.addFlight(flight);
    }

}
