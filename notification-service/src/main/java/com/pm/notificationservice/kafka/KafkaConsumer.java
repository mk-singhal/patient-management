package com.pm.notificationservice.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import com.pm.notificationservice.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Service
public class KafkaConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(KafkaConsumer.class);

    private final EmailService emailService;

    public KafkaConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(
            topics = "patient",
            groupId = "notification-service"
    )
    public void consumerEvent(byte[] event) {

        try {
            PatientEvent patientEvent = PatientEvent.parseFrom(event);

            if (!"PATIENT_CREATED".equals(patientEvent.getEventType())) {
                log.info("Ignoring event type: {}",
                        patientEvent.getEventType());
                return;
            }

            log.info("Processing patient registration: [PatientId={}, PatientName={}, PatientEmail={}]",
                    patientEvent.getPatientId(),
                    patientEvent.getName(),
                    patientEvent.getEmail());

            emailService.sendWelcomeEmail(
                    patientEvent.getName(),
                    patientEvent.getEmail()
            );

            log.info("Welcome email sent for patientId={}",
                    patientEvent.getPatientId());

        } catch (InvalidProtocolBufferException e) {
            log.error("Error deserializing patient event", e);
        }
    }
}