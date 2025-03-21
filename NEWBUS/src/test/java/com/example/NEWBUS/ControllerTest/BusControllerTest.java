package com.example.NEWBUS.ControllerTest;

import com.example.NEWBUS.Controller.BusController;
import com.example.NEWBUS.Entity.Bus;
import com.example.NEWBUS.Entity.Seat;
import com.example.NEWBUS.Service.BusService;
import com.example.NEWBUS.Service.SeatService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BusControllerTest {

    @Mock
    private BusService busService;

    @Mock
    private SeatService seatService;

    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    @InjectMocks
    private BusController busController;

    private Bus testBus;
    private List<Bus> busList;

    @BeforeEach
    void setUp() {
        testBus = new Bus();
        testBus.setId(1L);
        testBus.setFromLocation("City A");
        testBus.setToLocation("City B");
        testBus.setTravelDate(LocalDate.of(2025, 4, 10));

        busList = Arrays.asList(testBus);
    }

    @Test
    void testShowAddBusForm() {
        String viewName = busController.showAddBusForm(model);
        assertEquals("add-bus", viewName);
        verify(model, times(1)).addAttribute(eq("bus"), any(Bus.class));
    }

    @Test
    void testAddBus() {
        String viewName = busController.addBus(testBus);
        assertEquals("redirect:/bus/list", viewName);
        verify(busService, times(1)).addBus(testBus);
    }

    @Test
    void testSaveBus() {
        String viewName = busController.saveBus(testBus);
        assertEquals("redirect:/bus/search", viewName);
        verify(busService, times(1)).saveBus(testBus);
    }

    @Test
    void testShowBusList() {
        when(busService.getAllBuses()).thenReturn(busList);

        String viewName = busController.showBusList(model);

        assertEquals("bus-list", viewName);
        verify(model, times(1)).addAttribute("buses", busList);
    }

    @Test
    void testSearchBuses_WithParameters() {
        when(busService.searchBuses("City A", "City B", LocalDate.of(2025, 4, 10))).thenReturn(busList);

        String viewName = busController.searchBuses("City A", "City B", LocalDate.of(2025, 4, 10), model);

        assertEquals("search-bus", viewName);
        verify(model, times(1)).addAttribute("buses", busList);
    }

    @Test
    void testSearchBuses_NoParameters() {
        String viewName = busController.searchBuses(null, null, null, model);

        assertEquals("search-bus", viewName);
        verify(model, times(1)).addAttribute("buses", null);
    }

    @Test
    void testBookSeats_Successful() {
        when(session.getAttribute("loggedInUser")).thenReturn("passenger@example.com");
        when(busService.getBusById(1L)).thenReturn(testBus);
        when(seatService.getSeatsByBusId(1L)).thenReturn(Arrays.asList(new Seat()));

        String viewName = busController.bookSeats(1L, session, model);

        assertEquals("seat-selection", viewName);
        verify(model, times(1)).addAttribute("bus", testBus);
        verify(model, times(1)).addAttribute("seats", Arrays.asList(new Seat()));
        verify(model, times(1)).addAttribute("passengerEmail", "passenger@example.com");
    }

    @Test
    void testBookSeats_NoPassengerLoggedIn() {
        when(session.getAttribute("loggedInUser")).thenReturn(null);

        String viewName = busController.bookSeats(1L, session, model);

        assertEquals("redirect:/passenger/login", viewName);
    }


}
