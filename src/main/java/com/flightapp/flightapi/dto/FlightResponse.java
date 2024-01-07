package com.flightapp.flightapi.dto;

import java.math.BigDecimal;

public record FlightResponse(Long id, BigDecimal price, AirportResponse departureAirport, AirportResponse arrivalAirport) {
}
