package com.flightapp.flightapi.service;

import com.flightapp.flightapi.entity.Airport;
import com.flightapp.flightapi.entity.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
        String arrivalCity = airports[random.nextInt(airports.length)];

        Airport departureAirport = airportService.findByCity(departureCity);
        Airport arrivalAirport = airportService.findByCity(arrivalCity);

        while (arrivalCity.equals(departureCity)) {
            arrivalCity = airports[random.nextInt(airports.length)];
             arrivalAirport = airportService.findByCity(arrivalCity);
        }

        BigDecimal minPrice = BigDecimal.valueOf(100);
        BigDecimal maxPrice = BigDecimal.valueOf(1000);
        BigDecimal randomPrice = minPrice.add(BigDecimal.valueOf(random.nextDouble()).multiply(maxPrice.subtract(minPrice)));


        LocalDate currentDate = LocalDate.now();
        LocalDate departureDate = currentDate.plusDays(random.nextInt(30));
        LocalDate arrivalDate = departureDate.plusDays(random.nextInt(10));

        Flight scheduledFlight = new Flight();
        scheduledFlight.setDepartureAirport(departureAirport);
        scheduledFlight.setArrivalAirport(arrivalAirport);
        scheduledFlight.setPrice(randomPrice);
        scheduledFlight.setDepartureDate(departureDate);
        scheduledFlight.setArrivalDate(arrivalDate);

        flightService.addFlight(scheduledFlight);

        System.out.println("Scheduled flight executed at: " + LocalDate.now());
    }
}
