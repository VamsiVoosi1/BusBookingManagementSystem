package com.example.NEWBUS.ServiceTest;

import com.example.NEWBUS.Entity.Booking;
import com.example.NEWBUS.Entity.Bus;
import com.example.NEWBUS.Entity.Passenger;
import com.example.NEWBUS.Service.PDFGeneratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PDFGeneratorServiceTest {

    private PDFGeneratorService pdfGeneratorService;
    private Booking booking;
    private Bus bus;
    private Passenger passenger1;
    private Passenger passenger2;

    @BeforeEach
    void setUp() {
        pdfGeneratorService = new PDFGeneratorService();

        // Create a Bus
        bus = new Bus();
        bus.setId(1L);
        bus.setTravelsName("Express Travels");
        bus.setFromLocation("CityA");
        bus.setToLocation("CityB");
        bus.setTravelDate(LocalDate.of(2025, 5, 20));
        bus.setDepartureTime(LocalTime.of(10, 30));
        bus.setArrivalTime(LocalTime.of(14, 30));

        // Create Passengers
        passenger1 = new Passenger();
        passenger1.setId(1L);
        passenger1.setName("John Doe");
        passenger1.setSeatNumber("A1");
        passenger1.setAge(30);
        passenger1.setGender("Male");

        passenger2 = new Passenger();
        passenger2.setId(2L);
        passenger2.setName("Jane Doe");
        passenger2.setSeatNumber("A2");
        passenger2.setAge(28);
        passenger2.setGender("Female");

        List<Passenger> passengers = Arrays.asList(passenger1, passenger2);

        // Create a Booking
        booking = new Booking();
        booking.setId(101L);
        booking.setBus(bus);
        booking.setPassengers(passengers);
        booking.setTotalPrice(100.0);
    }

    @Test
    void testGenerateBookingTicket_Success() {
        byte[] pdfBytes = pdfGeneratorService.generateBookingTicket(booking);

        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0, "Generated PDF should not be empty");
    }

    @Test
    void testGenerateBookingTicket_NoPassengers() {
        booking.setPassengers(null); // Simulate missing passengers

        Exception exception = assertThrows(RuntimeException.class, () ->
                pdfGeneratorService.generateBookingTicket(booking)
        );

        assertTrue(exception.getMessage().contains("Error: No passengers found for this booking!"));
    }
}
