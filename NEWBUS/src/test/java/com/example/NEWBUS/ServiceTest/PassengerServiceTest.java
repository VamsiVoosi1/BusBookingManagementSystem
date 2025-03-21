package com.example.NEWBUS.ServiceTest;

import com.example.NEWBUS.Dto.PassengerDto;
import com.example.NEWBUS.Entity.Passenger;
import com.example.NEWBUS.Repository.PassengerRepository;
import com.example.NEWBUS.Service.PassengerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PassengerServiceTest {

    @Mock
    private PassengerRepository passengerRepository;

    @InjectMocks
    private PassengerService passengerService;

    @Mock
    private ModelMapper modelMapper;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private Passenger passenger;
    private PassengerDto passengerDto;

    @BeforeEach
    void setUp() {
        passenger = new Passenger();
        passenger.setEmail("test@example.com");
        passenger.setPassword(passwordEncoder.encode("password123"));

        passengerDto = new PassengerDto();
        passengerDto.setEmail("test@example.com");
        passengerDto.setPassword("password123");
    }

    @Test
    void testRegisterPassenger() {
        passengerService.registerPassenger(passenger);
        verify(passengerRepository, times(1)).save(passenger);
    }

    @Test
    void testLoginPassenger() {
        when(passengerRepository.findByEmailAndPassword("test@example.com", "password123"))
                .thenReturn(passenger);

        Passenger result = passengerService.loginPassenger("test@example.com", "password123");
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void testFindByEmail() {
        when(passengerRepository.findByEmail("test@example.com")).thenReturn(Optional.of(passenger));
        when(modelMapper.map(passenger, PassengerDto.class)).thenReturn(passengerDto);

        PassengerDto result = passengerService.findByEmail("test@example.com");

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void testSavePassenger() {
        when(modelMapper.map(passengerDto, Passenger.class)).thenReturn(passenger);
        when(passengerRepository.save(passenger)).thenReturn(passenger);
        when(modelMapper.map(passenger, PassengerDto.class)).thenReturn(passengerDto);

        PassengerDto result = passengerService.savePassenger(passengerDto);

        assertNotNull(result);
        assertEquals(passengerDto.getEmail(), result.getEmail());
        verify(passengerRepository, times(1)).save(passenger);
    }

    @Test
    void testAuthenticate_Success() {
        when(passengerRepository.findByEmail("test@example.com")).thenReturn(Optional.of(passenger));
        when(modelMapper.map(passenger, PassengerDto.class)).thenReturn(passengerDto);

        boolean isAuthenticated = passengerService.authenticate("test@example.com", "password123");
        assertTrue(isAuthenticated);
    }

    @Test
    void testAuthenticate_Failure() {
        when(passengerRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        boolean isAuthenticated = passengerService.authenticate("test@example.com", "wrongpassword");
        assertFalse(isAuthenticated);
    }
}
