package com.flightapp.flightapi.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class FlightException extends RuntimeException {

    private HttpStatus httpStatus;

    public FlightException(String message, HttpStatus httpStatus){
        super(message);
        this.httpStatus = httpStatus;
    }
}

