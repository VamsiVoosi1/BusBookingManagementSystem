package com.example.NEWBUS.ControllerTest;

import com.example.NEWBUS.Controller.AdminController;
import com.example.NEWBUS.Dto.AdminDto;
import com.example.NEWBUS.Service.AdminService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private AdminService adminService;

    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private AdminController adminController;

    private AdminDto adminDto;

    @BeforeEach
    void setUp() {
        adminDto = new AdminDto();
        adminDto.setEmail("admin@example.com");
        adminDto.setPassword("password");
    }

    @Test
    void testDisplayAbout() {
        String viewName = adminController.displayAbout();
        assertEquals("about", viewName);
    }

    @Test
    void testDisplayServices() {
        String viewName = adminController.displayServices();
        assertEquals("services", viewName);
    }

    @Test
    void testDisplayAdminLogin() {
        String viewName = adminController.displayAdminLogin();
        assertEquals("login-admin", viewName);
    }

    @Test
    void testDisplayAdminHome() {
        String viewName = adminController.displayAdminHome();
        assertEquals("home-admin", viewName);
    }

    @Test
    void testShowRegistrationForm() {
        String viewName = adminController.showRegistrationForm(model);
        assertEquals("register-admin", viewName);
        verify(model, times(1)).addAttribute(eq("admin"), any(AdminDto.class));
    }

    @Test
    void testSaveAdmin_SuccessfulRegistration() {
        when(adminService.findByEmail("admin@example.com")).thenReturn(null);
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = adminController.saveAdmin(adminDto, bindingResult, model);

        assertEquals("redirect:/admin/adminlogin", viewName);
        verify(adminService, times(1)).saveAdmin(adminDto);
    }

    @Test
    void testSaveAdmin_EmailAlreadyExists() {
        when(adminService.findByEmail("admin@example.com")).thenReturn(adminDto);
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = adminController.saveAdmin(adminDto, bindingResult, model);

        assertEquals("register-admin", viewName);
        verify(bindingResult, times(1)).rejectValue("email", "email.exist", "This Email is already Exist");
        verify(model, times(1)).addAttribute("admin", adminDto);
    }

    @Test
    void testLogin_Successful() {
        when(adminService.authenticate("admin@example.com", "password")).thenReturn(true);

        String viewName = adminController.login("admin@example.com", "password", model, session);

        assertEquals("redirect:/admin/adminhome", viewName);
        verify(session, times(1)).setAttribute("loggedInAdmin", "admin@example.com");
    }

    @Test
    void testLogin_Failed() {
        when(adminService.authenticate("admin@example.com", "wrongpassword")).thenReturn(false);

        String viewName = adminController.login("admin@example.com", "wrongpassword", model, session);

        assertEquals("login-admin", viewName);
        verify(model, times(1)).addAttribute("error", "Invalid email or password");
    }

    @Test
    void testLogout() {
        String viewName = adminController.logout(session);

        assertEquals("logout-admin", viewName);
        verify(session, times(1)).invalidate();
    }
}
