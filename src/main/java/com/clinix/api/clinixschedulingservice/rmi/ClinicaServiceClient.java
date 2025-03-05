package com.clinix.api.clinixschedulingservice.rmi;

import com.clinix.api.dto.ClinicaRmiDTO;
import com.clinix.api.dto.MedicoRmiDTO;
import com.clinix.api.dto.PacienteRmiDTO;
import com.clinix.api.interfaces.ClinicaService;
import com.clinix.api.interfaces.UsuarioService;

import java.rmi.Naming;

import org.springframework.stereotype.Service;

@Service
public class ClinicaServiceClient {

    private ClinicaService clinicaService;

    public ClinicaServiceClient() {
        try {
            clinicaService = (ClinicaService) Naming.lookup("rmi://localhost:1099/ClinicaService");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ClinicaRmiDTO getClinica(Long id) {
        try {
            return clinicaService.getClinicaPorId(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

