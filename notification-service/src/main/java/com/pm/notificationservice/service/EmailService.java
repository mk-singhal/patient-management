package com.pm.notificationservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.Body;
import software.amazon.awssdk.services.sesv2.model.Content;
import software.amazon.awssdk.services.sesv2.model.Destination;
import software.amazon.awssdk.services.sesv2.model.EmailContent;
import software.amazon.awssdk.services.sesv2.model.Message;
import software.amazon.awssdk.services.sesv2.model.SendEmailRequest;

@Service
public class EmailService {

    private final SesV2Client sesClient;
    private final String sender;

    public EmailService(
            SesV2Client sesClient,
            @Value("${aws.ses.sender}") String sender) {
        this.sesClient = sesClient;
        this.sender = sender;
    }

    public void sendWelcomeEmail(String name, String recipient) {

        String subject = "Welcome to Patient Management System";

        String body = """
                Hi %s,

                Welcome to Patient Management System!

                Your registration was successful.
                We're glad to have you onboard.

                Regards,
                Patient Management System
                """.formatted(name);

        SendEmailRequest request = SendEmailRequest.builder()
                .fromEmailAddress(sender)
                .destination(Destination.builder()
                        .toAddresses(recipient)
                        .build())
                .content(EmailContent.builder()
                        .simple(Message.builder()
                                .subject(Content.builder()
                                        .data(subject)
                                        .build())
                                .body(Body.builder()
                                        .text(Content.builder()
                                                .data(body)
                                                .build())
                                        .build())
                                .build())
                        .build())
                .build();

        sesClient.sendEmail(request);
    }
}