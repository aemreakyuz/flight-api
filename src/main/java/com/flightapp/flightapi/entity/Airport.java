package com.flightapp.flightapi.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="airport", schema = "flightapp")
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="city")
    private String city;


    @OneToMany(mappedBy = "departureAirport", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Flight> departingFlights;


    @OneToMany(mappedBy = "arrivalAirport", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Flight> arrivingFlights;
}
