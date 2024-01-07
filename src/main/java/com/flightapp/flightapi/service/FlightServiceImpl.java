package com.flightapp.flightapi.service;

import com.flightapp.flightapi.converter.DtoConverter;
import com.flightapp.flightapi.dto.FlightResponse;
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
public class FlightServiceImpl implements FlightService {

    private FlightRepository flightRepository;
    private AirportRepository airportRepository;


    @Autowired
    public FlightServiceImpl(FlightRepository flightRepository, AirportRepository airportRepository) {
        this.flightRepository = flightRepository;
        this.airportRepository = airportRepository;

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
            return DtoConverter.convertToFlightResponse(flight, departureAirport, arrivalAirport);
        }

        return null;
    }

    public Flight removeFlightById(Long flightId) {
        Flight flightToRemove = findById(flightId);
        flightRepository.delete(flightToRemove);
        return flightToRemove;
    }

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

    public Airport findDepartureAirportByFlightId(Long flightId) {
        Optional<Flight> flightOptional = flightRepository.findById(flightId);
        if (flightOptional.isPresent()) {
            Flight flight = flightOptional.get();
            return flight.getDepartureAirport();
        }
        return null;
    }

    public Airport findArrivalAirportByFlightId(Long flightId) {
        Optional<Flight> flightOptional = flightRepository.findById(flightId);
        if (flightOptional.isPresent()) {
            Flight flight = flightOptional.get();
            return flight.getArrivalAirport();
        }
        return null;
    }

    public LocalDate findDepartureDateById(Long flightId) {
        Optional<Flight> flightOptional = flightRepository.findById(flightId);
        if (flightOptional.isPresent()) {
            Flight flight = flightOptional.get();
            return flight.getDepartureDate();
        }
        return null;
    }

    public LocalDate findArrivalDateById(Long flightId) {
        Optional<Flight> flightOptional = flightRepository.findById(flightId);
        if (flightOptional.isPresent()) {
            Flight flight = flightOptional.get();
            return flight.getArrivalDate();
        }
        return null;
    }

    public List<Flight> findAll() {
        return flightRepository.findAll();
    }

    public List<Flight> findDepartingFlights() {

        LocalDate currentDate = LocalDate.now();
        return flightRepository.findByDepartureDateAfter(currentDate);

    }

    public List<Flight> findArrivingFlights() {
        LocalDate currentDate = LocalDate.now();
        return flightRepository.findByArrivalDateBefore(currentDate);
    }

    public List<Flight> searchFlights(Long flightId) {
        Optional<Flight> optionalFlight = flightRepository.findById(flightId);
        if (optionalFlight.isPresent()) {
            Flight flight = optionalFlight.get();
            if (flight.getArrivalDate() == null) {
                List<Flight> oneWayFlight = new ArrayList<>();
                oneWayFlight.add(flight);
                return oneWayFlight;
            } else {

            }
        }
        return new ArrayList<>();
    }
}
