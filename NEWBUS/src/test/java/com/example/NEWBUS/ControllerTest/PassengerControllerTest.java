package com.example.NEWBUS.ControllerTest;

import com.example.NEWBUS.Controller.PassengerController;
import com.example.NEWBUS.Dto.PassengerDto;
import com.example.NEWBUS.Entity.Booking;
import com.example.NEWBUS.Entity.Passenger;
import com.example.NEWBUS.Repository.PassengerRepository;
import com.example.NEWBUS.Service.BookingService;
import com.example.NEWBUS.Service.PassengerService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PassengerControllerTest {

    @Mock
    private PassengerService passengerService;

    @Mock
    private BookingService bookingService;

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private PassengerController passengerController;

    private Passenger testPassenger;
    private PassengerDto testPassengerDto;
    private List<Booking> bookingList;

    @BeforeEach
    void setUp() {
        testPassenger = new Passenger();
        testPassenger.setId(1L);
        testPassenger.setEmail("passenger@example.com");

        testPassengerDto = new PassengerDto();
        testPassengerDto.setEmail("passenger@example.com");
        testPassengerDto.setPassword("password");

        bookingList = Arrays.asList(new Booking());
    }

    @Test
    void testDisplayIndex() {
        String viewName = passengerController.displayIndex();
        assertEquals("index", viewName);
    }

    @Test
    void testDisplayDashboard() {
        String viewName = passengerController.displayDashboard();
        assertEquals("home-passenger", viewName);
    }

    @Test
    void testDisplayLogins() {
        String viewName = passengerController.displayLogins();
        assertEquals("loginhome", viewName);
    }

    @Test
    void testLogout() {
        String viewName = passengerController.logout(session);
        assertEquals("logout-passenger", viewName);
        verify(session, times(1)).invalidate();
    }

    @Test
    void testShowLoginPage() {
        String viewName = passengerController.showLoginPage();
        assertEquals("login-passenger", viewName);
    }

    @Test
    void testShowPassengerHome_Success() {
        when(session.getAttribute("loggedInPassenger")).thenReturn(testPassenger);

        String viewName = passengerController.showPassengerHome(session, model);

        assertEquals("home-passenger", viewName);
        verify(model, times(1)).addAttribute("passenger", testPassenger);
    }

    @Test
    void testShowPassengerHome_NoPassengerInSession() {
        when(session.getAttribute("loggedInPassenger")).thenReturn(null);

        String viewName = passengerController.showPassengerHome(session, model);

        assertEquals("redirect:/passenger/login", viewName);
    }

    @Test
    void testShowRegistrationForm() {
        String viewName = passengerController.showRegistrationForm(model);
        assertEquals("register-passenger", viewName);
        verify(model, times(1)).addAttribute(eq("passenger"), any(Passenger.class));
    }

    @Test
    void testSavePassenger_Success() {
        when(passengerService.findByEmail("passenger@example.com")).thenReturn(null);
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = passengerController.savePassenger(testPassengerDto, bindingResult, model);

        assertEquals("redirect:/passenger/login", viewName);
        verify(passengerService, times(1)).savePassenger(testPassengerDto);
    }

    @Test
    void testSavePassenger_EmailAlreadyExists() {
        when(passengerService.findByEmail("passenger@example.com")).thenReturn(testPassengerDto);
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = passengerController.savePassenger(testPassengerDto, bindingResult, model);

        assertEquals("register-passenger", viewName);
        verify(bindingResult, times(1)).rejectValue("email", "email.exists", "This Email is already registered");
        verify(model, times(1)).addAttribute("passenger", testPassengerDto);
    }

    @Test
    void testLogin_Success() {
        when(passengerService.authenticate("passenger@example.com", "password")).thenReturn(true);
        when(passengerRepository.findByEmail("passenger@example.com")).thenReturn(Optional.of(testPassenger));

        String viewName = passengerController.login("passenger@example.com", "password", model, session);

        assertEquals("redirect:/passenger/passengerlogin", viewName);
        verify(session, times(1)).setAttribute("loggedInPassenger", testPassenger);
        verify(session, times(1)).setAttribute("loggedInPassengerId", testPassenger.getId());
        verify(session, times(1)).setAttribute("loggedInUser", "passenger@example.com");
    }

    @Test
    void testLogin_Failed() {
        when(passengerService.authenticate("passenger@example.com", "wrongpassword")).thenReturn(false);

        String viewName = passengerController.login("passenger@example.com", "wrongpassword", model, session);

        assertEquals("login-passenger", viewName);
        verify(model, times(1)).addAttribute("error", "Invalid email or password");
    }



    @Test
    void testBookingHistory_NoPassengerInSession() {
        when(session.getAttribute("loggedInPassenger")).thenReturn(null);

        String viewName = passengerController.bookingHistory(session, model);

        assertEquals("redirect:/passenger/login", viewName);
    }

    @Test
    void testViewProfile_Success() {
        when(session.getAttribute("loggedInPassenger")).thenReturn(testPassenger);

        String viewName = passengerController.viewProfile(session, model);

        assertEquals("profile-passenger", viewName);
        verify(model, times(1)).addAttribute("passenger", testPassenger);
    }

    @Test
    void testViewProfile_NoPassengerInSession() {
        when(session.getAttribute("loggedInPassenger")).thenReturn(null);

        String viewName = passengerController.viewProfile(session, model);

        assertEquals("redirect:/passenger/login", viewName);
    }
}
