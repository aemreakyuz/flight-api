package com.flightapp.flightapi.converter;

import com.flightapp.flightapi.dto.AirportResponse;
import com.flightapp.flightapi.dto.FlightResponse;
import com.flightapp.flightapi.entity.Airport;
import com.flightapp.flightapi.entity.Flight;
import com.flightapp.flightapi.exception.AirportException;
import com.flightapp.flightapi.repository.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
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

    public Airport convertToAirportEntity(AirportResponse airportResponse){
        Airport airportEntity = airportRepository.findByCity(airportResponse.city());
        if(airportEntity == null){
            airportEntity = new Airport();
            airportEntity.setCity(airportResponse.city());
        }
      return airportEntity;
    }

    public AirportResponse convertAirportToAirportResponse(Airport airport) {
        return new AirportResponse(airport.getId(), airport.getCity());
    }


    public List<Flight> convertToFlightList(List<FlightResponse> flightResponses) {
        List<Flight> flights = new ArrayList<>();
        for (FlightResponse response : flightResponses) {
            DtoConverter dtoConverter = new DtoConverter();
            Flight flight = dtoConverter.convertToFlightEntity(response);
            flights.add(flight);
        }
        return flights;
    }

    public FlightResponse convertFlightToFlightResponse(Flight flight) {
        return new FlightResponse(
                flight.getId(),
                flight.getPrice(),
                convertAirportToAirportResponse(flight.getDepartureAirport()),
                convertAirportToAirportResponse(flight.getArrivalAirport()),
                flight.getDepartureDate(),
                flight.getArrivalDate()
        );
    }

}
