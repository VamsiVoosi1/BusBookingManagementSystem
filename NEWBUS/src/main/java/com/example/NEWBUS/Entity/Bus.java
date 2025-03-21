package com.example.NEWBUS.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String travelsName;
    private String fromLocation;
    private String toLocation;
    private LocalDate travelDate;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private double pricePerSeat;

    @OneToMany(mappedBy = "bus", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seat> seats;
}