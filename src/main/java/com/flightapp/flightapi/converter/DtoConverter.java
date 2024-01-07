package com.flightapp.flightapi.converter;

import com.flightapp.flightapi.dto.AirportResponse;
import com.flightapp.flightapi.dto.FlightResponse;
import com.flightapp.flightapi.entity.Airport;
import com.flightapp.flightapi.entity.Flight;

public class DtoConverter {

    public static AirportResponse convertToAirportResponse(Airport airport){
        return new AirportResponse(airport.getId(), airport.getCity());
    }

    public static FlightResponse convertToFlightResponse(Flight flight, Airport departureAirport, Airport arrivalAirport){
        return new FlightResponse(flight.getId(), flight.getPrice(), convertToAirportResponse(departureAirport), convertToAirportResponse(arrivalAirport));
    }


}
