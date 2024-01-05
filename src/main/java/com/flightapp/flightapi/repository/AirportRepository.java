package com.flightapp.flightapi.repository;

import com.flightapp.flightapi.entity.Airport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirportRepository extends JpaRepository<Airport, Long> {
}
