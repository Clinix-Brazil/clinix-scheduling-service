package com.clinix.api.clinixschedulingservice.service;

import com.clinix.api.clinixschedulingservice.dto.AppointmentSchedulingDTO;
import com.clinix.api.clinixschedulingservice.model.AppointmentScheduling;
import com.clinix.api.clinixschedulingservice.model.AppointmentStatus;
import com.clinix.api.clinixschedulingservice.repository.AppointmentSchedulingRepository;
import com.clinix.api.clinixschedulingservice.rmi.ClinicaServiceClient;
import com.clinix.api.clinixschedulingservice.rmi.UsuarioServiceClient;
import com.clinix.api.dto.ClinicaRmiDTO;
import com.clinix.api.dto.MedicoRmiDTO;
import com.clinix.api.dto.PacienteRmiDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentSchedulingService {

    private final AppointmentSchedulingRepository appointmentSchedulingRepository;
    private final UsuarioServiceClient usuarioServiceClient;
    private final ClinicaServiceClient clinicaServiceClient;

    public AppointmentSchedulingService(AppointmentSchedulingRepository appointmentSchedulingRepository, UsuarioServiceClient usuarioServiceClient, ClinicaServiceClient clinicaServiceClient) {
        this.appointmentSchedulingRepository = appointmentSchedulingRepository;
        this.usuarioServiceClient = usuarioServiceClient;
        this.clinicaServiceClient = clinicaServiceClient;
    }

    public List<AppointmentSchedulingDTO> findAll() {
        List<AppointmentScheduling> appointments = appointmentSchedulingRepository.findAll();
        return appointments.stream().map(this::convertToDTO).toList();
    }

    public Optional<AppointmentSchedulingDTO> findById(Long id) {
        return appointmentSchedulingRepository.findById(id).map(this::convertToDTO);
    }

    public AppointmentScheduling save(AppointmentScheduling appointment) {
        MedicoRmiDTO medico = usuarioServiceClient.getMedico(appointment.getDoctorId());
        if (medico == null) {
            throw new RuntimeException("Médico não encontrado via RMI!");
        }

        PacienteRmiDTO paciente = usuarioServiceClient.getPaciente(appointment.getPatientId());
        if (paciente == null) {
            throw new RuntimeException("Paciente não encontrado via RMI!");
        }

        ClinicaRmiDTO clinica = clinicaServiceClient.getClinica(appointment.getClinicId());
        if (clinica == null) {
            throw new RuntimeException("Clínica não encontrada via RMI!");
        }

        return appointmentSchedulingRepository.save(appointment);
    }

    public Optional<AppointmentScheduling> updateStatus(Long id, AppointmentStatus status, String cancellationReason) {
        return appointmentSchedulingRepository.findById(id).map(appointment -> {
            appointment.setStatus(status);
            if (status == AppointmentStatus.CANCELADO) {
                appointment.setCancellationReason(cancellationReason);
            }
            return appointmentSchedulingRepository.save(appointment);
        });
    }

    public boolean cancel(Long id, String reason) {
        return appointmentSchedulingRepository.findById(id).map(appointment -> {
            appointment.setStatus(AppointmentStatus.CANCELADO);
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

    private AppointmentSchedulingDTO convertToDTO(AppointmentScheduling appointment) {
        MedicoRmiDTO doctor = usuarioServiceClient.getMedico(appointment.getDoctorId());
        PacienteRmiDTO patient = usuarioServiceClient.getPaciente(appointment.getPatientId());
        ClinicaRmiDTO clinic = clinicaServiceClient.getClinica(appointment.getClinicId());

        return new AppointmentSchedulingDTO(
                appointment.getId(),
                doctor != null ? doctor.nome() : "Desconhecido",
                patient != null ? patient.nome() : "Desconhecido",
                clinic != null ? clinic.nomeFantasia() : "Desconhecida",
                appointment.getDateTime(),
                appointment.getStatus()
        );
    }

}