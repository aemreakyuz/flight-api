package com.flightapp.flightapi.converter;

import com.flightapp.flightapi.dto.AirportResponse;
import com.flightapp.flightapi.dto.FlightResponse;
import com.flightapp.flightapi.entity.Airport;
import com.flightapp.flightapi.entity.Flight;
import com.flightapp.flightapi.exception.AirportException;
import com.flightapp.flightapi.repository.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public class DtoConverter {

    @Autowired
    private AirportRepository airportRepository;

    public static AirportResponse convertToAirportResponse(Airport airport){
        return new AirportResponse(airport.getId(), airport.getCity());
    }

    public static FlightResponse convertToFlightResponse(Flight flight, AirportResponse departureAirport, AirportResponse arrivalAirport){
        return new FlightResponse(flight.getId(), flight.getPrice(), departureAirport, arrivalAirport, flight.getDepartureDate(), flight.getArrivalDate());
    }

    public Flight convertToFlightEntity(FlightResponse flightResponse) {
        Flight newFlight = new Flight();
        newFlight.setId(flightResponse.id());
        newFlight.setPrice(flightResponse.price());

        Airport departureAirport = airportRepository.findByCity(flightResponse.departureAirport().city());
        if (departureAirport != null) {
            newFlight.setDepartureAirport(departureAirport);
        }else{
            throw new AirportException("Arrival airport not found", HttpStatus.NOT_FOUND);
        }

        Airport arrivalAirport = airportRepository.findByCity(flightResponse.arrivalAirport().city());
        if (arrivalAirport != null) {
            newFlight.setArrivalAirport(arrivalAirport);
        }else{
            throw new AirportException("Arrival airport not found", HttpStatus.NOT_FOUND);
        }

        newFlight.setDepartureDate(flightResponse.departureDate());
        newFlight.setArrivalDate(flightResponse.arrivalDate());

        return newFlight;
    }
}
