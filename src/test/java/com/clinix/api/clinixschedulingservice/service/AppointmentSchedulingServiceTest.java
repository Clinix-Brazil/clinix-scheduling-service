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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentSchedulingServiceTest {

    @Mock
    private AppointmentSchedulingRepository appointmentSchedulingRepository;

    @Mock
    private UsuarioServiceClient usuarioServiceClient;

    @Mock
    private ClinicaServiceClient clinicaServiceClient;

    @InjectMocks
    private AppointmentSchedulingService appointmentSchedulingService;

    private AppointmentScheduling appointment;

    @BeforeEach
    void setUp() {
        appointment = new AppointmentScheduling();
        appointment.setId(1L);
        appointment.setDoctorId(10L);
        appointment.setPatientId(20L);
        appointment.setClinicId(30L);
        appointment.setDateTime(LocalDateTime.now());
        appointment.setStatus(AppointmentStatus.AGENDADO);

        MedicoRmiDTO medico = new MedicoRmiDTO(10L, "Dr. João");
        PacienteRmiDTO paciente = new PacienteRmiDTO(20L, "Maria Silva");
        ClinicaRmiDTO clinica = new ClinicaRmiDTO(30L, "Clinica Saúde");

        lenient().when(usuarioServiceClient.getMedico(10L)).thenReturn(medico);
        lenient().when(usuarioServiceClient.getPaciente(20L)).thenReturn(paciente);
        lenient().when(clinicaServiceClient.getClinica(30L)).thenReturn(clinica);
    }

    @Test
    void testFindAll() {
        when(appointmentSchedulingRepository.findAll()).thenReturn(List.of(appointment));

        List<AppointmentSchedulingDTO> result = appointmentSchedulingService.findAll();

        assertEquals(1, result.size());
        assertEquals("Dr. João", result.getFirst().doctorName());
        assertEquals("Maria Silva", result.getFirst().patientName());
        assertEquals("Clinica Saúde", result.getFirst().clinicName());
        verify(appointmentSchedulingRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(appointmentSchedulingRepository.findById(1L)).thenReturn(Optional.of(appointment));

        Optional<AppointmentSchedulingDTO> result = appointmentSchedulingService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Dr. João", result.get().doctorName());
        verify(appointmentSchedulingRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveAppointment() {
        when(appointmentSchedulingRepository.save(any())).thenReturn(appointment);

        AppointmentScheduling savedAppointment = appointmentSchedulingService.save(appointment);

        assertNotNull(savedAppointment);
        verify(appointmentSchedulingRepository, times(1)).save(appointment);
    }

    @Test
    void testSaveAppointmentWithInvalidDoctor() {
        when(usuarioServiceClient.getMedico(10L)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, ()
                -> appointmentSchedulingService.save(appointment));

        assertEquals("Médico não encontrado via RMI!", exception.getMessage());
    }

    @Test
    void testSaveAppointmentWithInvalidPatient() {
        when(usuarioServiceClient.getPaciente(20L)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, ()
                -> appointmentSchedulingService.save(appointment));

        assertEquals("Paciente não encontrado via RMI!", exception.getMessage());
    }

    @Test
    void testSaveAppointmentWithInvalidClinic() {
        when(clinicaServiceClient.getClinica(30L)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, ()
                -> appointmentSchedulingService.save(appointment));

        assertEquals("Clínica não encontrada via RMI!", exception.getMessage());
    }

    @Test
    void testUpdateStatus() {
        when(appointmentSchedulingRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentSchedulingRepository.save(any())).thenReturn(appointment);

        Optional<AppointmentScheduling> updated = appointmentSchedulingService.updateStatus(1L, AppointmentStatus.CONFIRMADO, null);

        assertTrue(updated.isPresent());
        assertEquals(AppointmentStatus.CONFIRMADO, updated.get().getStatus());
        verify(appointmentSchedulingRepository, times(1)).save(appointment);
    }

    @Test
    void testCancelAppointment() {
        when(appointmentSchedulingRepository.findById(1L)).thenReturn(Optional.of(appointment));

        boolean result = appointmentSchedulingService.cancel(1L, "Paciente indisponível");

        assertTrue(result);
        assertEquals(AppointmentStatus.CANCELADO, appointment.getStatus());
        assertEquals("Paciente indisponível", appointment.getCancellationReason());
        verify(appointmentSchedulingRepository, times(1)).save(appointment);
    }

    @Test
    void testCancelAppointmentNotFound() {
        when(appointmentSchedulingRepository.findById(2L)).thenReturn(Optional.empty());

        boolean result = appointmentSchedulingService.cancel(2L, "Motivo qualquer");

        assertFalse(result);
        verify(appointmentSchedulingRepository, never()).save(any());
    }

    @Test
    void testDeleteAppointment() {
        when(appointmentSchedulingRepository.existsById(1L)).thenReturn(true);
        doNothing().when(appointmentSchedulingRepository).deleteById(1L);

        boolean result = appointmentSchedulingService.delete(1L);

        assertTrue(result);
        verify(appointmentSchedulingRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteNonExistentAppointment() {
        when(appointmentSchedulingRepository.existsById(2L)).thenReturn(false);

        boolean result = appointmentSchedulingService.delete(2L);

        assertFalse(result);
        verify(appointmentSchedulingRepository, never()).deleteById(any());
    }
}