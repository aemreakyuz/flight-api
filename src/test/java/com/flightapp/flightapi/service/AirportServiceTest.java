package com.flightapp.flightapi.service;

import com.flightapp.flightapi.dto.AirportResponse;
import com.flightapp.flightapi.entity.Airport;
import com.flightapp.flightapi.repository.AirportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AirportServiceTest {

    private AirportService airportService;

    @Mock
    private AirportRepository airportRepository;

    @BeforeEach
    void setUp(){
        airportService = new AirportService(airportRepository);
    }

    @Test
    void findAll(){
        airportService.findAll();
        verify(airportRepository).findAll();
    }

    @Test
    void findById(){
        Long id = 1L;
        Airport expectedAirport =  new Airport();
        when(airportRepository.findById(id)).thenReturn(Optional.of(expectedAirport));
        Airport result = airportService.findById(id);
        verify(airportRepository).findById(id);
        assertNotNull(result);
    }

    @Test
    void findByCity(){
        String city = "Istanbul";
        Airport expectedAirport = new Airport();
        when(airportRepository.findByCity(city)).thenReturn(expectedAirport);
        Airport result = airportService.findByCity(city);
        verify(airportRepository).findByCity(city);
        assertNotNull(result);
    }


    @Test
    void canSave(){
        Airport airport = new Airport();
        airport.setCity("Paris");
        airportService.addNewAirport(airport);
        verify(airportRepository).save(airport);
    }

    @Test
    void remove(){
        Airport airport = new Airport();
        airport.setId(Long.valueOf(1));
        airport.setCity("Dublin");

        given(airportRepository.findById(Long.valueOf(1))).willReturn(Optional.of(airport));
        AirportResponse removedAirport = airportService.deleteAirportById(airport.getId());
        verify(airportRepository).delete(airport);
        //assertEquals("Dublin", removedAirport.getCity());
    }


}
