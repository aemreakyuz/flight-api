package com.flightapp.flightapi.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flightapp.flightapi.converter.DtoConverter;
import com.flightapp.flightapi.dto.FlightResponse;
import com.flightapp.flightapi.dto.RoundTripFlightResponse;
import com.flightapp.flightapi.entity.Airport;
import com.flightapp.flightapi.entity.Flight;
import com.flightapp.flightapi.repository.AirportRepository;
import com.flightapp.flightapi.service.AirportService;
import com.flightapp.flightapi.service.FlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Tag(name = "REST Apis for flight controller", description = "Can get, add, update, delete flights")
@RestController
@RequestMapping("/flights")
public class FlightController {


    private FlightService flightService;
    private AirportService airportService;
    private AirportRepository airportRepository;
    private DtoConverter dtoConverter;

    @Autowired
    public FlightController(FlightService flightService, AirportService airportService, AirportRepository airportRepository,DtoConverter dtoConverter) {
        this.flightService = flightService;
        this.airportService = airportService;
        this.airportRepository = airportRepository;
        this.dtoConverter = dtoConverter;
    }


    @Operation(summary = "Search flights. One-way or Two-way", description = "Send the necessary details. If arrival date is not sent. It will return a one way ticket. Otherwise it will return two tickets: First one is for leaving and the second one is for return ticket")
    @ApiResponse(responseCode = "200", description = "If the necessary parameters are valid it returns status code 200")
    @PostMapping("/search")
    public List<?> searchFlights(@RequestParam(name = "departureDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate, @RequestParam(name = "arrivalDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate arrivalDate, @RequestParam(name = "departureAirport") String departureAirport, @RequestParam(name = "arrivalAirport") String arrivalAirport
    ) {
        log.info("entered search post");
        if (arrivalDate != null) {
            List<RoundTripFlightResponse> returnValues = flightService.searchFlightsWithReturn(departureDate, arrivalDate, departureAirport, arrivalAirport);
            return returnValues;
        } else {
            List<FlightResponse> returnValues = flightService.searchFlightsOneWay(departureDate, departureAirport, arrivalAirport);
            return returnValues;
        }
    }

    @Operation(summary = "Add One Way or Two Way Flights", description = "Send flight object and it will be added")
    @ApiResponse(responseCode = "200", description = "If Flight object is correctly sent it returns status code 200")
    @PostMapping("")
    public List<?> addFlight(@RequestBody Flight flight) {
        if (flight.getArrivalDate() == null) {
            flight.setArrivalDate(null);
            return flightService.addFlight(flight);
        }
        return flightService.addFlight(flight);
    }

    @Operation(summary = "Update flight info", description = "Send the desired flight object. some fields may be null")
    @PutMapping("/{id}")
    public FlightResponse update(@PathVariable Long id, @RequestBody Flight flightDetails) {
        return flightService.updateFlight(id, flightDetails);
    }

    @Scheduled(cron = "30 9 * * * ?", zone = "Europe/Paris")
    @PostMapping("/save")
    public ResponseEntity<String> saveFlightsFromApi() {

        try {
            String results = pingApi(); // Fetch data from the third-party API

            // Convert the received JSON data into a list of FlightResponse objects
            ObjectMapper objectMapper = new ObjectMapper();
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            objectMapper.registerModule(new JavaTimeModule());

            List<FlightResponse> apiFlights = objectMapper.readValue(results, typeFactory.constructCollectionType(List.class, FlightResponse.class));

            List<Flight> flights = DtoConverter.convertToFlightList(apiFlights);
            log.debug("Size of flights: " + flights.size());
            List<FlightResponse> responseList = new ArrayList<>();

            // Save the fetched flights to the database
            for (Flight flight : flights) {
                Airport departureAirport = airportRepository.findByCity(flight.getDepartureAirport().getCity());
                Airport arrivalAirport = airportRepository.findByCity(flight.getArrivalAirport().getCity());

                flight.setDepartureAirport(departureAirport);
                flight.setArrivalAirport(arrivalAirport);
                FlightResponse response = DtoConverter.convertFlightToFlightResponse(flight);
                responseList.add(response);
            }
            flightService.saveFlightsFromApiResponse(responseList);

            return ResponseEntity.ok("Flights saved successfully!");
        } catch (Exception e) {
            log.error("Error saving flights: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save flights");
        }
    }

    @GetMapping("/fetch")
    public ResponseEntity<String> fetchApi() throws JsonProcessingException {
        String results = pingApi();
        log.debug(results);
        String fetchedData = ""; // Declare it here with an initial value
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            List<FlightResponse> apiFlights = objectMapper.readValue(results, typeFactory.constructCollectionType(List.class, FlightResponse.class));

            fetchedData = objectMapper.writeValueAsString(apiFlights);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch data");
        }

        return ResponseEntity.ok(fetchedData);
    }

    private String pingApi() {
        URL url = null;
        try {
            url = new URL("https://659c20abd565feee2dac7859.mockapi.io/flights/v1/flights");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            return content.toString();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return null;

    }


}





