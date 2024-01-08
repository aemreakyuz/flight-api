package com.flightapp.flightapi.service;

import com.flightapp.flightapi.converter.DtoConverter;
import com.flightapp.flightapi.dto.FlightResponse;
import com.flightapp.flightapi.dto.RoundTripFlightResponse;
import com.flightapp.flightapi.entity.Airport;
import com.flightapp.flightapi.entity.Flight;
import com.flightapp.flightapi.repository.AirportRepository;
import com.flightapp.flightapi.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FlightService {

    private FlightRepository flightRepository;
    private AirportRepository airportRepository;


    @Autowired
    public FlightService(FlightRepository flightRepository, AirportRepository airportRepository) {
        this.flightRepository = flightRepository;
        this.airportRepository = airportRepository;
    }

    public List<RoundTripFlightResponse> searchFlightsWithReturn(LocalDate departureDate, LocalDate arrivalDate, String departureAirport, String arrivalAirport) {
        List<Flight> flights = flightRepository.searchReturnFlights(departureDate, arrivalDate, departureAirport, arrivalAirport);
        List<RoundTripFlightResponse> response = new ArrayList<>();
        for(Flight flight : flights){

            FlightResponse departingFlight = DtoConverter.convertToFlightResponse(flight, departureAirport, arrivalAirport);

            LocalDate newDepartureDate = departingFlight.arrivalDate();

            FlightResponse arrivingFlight = DtoConverter.convertToFlightResponse(flight, arrivalAirport, departureAirport).withDepartureDate(newDepartureDate);

            RoundTripFlightResponse roundTrips = new RoundTripFlightResponse(departingFlight, arrivingFlight);
            response.add(roundTrips);
        }
        return response;
    }

    public List<FlightResponse> searchFlightsOneWay(LocalDate departureDate, String departureAirport, String arrivalAirport) {
        List<Flight> flights = flightRepository.searchOneWayFlights(departureDate, departureAirport, arrivalAirport);
        List<FlightResponse> flightResponses = new ArrayList<>();

        for (Flight flight : flights) {
            FlightResponse flightResponse = DtoConverter.convertToFlightResponse(flight, departureAirport, arrivalAirport);
            flightResponses.add(flightResponse);
        }

        return flightResponses;
    }

    public Flight findById(Long id) {
        Optional<Flight> flightOptional = flightRepository.findById(id);
        return flightOptional.orElse(null);

        //TODO => Error Handling, dto converter
    }

    public FlightResponse addFlight(Flight flight) {
        Optional<Airport> departureAirportOptional = airportRepository.findById(flight.getDepartureAirport().getId());
        Optional<Airport> arrivalAirportOptional = airportRepository.findById(flight.getArrivalAirport().getId());

        if (departureAirportOptional.isPresent() & arrivalAirportOptional.isPresent()) {
            Airport departureAirport = departureAirportOptional.get();
            Airport arrivalAirport = arrivalAirportOptional.get();
            flightRepository.save(flight);
            return DtoConverter.convertToFlightResponse(flight, departureAirport.getCity(), arrivalAirport.getCity());
        }

        return null;
    }

    public Flight removeFlightById(Long flightId) {
        Flight flightToRemove = findById(flightId);
        flightRepository.delete(flightToRemove);
        return flightToRemove;
    }


    //Flight objesi parametre,
    public Flight updateFlight(Long flightId, BigDecimal price, Airport departureAirport, Airport arrivalAirport, LocalDate departureDate, LocalDate arrivalDate) {
        Optional<Flight> flightToUpdate = flightRepository.findById(flightId);
        if (flightToUpdate.isPresent()) {
            Flight existingFlight = flightToUpdate.get();
            if (price != null) {
                existingFlight.setPrice(price);
            }
            if (departureAirport != null) {
                existingFlight.setDepartureAirport(departureAirport);
            }
            if (arrivalAirport != null) {
                existingFlight.setArrivalAirport(arrivalAirport);
            }
            if (departureDate != null) {
                existingFlight.setDepartureDate(departureDate);
            }
            if (arrivalDate != null) {
                existingFlight.setArrivalDate(arrivalDate);
            }
            return flightRepository.save(existingFlight);
        }

        return null;
    }


    public List<Flight> findAll() {
        return flightRepository.findAll();
    }



}
