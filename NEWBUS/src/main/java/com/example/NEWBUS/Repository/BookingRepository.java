package com.example.NEWBUS.Repository;

import com.example.NEWBUS.Entity.Booking;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking,Long> {
    @EntityGraph(attributePaths = {"passengers"})
    Optional<Booking> findById(Long id);

    @Query("SELECT b FROM Booking b LEFT JOIN FETCH b.passengers WHERE b.id = :bookingId")
    Booking findByIdWithPassengers(@Param("bookingId") Long bookingId);

    List<Booking> findByPassengerId(Long passengerId);




}
