package com.example.NEWBUS.Service;

import com.example.NEWBUS.Entity.Booking;
import com.example.NEWBUS.Entity.Bus;
import com.example.NEWBUS.Entity.Passenger;
import com.example.NEWBUS.Entity.Seat;
import com.example.NEWBUS.Repository.BookingRepository;
import com.example.NEWBUS.Repository.PassengerRepository;
import com.example.NEWBUS.Repository.SeatRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final PassengerRepository passengerRepository;
    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;

    // ✅ Constructor Injection (Recommended)
    @Autowired
    public BookingService(BookingRepository bookingRepository,
                          PassengerRepository passengerRepository,
                          SeatRepository seatRepository) {
        this.bookingRepository = bookingRepository;
        this.passengerRepository = passengerRepository;
        this.seatRepository = seatRepository;
    }
//    @Transactional
    public Booking saveBooking(Long passengerId, Bus bus, List<String> seatNumbers,
                               List<String> passengerNames, List<Integer> passengerAges,
                               List<String> passengerGenders, double totalPrice) {
        // ✅ Fetch Passenger (Booking Owner)
        Passenger accountHolder = passengerRepository.findById(passengerId)
                .orElseThrow(() -> new RuntimeException("Passenger not found!"));

        // ✅ Create & Save Booking (Initial Save)
        Booking booking = new Booking();
        booking.setPassenger(accountHolder);
        booking.setEmail(accountHolder.getEmail()); // ✅ Save Passenger Email
        booking.setBus(bus);
        booking.setTotalPrice(totalPrice);
        booking.setBookingDate(LocalDate.now());
        booking.setStatus("Confirmed");

        Booking savedBooking = bookingRepository.save(booking);
        bookingRepository.flush(); // ✅ Ensure Immediate Save

        System.out.println("✅ Booking Saved: " + savedBooking.getId()); // 🔍 Debugging Line

        // ✅ Save Each Passenger & Assign Seats
        List<Passenger> passengers = new ArrayList<>();
        for (int i = 0; i < seatNumbers.size(); i++) {
            String seatNum = seatNumbers.get(i);

            // ✅ Fetch Seat and Mark as Booked
            Seat seat = seatRepository.findByBusIdAndSeatNumber(bus.getId(), seatNum)
                    .orElseThrow(() -> new RuntimeException("Seat not found: " + seatNum));
            seat.setBooked(true);
            seat.setBooking(savedBooking); // ✅ Assign Booking to Seat

            // ✅ Create Passenger
            Passenger newPassenger = new Passenger();
            newPassenger.setName(i < passengerNames.size() ? passengerNames.get(i) : "Unknown");
            newPassenger.setAge(i < passengerAges.size() ? passengerAges.get(i) : 0);
            newPassenger.setGender(i < passengerGenders.size() ? passengerGenders.get(i) : "Not Specified");
            newPassenger.setSeatNumber(seatNum);
            newPassenger.setBooking(savedBooking); // ✅ Assign Booking
            newPassenger.setBus(bus); // ✅ Assign Bus
            newPassenger.setSeat(seat); // ✅ Assign Seat to Passenger

            passengers.add(newPassenger);
            seatRepository.save(seat); // ✅ Save Seat with Updated Booking & Passenger
        }

        // ✅ Save All Passengers
        passengerRepository.saveAll(passengers);

        // ✅ Link Passengers to Booking
        savedBooking.setPassengers(passengers);
        Booking finalBooking = bookingRepository.save(savedBooking);

        System.out.println("✅ Final Booking Saved: " + finalBooking.getId()); // 🔍 Debugging Line

        // ✅ Verify Booking Exists in Database
        Optional<Booking> bookingCheck = bookingRepository.findById(savedBooking.getId());
        if (bookingCheck.isPresent()) {
            System.out.println("✅ Booking Successfully Stored in Database: " + bookingCheck.get().getId());
        } else {
            System.out.println("❌ Booking NOT Found in Database!");
        }

        return finalBooking;
    }





    public Optional<Booking> findById(Long bookingId) {
        return bookingRepository.findById(bookingId);
    }
    // ✅ Fetch Booking with Passengers (Fix Lazy Loading Issue)
    @Transactional
    public Booking getBookingWithPassengers(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found for ID: " + bookingId));
    }
    public List<Booking> getBookingsByPassengerId(Long passengerId) {
        return bookingRepository.findByPassengerId(passengerId);
    }
    public List<Booking> findByPassengerId(Long passengerId) {
        return bookingRepository.findByPassengerId(passengerId);
    }


}
