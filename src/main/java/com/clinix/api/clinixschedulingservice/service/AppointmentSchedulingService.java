package com.clinix.api.clinixschedulingservice.service;

import com.clinix.api.clinixschedulingservice.model.AppointmentScheduling;
import com.clinix.api.clinixschedulingservice.model.AppointmentStatus;
import com.clinix.api.clinixschedulingservice.repository.AppointmentSchedulingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentSchedulingService {

    private final AppointmentSchedulingRepository appointmentSchedulingRepository;

    public AppointmentSchedulingService(AppointmentSchedulingRepository appointmentSchedulingRepository) {
        this.appointmentSchedulingRepository = appointmentSchedulingRepository;
    }

    public List<AppointmentScheduling> findAll() {
        return appointmentSchedulingRepository.findAll();
    }

    public Optional<AppointmentScheduling> findById(Long id) {
        return appointmentSchedulingRepository.findById(id);
    }

    public AppointmentScheduling save(AppointmentScheduling appointment) {
        return appointmentSchedulingRepository.save(appointment);
    }

    public Optional<AppointmentScheduling> updateStatus(Long id, AppointmentStatus status, String cancellationReason) {
        return appointmentSchedulingRepository.findById(id).map(appointment -> {
            appointment.setAppointmentStatus(status);
            if (status == AppointmentStatus.CANCELED) {
                appointment.setCancellationReason(cancellationReason);
            }
            return appointmentSchedulingRepository.save(appointment);
        });
    }

    public boolean cancel(Long id, String reason) {
        return appointmentSchedulingRepository.findById(id).map(appointment -> {
            appointment.setAppointmentStatus(AppointmentStatus.CANCELED);
            appointment.setCancellationReason(reason);
            appointmentSchedulingRepository.save(appointment);
            return true;
        }).orElse(false);
    }

    public boolean delete(Long id) {
        if (appointmentSchedulingRepository.existsById(id)) {
            appointmentSchedulingRepository.deleteById(id);
            return true;
        }
        return false;
    }
}