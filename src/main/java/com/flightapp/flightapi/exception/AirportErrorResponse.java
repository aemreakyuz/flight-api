package com.flightapp.flightapi.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class AirportErrorResponse {
    private int status;
    private String message;
}
