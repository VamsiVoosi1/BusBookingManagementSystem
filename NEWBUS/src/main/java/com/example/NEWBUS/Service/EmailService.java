package com.example.NEWBUS.Service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendBookingConfirmation(String recipientEmail, byte[] pdfBytes) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(recipientEmail);
            helper.setSubject("🎟️ Your Bus Ticket Confirmation");
            helper.setText("Dear Passenger,\n\nYour bus ticket has been successfully booked. Please find your ticket attached.\n\nThank you!");

            // ✅ Attach PDF
            helper.addAttachment("Bus_Ticket.pdf", new ByteArrayResource(pdfBytes));

            mailSender.send(message);
            System.out.println("✅ Ticket Email Sent to: " + recipientEmail);

        } catch (MessagingException e) {
            throw new RuntimeException("🚨 Error: Unable to send email!", e);
        }
    }
}
