package com.flightapp.flightapi.service;

import com.flightapp.flightapi.entity.Airport;
import com.flightapp.flightapi.repository.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AirportServiceImpl implements AirportService{

    private AirportRepository airportRepository;

    @Autowired
    public AirportServiceImpl(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    public Airport addNewAirport(Airport airport) {
        return airportRepository.save(airport);
        }

    public Airport deleteAirportById(Long id) {
        Airport airportToBeDeleted = findById(id);
        airportRepository.delete(airportToBeDeleted);
        return airportToBeDeleted;

        // TODO=> Error Handling, dto converter

    }

    public Airport findById(Long id) {
        Optional<Airport> airportOptional = airportRepository.findById(id);
        return airportOptional.orElse(null);

        //TODO => Error Handling, dto converter
    }
}
