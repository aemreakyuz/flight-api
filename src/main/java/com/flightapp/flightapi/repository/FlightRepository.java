package com.flightapp.flightapi.repository;

import com.flightapp.flightapi.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<Flight, Long> {
}
