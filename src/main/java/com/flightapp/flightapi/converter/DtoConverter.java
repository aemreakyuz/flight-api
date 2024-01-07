package com.flightapp.flightapi.converter;

import com.flightapp.flightapi.dto.AirportResponse;
import com.flightapp.flightapi.entity.Airport;

public class DtoConverter {

    public static AirportResponse convertToAirportResponse(Airport airport){
        return new AirportResponse(airport.getId(), airport.getCity());
    }
}
