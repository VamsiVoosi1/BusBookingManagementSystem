package com.example.NEWBUS.Repository;

import com.example.NEWBUS.Entity.Bus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BusRepository extends JpaRepository<Bus,Long> {
    List<Bus> findByFromLocationAndToLocationAndTravelDate(String fromLocation,
                                                           String toLocation,
                                                           LocalDate travelDate);
}
