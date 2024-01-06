package com.flightapp.flightapi.service;

import com.flightapp.flightapi.entity.Airport;

public interface AirportService {

    //TODO
    // addNewAirport()
    // deleteAirportById()
    // findById()

    Airport addNewAirport(Airport airport);

    Airport deleteAirportById(Long id);

    Airport findById(Long id);


}
