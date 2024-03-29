package com.flightapp.flightapi.service;

import com.flightapp.flightapi.converter.DtoConverter;
import com.flightapp.flightapi.dto.AirportResponse;
import com.flightapp.flightapi.entity.Airport;
import com.flightapp.flightapi.entity.Flight;
import com.flightapp.flightapi.exception.AirportException;
import com.flightapp.flightapi.repository.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AirportService  {

    private AirportRepository airportRepository;

    @Autowired
    public AirportService(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    public List<Airport> findAll() {
        return airportRepository.findAll();
    }

    public List<Flight> findAllFlights() {
        return null;
    }

    public Airport findByCity(String city) {
        return airportRepository.findByCity(city);
    }

    public AirportResponse addNewAirport(Airport airport) {
        Airport foundAirport = findByCity(airport.getCity());
        if (foundAirport != null) {
            throw new AirportException("Airport already exists.", HttpStatus.BAD_REQUEST);
        }
        airportRepository.save(airport);
        return DtoConverter.convertToAirportResponse(airport);
    }

    public AirportResponse deleteAirportById(Long id) {
        Airport airportToBeDeleted = findById(id);
        if (airportToBeDeleted != null) {
            airportRepository.delete(airportToBeDeleted);
            return DtoConverter.convertToAirportResponse(airportToBeDeleted);
        }
        return null;

    }

    public Airport findById(Long id) {
        Optional<Airport> airportOptional = airportRepository.findById(id);
        return airportOptional.orElse(null);


    }
}
