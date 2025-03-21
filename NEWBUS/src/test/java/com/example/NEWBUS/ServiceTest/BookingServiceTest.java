package com.example.NEWBUS.ServiceTest;

import com.example.NEWBUS.Entity.Booking;
import com.example.NEWBUS.Entity.Bus;
import com.example.NEWBUS.Entity.Passenger;
import com.example.NEWBUS.Entity.Seat;
import com.example.NEWBUS.Repository.BookingRepository;
import com.example.NEWBUS.Repository.PassengerRepository;
import com.example.NEWBUS.Repository.SeatRepository;
import com.example.NEWBUS.Service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private BookingService bookingService;

    private Passenger passenger;
    private Bus bus;
    private Seat seat;
    private Booking booking;

    @BeforeEach
    void setUp() {
        passenger = new Passenger();
        passenger.setId(1L);
        passenger.setEmail("test@example.com");

        bus = new Bus();
        bus.setId(101L);

        seat = new Seat();
        seat.setId(1L);
        seat.setSeatNumber("A1");
        seat.setBus(bus);
        seat.setBooked(false);

        booking = new Booking();
        booking.setId(1L);
        booking.setPassenger(passenger);
        booking.setBus(bus);
        booking.setTotalPrice(500);
        booking.setBookingDate(LocalDate.now());
        booking.setStatus("Confirmed");
    }

    @Test
    void testSaveBooking_Success() {
        List<String> seatNumbers = Arrays.asList("A1", "A2");
        List<String> passengerNames = Arrays.asList("John Doe", "Jane Doe");
        List<Integer> passengerAges = Arrays.asList(25, 22);
        List<String> passengerGenders = Arrays.asList("Male", "Female");
        double totalPrice = 1000.0;

        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));
        when(seatRepository.findByBusIdAndSeatNumber(bus.getId(), "A1")).thenReturn(Optional.of(seat));
        when(seatRepository.findByBusIdAndSeatNumber(bus.getId(), "A2")).thenReturn(Optional.of(new Seat()));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking savedBooking = bookingService.saveBooking(1L, bus, seatNumbers, passengerNames, passengerAges, passengerGenders, totalPrice);

        assertNotNull(savedBooking);
        assertEquals(1L, savedBooking.getPassenger().getId());
        assertEquals(500.0, savedBooking.getTotalPrice());
        assertEquals("Confirmed", savedBooking.getStatus());

        verify(bookingRepository, times(2)).save(any(Booking.class));
        verify(seatRepository, times(2)).save(any(Seat.class));
        verify(passengerRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testSaveBooking_PassengerNotFound() {
        when(passengerRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                bookingService.saveBooking(2L, bus, List.of("A1"), List.of("John Doe"), List.of(25), List.of("Male"), 500.0));

        assertEquals("Passenger not found!", exception.getMessage());
    }

    @Test
    void testFindById_Success() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        Optional<Booking> foundBooking = bookingService.findById(1L);

        assertTrue(foundBooking.isPresent());
        assertEquals(1L, foundBooking.get().getId());
    }

    @Test
    void testFindById_NotFound() {
        when(bookingRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<Booking> foundBooking = bookingService.findById(2L);

        assertFalse(foundBooking.isPresent());
    }

    @Test
    void testGetBookingWithPassengers_Success() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        Booking foundBooking = bookingService.getBookingWithPassengers(1L);

        assertNotNull(foundBooking);
        assertEquals(1L, foundBooking.getId());
    }

    @Test
    void testGetBookingWithPassengers_NotFound() {
        when(bookingRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> bookingService.getBookingWithPassengers(2L));

        assertEquals("Booking not found for ID: 2", exception.getMessage());
    }

    @Test
    void testGetBookingsByPassengerId() {
        when(bookingRepository.findByPassengerId(1L)).thenReturn(Arrays.asList(booking));

        List<Booking> bookings = bookingService.getBookingsByPassengerId(1L);

        assertFalse(bookings.isEmpty());
        assertEquals(1, bookings.size());
        assertEquals(1L, bookings.get(0).getId());
    }
}
