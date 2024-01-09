package com.flightapp.flightapi.converter;

import com.flightapp.flightapi.dto.AirportResponse;
import com.flightapp.flightapi.dto.FlightResponse;
import com.flightapp.flightapi.entity.Airport;
import com.flightapp.flightapi.entity.Flight;
import com.flightapp.flightapi.exception.AirportException;
import com.flightapp.flightapi.repository.AirportRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@NoArgsConstructor
public class DtoConverter {


    private static AirportRepository airportRepository;

    @Autowired
    public DtoConverter(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    public static AirportResponse convertToAirportResponse(Airport airport){
        return new AirportResponse(airport.getId(), airport.getCity());
    }

    public static FlightResponse convertToFlightResponse(Flight flight, AirportResponse departureAirport, AirportResponse arrivalAirport){
        return new FlightResponse(flight.getId(), flight.getPrice(), departureAirport, arrivalAirport, flight.getDepartureDate(), flight.getArrivalDate());
    }

    public static Flight convertToFlightEntity(FlightResponse flightResponse) {

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

    public static AirportResponse convertAirportToAirportResponse(Airport airport) {
        return new AirportResponse(airport.getId(), airport.getCity());
    }


    public static List<Flight> convertToFlightList(List<FlightResponse> flightResponses) {
        List<Flight> flights = new ArrayList<>();
        for (FlightResponse response : flightResponses) {
            Flight flight = convertToFlightEntity(response);
            flights.add(flight);
        }
        return flights;
    }

    public static FlightResponse convertFlightToFlightResponse(Flight flight) {
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
