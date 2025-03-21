package com.example.NEWBUS.Dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassengerDto {
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

}