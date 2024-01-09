package com.flightapp.flightapi.exception;


import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class AirportException extends RuntimeException {

    private HttpStatus httpStatus;

    public AirportException(String message, HttpStatus httpStatus){
        super(message);
        this.httpStatus = httpStatus;
    }
}
