package com.flightapp.flightapi.repository;

import com.flightapp.flightapi.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {




    @Query("SELECT f FROM Flight f " +
            "WHERE f.departureDate = :departureDate " +
            "AND f.arrivalDate = :arrivalDate " +
            "AND f.arrivalAirport.id = (SELECT a.id FROM Airport a WHERE a.city = :arrivalAirport) " +
            "AND f.departureAirport.id = (SELECT a.id FROM Airport a WHERE a.city = :departureAirport)")
    List<Flight> searchReturnFlights(
                                     @RequestParam("departureDate") LocalDate departureDate,
                                     @RequestParam("arrivalDate") LocalDate arrivalDate,
                                     @RequestParam("arrivalAirport") String arrivalAirport,
                                     @RequestParam("departureAirport") String departureAirport);


    @Query("SELECT f FROM Flight f " +
            "WHERE f.departureDate = :departureDate " +
            "AND f.arrivalAirport.id = (SELECT a.id FROM Airport a WHERE a.city = :arrivalAirport) " +
            "AND f.departureAirport.id = (SELECT a.id FROM Airport a WHERE a.city = :departureAirport)")
    List<Flight> searchOneWayFlights(@RequestParam("departureDate") LocalDate departureDate,
                                             @RequestParam("arrivalAirport") String arrivalAirport,
                                             @RequestParam("departureAirport") String departureAirport);


}
