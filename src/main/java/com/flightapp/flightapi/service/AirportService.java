package com.flightapp.flightapi.service;

import com.flightapp.flightapi.dto.AirportResponse;
import com.flightapp.flightapi.entity.Airport;

import java.util.List;

public interface AirportService {

    //TODO
    // addNewAirport()
    // deleteAirportById()
    // findById()

    AirportResponse addNewAirport(Airport airport);

    AirportResponse deleteAirportById(Long id);

    Airport findById(Long id);

    List<Airport> findAll();

}
