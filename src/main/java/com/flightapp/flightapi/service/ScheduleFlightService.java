package com.flightapp.flightapi.service;

import com.flightapp.flightapi.entity.Airport;
import com.flightapp.flightapi.entity.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Random;

@Service
public class ScheduleFlightService {

    @Autowired
    private FlightService flightService;

    @Autowired
    private AirportService airportService;


    @Scheduled(cron = "30 9 * * * ?", zone = "Europe/Paris")
    public void addScheduledFlight() {

        Random random = new Random();

        String[] airports = {"Istanbul", "Berlin", "Moscow", "Milan", "Dublin", "Dubai"};

        String departureCity = airports[random.nextInt(airports.length)];


        Airport departureAirport = airportService.findByCity("DepartureCity");
        Airport arrivalAirport = airportService.findByCity("ArrivalCity");

        Flight scheduledFlight = new Flight();
        scheduledFlight.setDepartureAirport(departureAirport);
        scheduledFlight.setArrivalAirport(arrivalAirport);

        flightService.addFlight(scheduledFlight);

        System.out.println("Scheduled flight executed at: " + LocalDate.now());
    }
}
