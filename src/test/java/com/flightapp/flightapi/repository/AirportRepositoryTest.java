package com.flightapp.flightapi.repository;


import com.flightapp.flightapi.entity.Airport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class AirportRepositoryTest {

    private AirportRepository airportRepository;


    @Autowired

    public AirportRepositoryTest(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }


    private void createAirport(String city) {
        Airport airport = new Airport();
        airport.setCity(city);
        Airport foundAirport = airportRepository.findByCity(city);
        if(foundAirport == null){
            airportRepository.save(airport);
        }
    }

    @BeforeEach
    void setUp(){
        createAirport("Berlin");
    }

    @AfterEach
    void tearDown(){
        airportRepository.deleteAll();
    }

    @DisplayName("Can find airport by city")
    @Test
    void findByCity(){
        Airport foundAirport = airportRepository.findByCity("Berlin");
        assertNotNull(foundAirport);
        assertEquals("Berlin", foundAirport.getCity());
    }
}
