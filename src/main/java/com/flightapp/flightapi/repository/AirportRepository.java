package com.flightapp.flightapi.repository;

import com.flightapp.flightapi.entity.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AirportRepository extends JpaRepository<Airport, Long> {


    @Query("SELECT a FROM Airport a WHERE a.city = :city")
    Airport findByCity(String city);

}
