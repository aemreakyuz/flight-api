package com.flightapp.flightapi.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightapp.flightapi.converter.DtoConverter;
import com.flightapp.flightapi.dto.AirportResponse;
import com.flightapp.flightapi.entity.Airport;
import com.flightapp.flightapi.service.AirportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class AirportControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private AirportService airportService;


    @Test
    void save() throws Exception{
        Airport airport = new Airport();
        airport.setCity("Rome");
        AirportResponse expectedResponse = new AirportResponse(airport.getId(), airport.getCity());
        when(airportService.addNewAirport(airport)).thenReturn(expectedResponse);

        mockMvc.perform(post("/airport")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonToString(airport))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.city").value("Rome"));

        airportService.addNewAirport(airport);
    }

    @Test
    void remove() throws Exception{
        Airport airport = new Airport();
        airport.setId(1L);
        airport.setCity("Berlin");
        when(airportService.findById(1L)).thenReturn(airport);
        when(airportService.deleteAirportById(airport.getId())).thenReturn(DtoConverter.convertToAirportResponse(airport));

        mockMvc.perform(delete("/airport/{id}"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.city").value("Berlin"));

        verify(airportService).deleteAirportById(1L);
    }

    public static String jsonToString(Object object){
        try{
            return new ObjectMapper().writeValueAsString(object);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
