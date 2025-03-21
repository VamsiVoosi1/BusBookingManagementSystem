package com.example.NEWBUS.ServiceTest;

import com.example.NEWBUS.Dto.AdminDto;
import com.example.NEWBUS.Entity.Admin;
import com.example.NEWBUS.Repository.AdminRepository;
import com.example.NEWBUS.Service.AdminServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AdminServiceImpl adminService;

    private PasswordEncoder passwordEncoder;

    private Admin admin;
    private AdminDto adminDto;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        admin = new Admin();
        admin.setId(1L);
        admin.setEmail("admin@example.com");
        admin.setPassword(passwordEncoder.encode("password"));

        adminDto = new AdminDto();
        adminDto.setEmail("admin@example.com");
        adminDto.setPassword("password");
    }

    @Test
    void testSaveAdmin() {
        when(modelMapper.map(adminDto, Admin.class)).thenReturn(admin);
        when(adminRepository.save(any(Admin.class))).thenReturn(admin);
        when(modelMapper.map(admin, AdminDto.class)).thenReturn(adminDto);

        AdminDto savedAdmin = adminService.saveAdmin(adminDto);

        assertNotNull(savedAdmin);
        assertEquals("admin@example.com", savedAdmin.getEmail());
        verify(adminRepository, times(1)).save(any(Admin.class));
    }

    @Test
    void testFindByEmail() {
        when(adminRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(admin));
        when(modelMapper.map(admin, AdminDto.class)).thenReturn(adminDto);

        AdminDto foundAdmin = adminService.findByEmail("admin@example.com");

        assertNotNull(foundAdmin);
        assertEquals("admin@example.com", foundAdmin.getEmail());
        verify(adminRepository, times(1)).findByEmail("admin@example.com");
    }

    @Test
    void testFindByEmail_NotFound() {
        when(adminRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        AdminDto foundAdmin = adminService.findByEmail("notfound@example.com");

        assertNull(foundAdmin);
        verify(adminRepository, times(1)).findByEmail("notfound@example.com");
    }

    @Test
    void testGetAdminById() {
        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(modelMapper.map(Optional.of(admin), Admin.class)).thenReturn(admin);

        Admin foundAdmin = adminService.getAdminById(1L);

        assertNotNull(foundAdmin);
        assertEquals(1L, foundAdmin.getId());
        verify(adminRepository, times(1)).findById(1L);
    }



    @Test
    void testAuthenticate_WrongPassword() {
        when(adminRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(admin));
        when(modelMapper.map(admin, AdminDto.class)).thenReturn(adminDto);

        boolean isAuthenticated = adminService.authenticate("admin@example.com", "wrongpassword");

        assertFalse(isAuthenticated);
    }
}
