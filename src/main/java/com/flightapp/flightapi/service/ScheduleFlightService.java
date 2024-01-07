package com.flightapp.flightapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ScheduleFlightService {

    private FlightService flightService;
    private AirportService airportService;

    @Autowired
    public ScheduleFlightService(FlightService flightService, AirportService airportService) {
        this.flightService = flightService;
        this.airportService = airportService;
    }

    @Scheduled(cron = "* * 1 * *", zone = "Europe/Paris")
    public void addScheduledFlight(){
        System.out.println("Scheduled flight executed at: " + LocalDate.now());
    }
}
