package com.flightapp.flightapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flightapp.flightapi.converter.DtoConverter;
import com.flightapp.flightapi.dto.FlightResponse;
import com.flightapp.flightapi.entity.Airport;
import com.flightapp.flightapi.entity.Flight;
import com.flightapp.flightapi.repository.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


@Service
public class ScheduleFlightService {

    private FlightService flightService;
    private AirportRepository airportRepository;

    @Autowired
    public ScheduleFlightService(FlightService flightService, AirportRepository airportRepository) {
        this.flightService = flightService;
        this.airportRepository =airportRepository;
    }

    //Every day at 8am it fetches and saves data to database
    @Scheduled(cron = "0 8 * * * ?", zone = "Europe/Paris")
    public ResponseEntity<String> saveFlightsFromApi() {

        try {
            String results = pingApi(); // Fetch data from the third-party API

            // Convert the received JSON data into a list of FlightResponse objects
            ObjectMapper objectMapper = new ObjectMapper();
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            objectMapper.registerModule(new JavaTimeModule());

            List<FlightResponse> apiFlights = objectMapper.readValue(results, typeFactory.constructCollectionType(List.class, FlightResponse.class));

            List<Flight> flights = DtoConverter.convertToFlightList(apiFlights);

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

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save flights");
        }
    }

    public String pingApi() {
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
            return "Failed to get flights";

        }
    }
}
