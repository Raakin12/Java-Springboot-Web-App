package com.group20.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class NotificationService {

    private final JavaMailSender mailSender;

    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendBookingConfirmationEmail(String recipientEmail, Long bookingId, String movieName,
            String showtime, String theatreName, String seatNumber, String date) {
        System.out.println("attempting to send an email" + recipientEmail);

        String subject = "Booking Confirmation";
        String htmlContent = getBookingConfirmationTemplate(bookingId, movieName, date, showtime, theatreName,
                seatNumber);
        sendEmail(recipientEmail, subject, htmlContent);
    }

    public void sendCancellationEmail(String recipientEmail, Long creditId, String expiryDate, double creditAmount) {
        String subject = "Cancellation Confirmation";
        String htmlContent = getCancellationConfirmationTemplate(creditId, creditAmount, expiryDate);
        sendEmail(recipientEmail, subject, htmlContent);
    }

    public void sendSignUpReceiptEmail(String recipientEmail, String firstName, String lastName,
            String renewalDate, double paymentAmount) {
        String subject = "Sign-Up Receipt";
        String htmlContent = getSignUpReceiptTemplate(firstName, lastName, recipientEmail, paymentAmount, renewalDate);
        sendEmail(recipientEmail, subject, htmlContent);
    }

    public void sendReceiptConfirmationEmail(String recipientEmail, Long bookingId,
            String movieName,
            double paymentAmount) {
        String subject = "Purchase Receipt";
        String htmlContent = getReceiptConfirmationEmail(recipientEmail, bookingId, movieName,
                paymentAmount);
        sendEmail(recipientEmail, subject, htmlContent);
    }

    private void sendEmail(String recipientEmail, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    "UTF-8");

            helper.setTo(recipientEmail);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            System.out.println(subject + " email sent successfully to " + recipientEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Failed to send email: " + e.getMessage());
        }
    }

    private String getBookingConfirmationTemplate(Long bookingId, String movieName, String date,
            String showtime, String theatreName, String seatNumber) {
        return String.format(
                """
                        <!DOCTYPE html>
                        <html>
                        <head>
                        <meta charset="UTF-8">
                        <title>Booking Confirmation</title>
                        <style>
                        body { font-family: Arial, sans-serif; background-color: #f4f4f9; margin: 0; padding: 0; }
                        .ticket { background: #fff; border: 2px dashed #ccc; width: 600px; margin: 20px auto; padding: 20px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
                        .ticket-header { background-color: #333; color: #fff; text-align: center; padding: 15px 0; font-size: 1.5em; letter-spacing: 2px; }
                        .ticket-details { padding: 20px; border-top: 1px dashed #ccc; border-bottom: 1px dashed #ccc; }
                        .detail-item { display: flex; justify-content: space-between; padding: 10px 0; font-size: 1.2em; }
                        .label { font-weight: bold; }
                        .highlight { color: #333; font-weight: bold; font-size: 1.2em; }
                        .footer { text-align: center; color: #666; font-size: 0.9em; padding-top: 10px; }
                        </style>
                        </head>
                        <body>
                        <div class="ticket">
                        <div class="ticket-header">Movie Ticket Confirmation</div>
                        <div class="ticket-details">
                        <div class="detail-item"><div class="label">Booking ID:</div><div class="highlight">#%d</div></div>
                        <div class="detail-item"><div class="label">Movie:</div><div>%s</div></div>
                        <div class="detail-item"><div class="label">Movie Date:</div><div>%s</div></div>
                        <div class="detail-item"><div class="label">Showtime:</div><div>%s</div></div>
                        <div class="detail-item"><div class="label">Theatre:</div><div>%s</div></div>
                        <div class="detail-item"><div class="label">Seat:</div><div>%s</div></div>
                        </div>
                        <div class="footer">Please arrive 15 minutes early. Enjoy your movie!</div>
                        </div>
                        </body>
                        </html>
                        """,
                bookingId, movieName, date, showtime, theatreName, seatNumber);
    }

    private String getCancellationConfirmationTemplate(Long creditId, double creditAmount, String expiryDate) {
        return String.format(
                """
                        <!DOCTYPE html>
                        <html lang="en">
                        <head>
                            <meta charset="UTF-8">
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                            <style>
                                body { font-family: Arial, sans-serif; background-color: #f4f4f9; margin: 0; padding: 0; }
                                .email-container { background: #fff; width: 600px; margin: 20px auto; padding: 20px; box-shadow: 0 0 10px rgba(0,0,0,0.1); border-radius: 8px; }
                                .header { background: linear-gradient(135deg, #6a11cb, #2575fc); color: white; text-align: center; padding: 20px; font-size: 24px; }
                                .content { padding: 20px; text-align: center; }
                                .credit-card { background-color: #f5f7fa; padding: 15px; border-radius: 8px; border: 1px solid #dcdfe6; margin: 15px 0; }
                                .credit-card h2 { font-size: 22px; color: #6a11cb; }
                                .credit-card p { font-size: 18px; margin: 5px 0; }
                                .highlight { font-size: 24px; font-weight: bold; color: #333; }
                                .footer { text-align: center; font-size: 12px; color: #888; margin-top: 15px; }
                            </style>
                        </head>
                        <body>
                            <div class="email-container">
                                <div class="header">🎁 Your Cancellation Credit 🎁</div>
                                <div class="content">
                                    <p>Hi there,</p>
                                    <p>You have been issued a credit for your cancelled booking:</p>
                                    <div class="credit-card">
                                        <h2>Credit Details</h2>
                                        <p>Credit ID: <span class="highlight">%d</span></p>
                                        <p>Amount: <span class="highlight">$%.2f</span></p>
                                        <p>Expiry Date: <span class="highlight">%s</span></p>
                                    </div>
                                    <p>You can use this credit for your future bookings.</p>
                                </div>
                                <div class="footer">Thank you for being a valued customer!</div>
                            </div>
                        </body>
                        </html>
                        """,
                creditId, creditAmount, expiryDate);
    }

    private String getSignUpReceiptTemplate(String firstName, String lastName, String recipientEmail,
            double paymentAmount, String renewalDate) {
        return String.format(
                """
                        <!DOCTYPE html>
                        <html>
                        <head>
                        <meta charset="UTF-8">
                        <title>Sign-Up Receipt</title>
                        <style>
                        body { font-family: Arial, sans-serif; background-color: #f4f4f9; margin: 0; padding: 0; }
                        .receipt { background: #fff; border: 2px dashed #ccc; width: 600px; margin: 20px auto; padding: 20px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
                        .receipt-header { background-color: #333; color: #fff; text-align: center; padding: 15px 0; font-size: 1.5em; letter-spacing: 2px; }
                        .receipt-details { padding: 20px; border-top: 1px dashed #ccc; border-bottom: 1px dashed #ccc; }
                        .detail-item { display: flex; justify-content: space-between; padding: 10px 0; font-size: 1.2em; }
                        .label { font-weight: bold; }
                        .highlight { color: #333; font-weight: bold; font-size: 1.2em; }
                        .footer { text-align: center; color: #666; font-size: 0.9em; padding-top: 10px; }
                        </style>
                        </head>
                        <body>
                        <div class="receipt">
                        <div class="receipt-header">Sign-Up Receipt</div>
                        <div class="receipt-details">
                        <div class="detail-item"><div class="label">User:</div><div>%s %s</div></div>
                        <div class="detail-item"><div class="label">Email:</div><div>%s</div></div>
                        <div class="detail-item"><div class="label">Payment Amount:</div><div class="highlight">$%.2f</div></div>
                        <div class="detail-item"><div class="label">Next Renewal Date:</div><div class="highlight">%s</div></div>
                        </div>
                        <div class="footer">Thank you for signing up! Please contact support if you have any questions.</div>
                        </div>
                        </body>
                        </html>
                        """,
                firstName, lastName, recipientEmail, paymentAmount, renewalDate);
    }

    private String getReceiptConfirmationEmail(String recipientEmail, Long bookingId,
            String movieName, double paymentAmount) {
        return String.format(
                """
                        <!DOCTYPE html>
                        <html>
                        <head>
                        <meta charset="UTF-8">
                        <title>Booking Receipt</title>
                        <style>
                        body { font-family: Arial, sans-serif; background-color: #f4f4f9; margin: 0; padding: 0; }
                        .ticket { background: #fff; border: 2px dashed #ccc; width: 600px; margin: 20px auto; padding: 20px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
                        .ticket-header { background-color: #333; color: #fff; text-align: center; padding: 15px 0; font-size: 1.5em; letter-spacing: 2px; }
                        .ticket-details { padding: 20px; border-top: 1px dashed #ccc; border-bottom: 1px dashed #ccc; }
                        .detail-item { display: flex; justify-content: space-between; padding: 10px 0; font-size: 1.2em; }
                        .label { font-weight: bold; }
                        .highlight { color: #333; font-weight: bold; font-size: 1.2em; }
                        .footer { text-align: center; color: #666; font-size: 0.9em; padding-top: 10px; }
                        </style>
                        </head>
                        <body>
                        <div class="ticket">
                        <div class="ticket-header">Movie Receipt Confirmation</div>
                        <div class="ticket-details">
                        <div class="detail-item"><div class="label">Email:</div><div>%s</div></div>
                        <div class="detail-item"><div class="label">Booking ID:</div><div class="highlight">#%d</div></div>
                        <div class="detail-item"><div class="label">Purchased Movie:</div><div>%s</div></div>
                        <div class="detail-item"><div class="label">Payment Amount:</div><div>%s $</div></div>
                        </div>
                        <div class="footer">Please save a copy of this receipt. You will also receive an email with your booking details.<br>Thank you!</div>
                        </div>
                        </body>
                        </html>
                        """,
                recipientEmail, bookingId, movieName, paymentAmount);
    }
}
