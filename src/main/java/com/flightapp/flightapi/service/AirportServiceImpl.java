package com.flightapp.flightapi.service;

import com.flightapp.flightapi.converter.DtoConverter;
import com.flightapp.flightapi.dto.AirportResponse;
import com.flightapp.flightapi.entity.Airport;
import com.flightapp.flightapi.repository.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AirportServiceImpl implements AirportService{

    private AirportRepository airportRepository;

    @Autowired
    public AirportServiceImpl(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    public List<Airport> findAll() {
        return airportRepository.findAll();
    }

    public Airport findByCity(String city) {
        return airportRepository.findByCity(city);
    }

    public AirportResponse addNewAirport(Airport airport) {
         airportRepository.save(airport);
         return DtoConverter.convertToAirportResponse(airport);
        }

    public AirportResponse deleteAirportById(Long id) {
        Airport airportToBeDeleted = findById(id);
        airportRepository.delete(airportToBeDeleted);
        return DtoConverter.convertToAirportResponse(airportToBeDeleted);

        // TODO=> Error Handling

    }

    public Airport findById(Long id) {
        Optional<Airport> airportOptional = airportRepository.findById(id);

        return airportOptional.orElse(null);

        //TODO => Error Handling
    }
}
