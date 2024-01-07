package com.flightapp.flightapi.repository;

import com.flightapp.flightapi.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {


    @Query("SELECT price FROM flightapp.flight\n" +
            "WHERE\n" +
            "arrival_date = ?1\n" +
            "AND departure_date = ?2\n" +
            "AND arrival_airport_id = (SELECT flightapp.airport.id FROM flightapp.airport WHERE city = ?3)\n" +
            "AND departure_airport_id  = (SELECT flightapp.airport.id FROM flightapp.airport WHERE city = ?4)")
    List<Flight> searchReturnFlights(String arrivalDate, String departureDate, String arrivalAirport, String departureAirport);


    @Query("SELECT price FROM flightapp.flight\n" +
            "WHERE\n" +
            "AND departure_date = ?2\n" +
            "AND arrival_airport_id = (SELECT flightapp.airport.id FROM flightapp.airport WHERE city = ?3)\n" +
            "AND departure_airport_id  = (SELECT flightapp.airport.id FROM flightapp.airport WHERE city = ?4)")
    List<Flight> searchOneWayFlights( String departureDate, String arrivalAirport, String departureAirport);






}
