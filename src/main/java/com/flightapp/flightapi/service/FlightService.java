package com.flightapp.flightapi.service;

import com.flightapp.flightapi.converter.DtoConverter;
import com.flightapp.flightapi.dto.FlightResponse;
import com.flightapp.flightapi.dto.RoundTripFlightResponse;
import com.flightapp.flightapi.entity.Airport;
import com.flightapp.flightapi.entity.Flight;
import com.flightapp.flightapi.exception.FlightException;
import com.flightapp.flightapi.repository.AirportRepository;
import com.flightapp.flightapi.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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

    public List<?> addFlight(Flight flight) {

        Optional<Airport> departureAirportOptional = airportRepository.findById(flight.getDepartureAirport().getId());
        Optional<Airport> arrivalAirportOptional = airportRepository.findById(flight.getArrivalAirport().getId());

        if (departureAirportOptional.isPresent() && arrivalAirportOptional.isPresent()) {
            Airport departureAirport = departureAirportOptional.get();
            Airport arrivalAirport = arrivalAirportOptional.get();

            Flight savedDepartureFlight = flightRepository.save(flight);
            FlightResponse departingFlightResponse = DtoConverter.convertToFlightResponse(savedDepartureFlight, departureAirport.getCity(), arrivalAirport.getCity());

            if (flight.getArrivalDate() != null) {
                Flight returnFlight = new Flight();

                returnFlight.setPrice(flight.getPrice());
                returnFlight.setDepartureAirport(arrivalAirport);
                returnFlight.setArrivalAirport(departureAirport);
                returnFlight.setDepartureDate(flight.getArrivalDate());
                returnFlight.setArrivalDate(flight.getArrivalDate());

                Flight savedReturnFlight = flightRepository.save(returnFlight);
                FlightResponse returningFlightResponse = DtoConverter.convertToFlightResponse(savedReturnFlight, arrivalAirport.getCity(), departureAirport.getCity());

                return List.of(new RoundTripFlightResponse(departingFlightResponse, returningFlightResponse));
            }

            return List.of(departingFlightResponse);
        }

        throw new FlightException("Airport not found", HttpStatus.NOT_FOUND);
    }

    public Flight removeFlightById(Long flightId) {
        Flight flightToRemove = findById(flightId);
        flightRepository.delete(flightToRemove);
        return flightToRemove;
    }


    public FlightResponse updateFlight(Long flightId, Flight flight) {

        Optional<Flight> flightToUpdate = flightRepository.findById(flightId);

        if (flightToUpdate.isPresent()) {

            Flight existingFlight = flightToUpdate.get();

            if (flight.getPrice() != null) {
                existingFlight.setPrice(flight.getPrice());
            }
            if (flight.getDepartureAirport() != null) {
                existingFlight.setDepartureAirport(flight.getDepartureAirport());
            }
            if (flight.getArrivalAirport() != null) {
                existingFlight.setArrivalAirport(flight.getArrivalAirport());
            }
            if (flight.getDepartureDate() != null) {
                existingFlight.setDepartureDate(flight.getDepartureDate());
            }
            if (flight.getArrivalDate() != null) {
                existingFlight.setArrivalDate(flight.getArrivalDate());
            }
            Flight updatedFlight = flightRepository.save(existingFlight);
            return DtoConverter.convertToFlightResponse(updatedFlight, updatedFlight.getDepartureAirport().getCity(), updatedFlight.getArrivalAirport().getCity());
        }

        throw new FlightException("Flight not found", HttpStatus.NOT_FOUND);
    }


    public List<Flight> findAll() {
        return flightRepository.findAll();
    }



}
