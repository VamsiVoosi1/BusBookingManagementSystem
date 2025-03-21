package com.example.NEWBUS.ServiceTest;

import com.example.NEWBUS.Service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    private MimeMessage mimeMessage;

    @BeforeEach
    void setUp() {
        mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
    }

    @Test
    void testSendBookingConfirmation_Success() throws MessagingException {
        String recipientEmail = "test@example.com";
        byte[] pdfBytes = "Sample PDF Content".getBytes();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        // Mock MimeMessageHelper behavior
        doNothing().when(mailSender).send(mimeMessage);

        emailService.sendBookingConfirmation(recipientEmail, pdfBytes);

        verify(mailSender, times(1)).send(mimeMessage);
    }

}
