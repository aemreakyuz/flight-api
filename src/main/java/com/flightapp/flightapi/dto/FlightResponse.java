package com.flightapp.flightapi.dto;

import java.math.BigDecimal;
import java.time.LocalDate;



public record FlightResponse(Long id, BigDecimal price, AirportResponse departureAirport, AirportResponse arrivalAirport, LocalDate departureDate, LocalDate arrivalDate) {

    public FlightResponse withDepartureDate(LocalDate newDepartureDate){
        return new FlightResponse(id, price, departureAirport, arrivalAirport, newDepartureDate, arrivalDate);
    }

    public Long id() {
        return id;
    }

    public BigDecimal price() {
        return price;
    }

    public AirportResponse departureAirport() {
        return departureAirport;
    }

    public AirportResponse arrivalAirport() {
        return arrivalAirport;
    }

    public LocalDate departureDate() {
        return departureDate;
    }

    public LocalDate arrivalDate() {
        return arrivalDate;
    }
}


