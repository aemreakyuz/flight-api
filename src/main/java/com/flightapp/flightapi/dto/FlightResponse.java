package com.flightapp.flightapi.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FlightResponse(Long id, BigDecimal price, AirportResponse departureAirport, AirportResponse arrivalAirport, LocalDate departureDate, LocalDate arrivalDate) {
}

