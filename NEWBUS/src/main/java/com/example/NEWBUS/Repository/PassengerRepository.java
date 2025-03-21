package com.example.NEWBUS.Repository;

import com.example.NEWBUS.Entity.Booking;
import com.example.NEWBUS.Entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PassengerRepository extends JpaRepository<Passenger,Long> {
    Passenger findByEmailAndPassword(String email, String password);
    Optional<Passenger> findByEmail(String email);



}
