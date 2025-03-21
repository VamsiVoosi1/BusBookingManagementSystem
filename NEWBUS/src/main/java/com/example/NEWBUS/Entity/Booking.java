package com.example.NEWBUS.Entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate bookingDate;
    private String email;
    private double totalPrice;
    private String status; // ✅ Status (e.g., "CONFIRMED", "CANCELLED")

    @ManyToOne
    @JoinColumn(name = "bus_id", nullable = false)
    private Bus bus; // ✅ Bus for this booking

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Passenger> passengers; // ✅ List of passengers

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Seat> seats; // ✅ List of seats booked

    @ManyToOne
    @JoinColumn(name = "passenger_id", nullable = false)
    private Passenger passenger; // ✅ Ensure Passenger is linked
}