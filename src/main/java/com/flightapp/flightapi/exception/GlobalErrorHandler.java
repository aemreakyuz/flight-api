package com.flightapp.flightapi.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler
    public ResponseEntity<AirportErrorResponse> handleException(AirportException airportException){
        AirportErrorResponse response = new AirportErrorResponse(airportException.getHttpStatus().value(), airportException.getMessage());
        return new ResponseEntity<>(response, airportException.getHttpStatus());
    }


    @ExceptionHandler
    public ResponseEntity<AirportErrorResponse> handleException(Exception exception){
        AirportErrorResponse response = new AirportErrorResponse(HttpStatus.BAD_REQUEST.value(),
                exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }



}
