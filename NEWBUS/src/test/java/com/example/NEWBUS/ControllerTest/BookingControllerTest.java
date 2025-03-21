package com.example.NEWBUS.ControllerTest;

import com.example.NEWBUS.Controller.BookingController;
import com.example.NEWBUS.Entity.Booking;
import com.example.NEWBUS.Entity.Bus;
import com.example.NEWBUS.Service.BookingService;
import com.example.NEWBUS.Service.BusService;
import com.example.NEWBUS.Service.EmailService;
import com.example.NEWBUS.Service.PDFGeneratorService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @Mock
    private BusService busService;

    @Mock
    private BookingService bookingService;

    @Mock
    private PDFGeneratorService pdfGeneratorService;

    @Mock
    private EmailService emailService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private BookingController bookingController;

    private Bus testBus;
    private Booking testBooking;

    @BeforeEach
    void setUp() {
        testBus = new Bus();
        testBus.setId(1L);
        testBus.setTravelsName("Test Travels");
        testBus.setPricePerSeat(500.0);

        testBooking = new Booking();
        testBooking.setId(1L);
        testBooking.setBus(testBus);
    }

    @Test
    void testCheckout_Success() {
        when(session.getAttribute("loggedInPassengerId")).thenReturn(1L);
        when(session.getAttribute("loggedInUser")).thenReturn("passenger@example.com");
        when(busService.getBusById(1L)).thenReturn(testBus);

        String viewName = bookingController.checkout(
                1L, "A1,A2",
                Arrays.asList("John", "Doe"),
                Arrays.asList(25, 30),
                Arrays.asList("Male", "Female"),
                session, model
        );

        assertEquals("checkout", viewName);
        verify(model, times(1)).addAttribute(eq("bus"), any(Bus.class));
        verify(model, times(1)).addAttribute(eq("seatNumbers"), anyList());
        verify(model, times(1)).addAttribute(eq("passengerList"), anyList());
        verify(model, times(1)).addAttribute(eq("totalPrice"), eq(1000.0));
    }

    @Test
    void testCheckout_Failure_NoPassengerInSession() {
        when(session.getAttribute("loggedInPassengerId")).thenReturn(null);

        String viewName = bookingController.checkout(
                1L, "A1,A2",
                Arrays.asList("John", "Doe"),
                Arrays.asList(25, 30),
                Arrays.asList("Male", "Female"),
                session, model
        );

        assertEquals("redirect:/passenger/passengerlogin", viewName);
    }

    @Test
    void testConfirmBooking_Success() {
        when(session.getAttribute("loggedInPassengerId")).thenReturn(1L);
        when(busService.getBusById(1L)).thenReturn(testBus);
        when(bookingService.saveBooking(anyLong(), any(), anyList(), anyList(), anyList(), anyList(), anyDouble()))
                .thenReturn(testBooking);

        String viewName = bookingController.confirmBooking(
                1L, "A1,A2",
                Arrays.asList("John", "Doe"),
                Arrays.asList(25, 30),
                Arrays.asList("Male", "Female"),
                1000.0, session, model
        );

        assertEquals("booking-confirmation", viewName);
        verify(model, times(1)).addAttribute("bookingId", testBooking.getId());
    }


    @Test
    void testDownloadTicket_Success() {
        when(bookingService.findById(1L)).thenReturn(Optional.of(testBooking));
        when(pdfGeneratorService.generateBookingTicket(testBooking)).thenReturn(new byte[]{1, 2, 3});

        ResponseEntity<byte[]> response = bookingController.downloadTicket(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertArrayEquals(new byte[]{1, 2, 3}, response.getBody());
    }
}
