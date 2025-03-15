package com.clinix.api.clinixschedulingservice.dto;

import com.clinix.api.clinixschedulingservice.model.AppointmentStatus;

import java.time.LocalDateTime;

public record AppointmentSchedulingDTO(
        Long id,
        String doctorName,
        String patientName,
        String clinicName,
        LocalDateTime dateTime,
        AppointmentStatus status
) {}
