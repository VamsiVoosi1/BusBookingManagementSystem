package com.example.NEWBUS.Service;

import com.example.NEWBUS.Entity.Booking;
import com.example.NEWBUS.Entity.Passenger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

@Service
public class PDFGeneratorService {

    public byte[] generateBookingTicket(Booking booking) {
        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Load Images (Logo & Background)
            PDImageXObject logo = PDImageXObject.createFromFile("src/main/resources/static/ZENBUSLOGO.png", document);
            PDImageXObject flowerBorder = PDImageXObject.createFromFile("src/main/resources/static/PDFBG.jpg", document);

            // Apply Glassmorphism Effect (Background)
            contentStream.setNonStrokingColor(245, 245, 245);
            contentStream.addRect(20, 20, page.getMediaBox().getWidth() - 40, page.getMediaBox().getHeight() - 40);
            contentStream.fill();

            // Draw Border Image
            contentStream.drawImage(flowerBorder, 10, 10, page.getMediaBox().getWidth() - 20, page.getMediaBox().getHeight() - 20);

            // Logo & Title
            contentStream.drawImage(logo, 520, 700, 60, 60);
            contentStream.setFont(PDType1Font.TIMES_BOLD_ITALIC, 20);
            contentStream.setNonStrokingColor(0, 0, 0);

            contentStream.beginText();
            contentStream.newLineAtOffset(170, 720);
            contentStream.showText("ZenBus - Your Smart Travel Companion");
            contentStream.endText();

            contentStream.setFont(PDType1Font.TIMES_BOLD, 16);
            contentStream.beginText();
            contentStream.newLineAtOffset(190, 670);
            contentStream.showText("ELECTRONIC RESERVATION TICKET");
            contentStream.endText();

            // Set Line Color (Black)
            contentStream.setStrokingColor(0, 0, 0);
            // Move to Start Position (Left)
            contentStream.moveTo(190, 665); // Adjust Y value to position above "Travel Agency"
            // Draw Line to Right
            contentStream.lineTo(485, 665); // Adjust width as needed
            // Stroke the Line (Make it Visible)
            contentStream.setLineWidth(1.5f); // Set thickness
            contentStream.stroke();

            // Travel Details
            contentStream.setFont(PDType1Font.COURIER_BOLD, 16);
            contentStream.beginText();
            contentStream.newLineAtOffset(230, 640);
            contentStream.showText("Travel Agency: " + booking.getBus().getTravelsName());
            contentStream.endText();

            // Booking Info Table
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(180, 600);
            contentStream.showText("Booked From: " + booking.getBus().getFromLocation()+"            --------->");
            contentStream.newLineAtOffset(280, 0);
            contentStream.showText("To: " + booking.getBus().getToLocation());
            contentStream.endText();

            contentStream.beginText();
            contentStream.newLineAtOffset(180, 580);
            contentStream.showText("Departure: " + booking.getBus().getDepartureTime());
            contentStream.newLineAtOffset(280, 0);
            contentStream.showText("Arrival: " + booking.getBus().getArrivalTime());
            contentStream.endText();

            // DOJ
            contentStream.setFont(PDType1Font.COURIER_BOLD, 14);
            contentStream.beginText();
            contentStream.newLineAtOffset(280, 550);
            contentStream.showText("DOJ: " + booking.getBus().getTravelDate());
            contentStream.endText();

            contentStream.setLineDashPattern(new float[]{10, 3, 3, 3}, 0); // Dash, Space, Dot, Space
            contentStream.moveTo(180, 540);
            contentStream.lineTo(540, 540);
            contentStream.setLineWidth(2);
            contentStream.stroke();


            // Passenger Details
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
            contentStream.beginText();
            contentStream.newLineAtOffset(140, 500);
            contentStream.showText("Passenger Details:");
            contentStream.endText();

            // Passenger Table
            float startY = 470;
            float rowHeight = 20;
            float[] colWidths = {50, 190, 55, 55, 80};
            String[] headers = {"S.No", "Passenger Name", "Age", "Gender", "Seat No"};

            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            float xOffset = 140;
            for (int i = 0; i < headers.length; i++) {
                contentStream.beginText();
                contentStream.newLineAtOffset(xOffset, startY);
                contentStream.showText(headers[i]);
                contentStream.endText();
                xOffset += colWidths[i];
            }
            // Passenger Data Rows
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            float currentY = startY - rowHeight;
            int count = 1;
            for (Passenger passenger : booking.getPassengers()) {
                xOffset = 140;
                contentStream.beginText();
                contentStream.newLineAtOffset(xOffset, currentY);
                contentStream.showText(String.valueOf(count));
                contentStream.newLineAtOffset(colWidths[0], 0);
                contentStream.showText(passenger.getName());
                contentStream.newLineAtOffset(colWidths[1], 0);
                contentStream.showText(String.valueOf(passenger.getAge()));
                contentStream.newLineAtOffset(colWidths[2], 0);
                contentStream.showText(passenger.getGender());
                contentStream.newLineAtOffset(colWidths[3], 0);
                contentStream.showText(passenger.getSeatNumber());
                contentStream.endText();

                currentY -= rowHeight;
                count++;
            }

            // Payment Details
            int totalPrice = (int) booking.getTotalPrice();
            String amountInWords = convertNumberToWords(totalPrice) + " Rupees Only";

            contentStream.setFont(PDType1Font.TIMES_BOLD_ITALIC, 16);
            contentStream.beginText();
            contentStream.newLineAtOffset(70, currentY - 90);
            contentStream.showText("Payment Details:");
            contentStream.endText();

            contentStream.setFont(PDType1Font.TIMES_BOLD_ITALIC, 14);
            contentStream.beginText();
            contentStream.newLineAtOffset(70, currentY - 110);
            contentStream.showText("Total Price in INR: "+totalPrice);
            contentStream.endText();

            contentStream.setFont(PDType1Font.TIMES_ITALIC, 14);
            contentStream.beginText();
            contentStream.newLineAtOffset(70, currentY - 130);
            contentStream.showText("Amount in Words: "+amountInWords);
            contentStream.endText();

            // Footer
            contentStream.setFont(PDType1Font.COURIER_BOLD_OBLIQUE, 22);
            contentStream.beginText();
            contentStream.newLineAtOffset(100, 100);
            contentStream.showText("ZenBus wishes you a Happy Journey!");
            contentStream.endText();

            // Close content stream
            contentStream.close();
            document.save(outputStream);
            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Error: PDF Generation Failed!", e);
        }
    }

    // ✅ Convert Total Price to Words
    private String convertNumberToWords(int number) {
        if (number == 0) return "Zero";

        String[] units = {"", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten",
                "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};

        String[] tens = {"", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"};

        StringBuilder words = new StringBuilder();

        if (number >= 10000000) { // Crore
            words.append(convertNumberToWords(number / 10000000)).append(" Crore ");
            number %= 10000000;
        }

        if (number >= 100000) { // Lakh
            words.append(convertNumberToWords(number / 100000)).append(" Lakh ");
            number %= 100000;
        }

        if (number >= 1000) { // Thousand
            words.append(convertNumberToWords(number / 1000)).append(" Thousand ");
            number %= 1000;
        }

        if (number >= 100) { // Hundred
            words.append(units[number / 100]).append(" Hundred ");
            number %= 100;
        }

        if (number > 0) { // Remaining Tens & Units
            if (number < 20) {
                words.append(units[number]);
            } else {
                words.append(tens[number / 10]);
                if (number % 10 != 0) {
                    words.append(" ").append(units[number % 10]);
                }
            }
        }

        return words.toString().trim();
    }

}
