package com.flightapp.flightapi.repository;


import com.flightapp.flightapi.entity.Airport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

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
        createAirport("Porto");
    }

    @AfterEach
    void tearDown(){
        airportRepository.deleteAll();
    }

    @DisplayName("Can find airport by city")
    @Test
    void findByCity(){
        Airport foundAirport = airportRepository.findByCity("Porto");
        assertNotNull(foundAirport);
        assertEquals("Porto", foundAirport.getCity());
    }

    @DisplayName("Can't find airport by city")
    @Test
    void findByCityFail(){
        Airport foundAirport = airportRepository.findByCity("Porto");
        assertNull(foundAirport);
    }

}
