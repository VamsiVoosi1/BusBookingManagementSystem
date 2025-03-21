package com.example.NEWBUS.ServiceTest;

import com.example.NEWBUS.Entity.Bus;
import com.example.NEWBUS.Repository.BusRepository;
import com.example.NEWBUS.Service.BusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BusServiceTest {

    @Mock
    private BusRepository busRepository;

    @InjectMocks
    private BusService busService;

    private Bus bus1;
    private Bus bus2;

    @BeforeEach
    void setUp() {
        bus1 = new Bus();
        bus1.setId(1L);
        bus1.setFromLocation("CityA");
        bus1.setToLocation("CityB");
        bus1.setTravelDate(LocalDate.of(2025, 5, 20));

        bus2 = new Bus();
        bus2.setId(2L);
        bus2.setFromLocation("CityC");
        bus2.setToLocation("CityD");
        bus2.setTravelDate(LocalDate.of(2025, 6, 15));
    }

    @Test
    void testAddBus() {
        busService.addBus(bus1);
        verify(busRepository, times(1)).save(bus1);
    }

    @Test
    void testGetAllBuses() {
        when(busRepository.findAll()).thenReturn(Arrays.asList(bus1, bus2));

        List<Bus> buses = busService.getAllBuses();

        assertNotNull(buses);
        assertEquals(2, buses.size());
        verify(busRepository, times(1)).findAll();
    }

    @Test
    void testSaveBus() {
        when(busRepository.save(bus1)).thenReturn(bus1);

        Bus savedBus = busService.saveBus(bus1);

        assertNotNull(savedBus);
        assertEquals(1L, savedBus.getId());
        verify(busRepository, times(1)).save(bus1);
    }

    @Test
    void testSearchBuses_Found() {
        when(busRepository.findByFromLocationAndToLocationAndTravelDate("CityA", "CityB", LocalDate.of(2025, 5, 20)))
                .thenReturn(Arrays.asList(bus1));

        List<Bus> result = busService.searchBuses("CityA", "CityB", LocalDate.of(2025, 5, 20));

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("CityA", result.get(0).getFromLocation());
        assertEquals("CityB", result.get(0).getToLocation());
        verify(busRepository, times(1))
                .findByFromLocationAndToLocationAndTravelDate("CityA", "CityB", LocalDate.of(2025, 5, 20));
    }

    @Test
    void testSearchBuses_NotFound() {
        when(busRepository.findByFromLocationAndToLocationAndTravelDate("CityX", "CityY", LocalDate.of(2025, 7, 10)))
                .thenReturn(List.of());

        List<Bus> result = busService.searchBuses("CityX", "CityY", LocalDate.of(2025, 7, 10));

        assertTrue(result.isEmpty());
        verify(busRepository, times(1))
                .findByFromLocationAndToLocationAndTravelDate("CityX", "CityY", LocalDate.of(2025, 7, 10));
    }

    @Test
    void testGetBusById_Found() {
        when(busRepository.findById(1L)).thenReturn(Optional.of(bus1));

        Bus foundBus = busService.getBusById(1L);

        assertNotNull(foundBus);
        assertEquals(1L, foundBus.getId());
        verify(busRepository, times(1)).findById(1L);
    }

    @Test
    void testGetBusById_NotFound() {
        when(busRepository.findById(3L)).thenReturn(Optional.empty());

        Bus foundBus = busService.getBusById(3L);

        assertNull(foundBus);
        verify(busRepository, times(1)).findById(3L);
    }
}
