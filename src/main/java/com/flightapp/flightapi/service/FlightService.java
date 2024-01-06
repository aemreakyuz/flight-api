package com.flightapp.flightapi.service;

import com.flightapp.flightapi.entity.Airport;
import com.flightapp.flightapi.entity.Flight;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface FlightService {

    //TODO
    // addFlight(), removeFlight(), updateFlight()
    // Airport findDepartureAirportById(), Airport findArrivalAirportById()
    // List<Flight> findDepartureDate(), List<Flight> findArrivalDate()
    // List<Flight> findAll()
    // List<Flight> searchFlights(depDate, arrDate) => if arrDate=null its is one-way

    Flight findById(Long flightId);

    Flight addFlight(Flight flight);

    Flight removeFlightById(Long flightId);

    Flight updateFlight(Long flightId,  BigDecimal price, Airport departureAirport, Airport arrivalAirport, LocalDate departureDate, LocalDate arrivalDate);

    Airport findDepartureAirportByFlightId(Long flightId);

    Airport findArrivalAirportByFlightId(Long flightId);

    LocalDate findDepartureDateById(Long id);

    LocalDate findArrivalDateById(Long id);

    List<Flight> findAll();

    List<Flight> findDepartingFlights();

    List<Flight> findArrivingFlights();

    List<Flight> searchFlightsWithOneWayOrToWay(LocalDate departureDate, LocalDate arrivalDate);

}
