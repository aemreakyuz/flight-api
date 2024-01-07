package com.flightapp.flightapi.repository;

import com.flightapp.flightapi.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    List<Flight> findByDepartureDateAfter(LocalDate currentDate);
    List<Flight> findByArrivalDateBefore(LocalDate currentDate);



}
