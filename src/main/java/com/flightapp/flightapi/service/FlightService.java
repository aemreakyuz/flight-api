package com.flightapp.flightapi.service;

import com.flightapp.flightapi.converter.DtoConverter;
import com.flightapp.flightapi.dto.AirportResponse;
import com.flightapp.flightapi.dto.FlightResponse;
import com.flightapp.flightapi.dto.RoundTripFlightResponse;
import com.flightapp.flightapi.entity.Airport;
import com.flightapp.flightapi.entity.Flight;
import com.flightapp.flightapi.exception.AirportException;
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
import java.util.stream.Collectors;

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
        AirportResponse departure = DtoConverter.convertToAirportResponse(airportRepository.findByCity(departureAirport));
        AirportResponse arrival = DtoConverter.convertToAirportResponse(airportRepository.findByCity(arrivalAirport));
        for(Flight flight : flights){

            FlightResponse departingFlight = DtoConverter.convertToFlightResponse(flight, departure, arrival);

            LocalDate newDepartureDate = departingFlight.arrivalDate();

            FlightResponse arrivingFlight = DtoConverter.convertToFlightResponse(flight, arrival, departure).withDepartureDate(newDepartureDate);

            RoundTripFlightResponse roundTrips = new RoundTripFlightResponse(departingFlight, arrivingFlight);
            response.add(roundTrips);
        }
        return response;
    }

    public List<FlightResponse> searchFlightsOneWay(LocalDate departureDate, String departureAirport, String arrivalAirport) {
        List<Flight> flights = flightRepository.searchOneWayFlights(departureDate, departureAirport, arrivalAirport);
        List<FlightResponse> flightResponses = new ArrayList<>();
        AirportResponse departure = DtoConverter.convertToAirportResponse(airportRepository.findByCity(departureAirport));
        AirportResponse arrival = DtoConverter.convertToAirportResponse(airportRepository.findByCity(arrivalAirport));

        for (Flight flight : flights) {
            FlightResponse flightResponse = DtoConverter.convertToFlightResponse(flight, departure, arrival);
            flightResponses.add(flightResponse);
        }

        return flightResponses;
    }

    public Flight findById(Long id) {
        Optional<Flight> flightOptional = flightRepository.findById(id);
        return flightOptional.orElse(null);

        //TODO => Error Handling, dto converter
    }

    public void saveFlightsFromApiResponse(List<FlightResponse> flightResponses) {
        List<Flight> flights = flightResponses.stream()
                .map(this::convertToFlight)
                .collect(Collectors.toList());
        flightRepository.saveAll(flights);
    }

    private Flight convertToFlight(FlightResponse flightResponse) {
        Flight flight = new Flight();
        flight.setPrice(flightResponse.price());

        Airport departureAirport = airportRepository.findByCity(flightResponse.arrivalAirport().city());
        if (departureAirport != null) {
            flight.setDepartureAirport(departureAirport);
        }

        Airport arrivalAirport = airportRepository.findByCity(flightResponse.arrivalAirport().city());
        if (arrivalAirport != null) {
            flight.setArrivalAirport(arrivalAirport);
        }


        flight.setDepartureDate(flightResponse.departureDate());
        flight.setArrivalDate(flightResponse.arrivalDate());


        return flight;
    }

    public List<?> addFlight(Flight flight) {

        Airport  departureAirport = airportRepository.findByCity(flight.getDepartureAirport().getCity());
        Airport  arrivalAirport = airportRepository.findByCity(flight.getArrivalAirport().getCity());

        if (departureAirport !=null && arrivalAirport != null) {

            flight.setDepartureAirport(departureAirport);
            flight.setArrivalAirport(arrivalAirport);

            Flight savedDepartureFlight = flightRepository.save(flight);
            FlightResponse departingFlightResponse = DtoConverter.convertToFlightResponse(savedDepartureFlight, new AirportResponse(departureAirport.getId(), departureAirport.getCity()), new AirportResponse(arrivalAirport.getId(), arrivalAirport.getCity()));

            if (flight.getArrivalDate() != null) {
                Flight returnFlight = new Flight();

                returnFlight.setPrice(flight.getPrice());
                returnFlight.setDepartureAirport(arrivalAirport);
                returnFlight.setArrivalAirport(departureAirport);
                returnFlight.setDepartureDate(flight.getArrivalDate());
                returnFlight.setArrivalDate(flight.getArrivalDate());

                Flight savedReturnFlight = flightRepository.save(returnFlight);
                FlightResponse returningFlightResponse = DtoConverter.convertToFlightResponse(savedReturnFlight, new AirportResponse(arrivalAirport.getId(), arrivalAirport.getCity()), new AirportResponse(departureAirport.getId(), departureAirport.getCity()));

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
            if (flight.getDepartureAirport() != null && flight.getDepartureAirport().getId() != null) {
                Airport departureAirport = airportRepository.findById(flight.getDepartureAirport().getId())
                        .orElseThrow(() -> new AirportException("Departure airport not found", HttpStatus.NOT_FOUND));
                existingFlight.setDepartureAirport(departureAirport);
            }

            if (flight.getArrivalAirport() != null && flight.getArrivalAirport().getId() != null) {
                Airport arrivalAirport = airportRepository.findById(flight.getArrivalAirport().getId())
                        .orElseThrow(() -> new AirportException("Arrival airport not found", HttpStatus.NOT_FOUND));
                existingFlight.setArrivalAirport(arrivalAirport);
            }
            if (flight.getDepartureDate() != null) {
                existingFlight.setDepartureDate(flight.getDepartureDate());
            }
            if (flight.getArrivalDate() != null) {
                existingFlight.setArrivalDate(flight.getArrivalDate());
            }
            Flight updatedFlight = flightRepository.save(existingFlight);
            return DtoConverter.convertToFlightResponse(updatedFlight,new AirportResponse(updatedFlight.getId(), updatedFlight.getDepartureAirport().getCity()), new AirportResponse(updatedFlight.getId(), updatedFlight.getDepartureAirport().getCity()));
        }

        throw new FlightException("Flight not found", HttpStatus.NOT_FOUND);
    }


    public List<Flight> findAll() {
        return flightRepository.findAll();
    }



}
