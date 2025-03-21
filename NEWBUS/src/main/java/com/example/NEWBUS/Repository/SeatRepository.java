package com.example.NEWBUS.Repository;

import com.example.NEWBUS.Entity.Bus;
import com.example.NEWBUS.Entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat,Long> {
    @Query("SELECT MAX(CAST(SUBSTRING(s.seatNumber, 2) AS int)) FROM Seat s WHERE s.bus.id = :busId")
    Integer findHighestSeatNumber(@Param("busId") Long busId);

    List<Seat> findByBusId(Long busId);
    @Query("SELECT s FROM Seat s WHERE s.bus.id = :busId AND s.seatNumber = :seatNumber")
    Optional<Seat> findByBusIdAndSeatNumber(@Param("busId") Long busId, @Param("seatNumber") String seatNumber);

    Seat findBySeatNumberAndBus(String seatNumber, Bus bus);



}
