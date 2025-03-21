package com.example.NEWBUS.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    @Column(nullable = false)
    private String seatNumber;
    private boolean booked = false; // ✅ Track booking status

    @ManyToOne
    @JoinColumn(name = "bus_id", nullable = false)
    private Bus bus; // ✅ Ensure each seat belongs to a bus

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;  // ✅ Store Booking ID for seat reservation

    @OneToOne(mappedBy = "seat") // ✅ Link to Passenger (Bidirectional)
    private Passenger passenger;
}