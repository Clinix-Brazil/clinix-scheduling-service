package com.clinix.api.clinixschedulingservice.controller;

import com.clinix.api.clinixschedulingservice.model.AppointmentScheduling;
import com.clinix.api.clinixschedulingservice.model.AppointmentStatus;
import com.clinix.api.clinixschedulingservice.service.AppointmentSchedulingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/appointment")
public class AppointmentSchedulingController {

    private final AppointmentSchedulingService appointmentService;

    public AppointmentSchedulingController(AppointmentSchedulingService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<AppointmentScheduling> create(@Valid @RequestBody AppointmentScheduling appointment) {
        AppointmentScheduling savedAppointment = appointmentService.save(appointment);
        return ResponseEntity.ok(savedAppointment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentScheduling> getById(@PathVariable Long id) {
        Optional<AppointmentScheduling> appointment = appointmentService.findById(id);
        return appointment.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/list")
    public List<AppointmentScheduling> getAll() {
        return appointmentService.findAll();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AppointmentScheduling> updateStatus(
            @PathVariable Long id,
            @RequestParam AppointmentStatus status,
            @RequestParam(required = false) String cancellationReason) {

        Optional<AppointmentScheduling> updatedAppointment = appointmentService.updateStatus(id, status, cancellationReason);

        return updatedAppointment.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long id, @RequestParam String reason) {
        boolean canceled = appointmentService.cancel(id, reason);
        return canceled ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = appointmentService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
