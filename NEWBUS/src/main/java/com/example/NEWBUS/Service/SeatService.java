package com.example.NEWBUS.Service;

import com.example.NEWBUS.Entity.Bus;
import com.example.NEWBUS.Entity.Seat;
import com.example.NEWBUS.Repository.BusRepository;
import com.example.NEWBUS.Repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private BusRepository busRepository;


    public Seat addSeat(Seat seat) {
        return seatRepository.save(seat);
    }
    public void addSeatsToBus(Long busId, int seatCount) {
        Bus bus = busRepository.findById(busId)
                .orElseThrow(() -> new RuntimeException("Bus not found"));

        // ✅ Fetch the highest seat number (if no seats exist, start from 0)
        Integer highestSeatNumber = seatRepository.findHighestSeatNumber(busId);
        if (highestSeatNumber == null) {
            highestSeatNumber = 0;
        }

        // ✅ Generate new seat numbers starting from the last highest seat number
        for (int i = 1; i <= seatCount; i++) {
            Seat seat = new Seat();
            seat.setBus(bus);
            seat.setSeatNumber("S" + (highestSeatNumber + i)); // Continue numbering correctly
            seat.setBooked(false);
            seatRepository.save(seat);
        }
    }
    public List<Seat> getSeatsByBus(Long busId) {
        return seatRepository.findByBusId(busId);
    }
    public List<Seat> getSeatsByBusId(Long busId) {
        return seatRepository.findByBusId(busId);
    }
}
