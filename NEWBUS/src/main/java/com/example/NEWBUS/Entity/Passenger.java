package com.example.NEWBUS.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "passenger", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"booking_id", "seat_number"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int age;
    private String gender;
    private String email; // ✅ Email of passenger
    private String phoneNumber;
    private String password; // Consider encrypting this field

    private String seatNumber; // ✅ Store seat number assigned to this passenger

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking; // ✅ Associate passengers with a booking

    @ManyToOne
    @JoinColumn(name = "bus_id")
    private Bus bus; // ✅ Associate passengers with a bus

    @OneToOne
    @JoinColumn(name = "seat_id", referencedColumnName = "id", unique = true) // ✅ Link to Seat
    private Seat seat;
}