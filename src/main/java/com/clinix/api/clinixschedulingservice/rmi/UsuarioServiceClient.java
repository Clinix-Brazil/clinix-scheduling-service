package com.clinix.api.clinixschedulingservice.rmi;

import com.clinix.api.dto.MedicoRmiDTO;
import com.clinix.api.dto.PacienteRmiDTO;
import com.clinix.api.interfaces.UsuarioService;
import com.clinix.api.rmi.RmiClientHelper;

import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceClient {
    private UsuarioService usuarioService;

    public UsuarioServiceClient() {
        this.usuarioService = RmiClientHelper.connect("rmi://localhost:1099/UsuarioService", UsuarioService.class);
    }

    public MedicoRmiDTO getMedico(Long id) {
        try {
            return usuarioService != null ? usuarioService.getMedicoPorId(id) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public PacienteRmiDTO getPaciente(Long id) {
        try {
            return usuarioService != null ? usuarioService.getPacientePorId(id) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}