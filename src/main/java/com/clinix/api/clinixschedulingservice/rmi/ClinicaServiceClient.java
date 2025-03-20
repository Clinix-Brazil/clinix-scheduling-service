package com.clinix.api.clinixschedulingservice.rmi;

import com.clinix.api.dto.ClinicaRmiDTO;
import com.clinix.api.dto.MedicoRmiDTO;
import com.clinix.api.dto.PacienteRmiDTO;
import com.clinix.api.interfaces.ClinicaService;
import com.clinix.api.interfaces.UsuarioService;

import java.rmi.Naming;

import com.clinix.api.rmi.RmiClientHelper;
import org.springframework.stereotype.Service;

@Service
public class ClinicaServiceClient {

    private ClinicaService clinicaService;

    public ClinicaServiceClient() {
        this.clinicaService = RmiClientHelper.connect("rmi://localhost:1099/ClinicaService", ClinicaService.class);
    }

    public ClinicaRmiDTO getClinica(Long id) {
        try {
            return clinicaService != null ? clinicaService.getClinicaPorId(id) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

