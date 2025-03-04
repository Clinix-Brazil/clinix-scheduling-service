package com.clinix.api.clinixschedulingservice.rmi;

import com.clinix.api.dto.MedicoRmiDTO;
import com.clinix.api.dto.PacienteRmiDTO;
import com.clinix.api.interfaces.UsuarioService;

import java.rmi.Naming;

import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceClient {

    private UsuarioService usuarioService;

    public UsuarioServiceClient() {
        try {
            usuarioService = (UsuarioService) Naming.lookup("rmi://localhost:1099/UsuarioService");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MedicoRmiDTO getMedico(Long id) {
        try {
            return usuarioService.getMedicoPorId(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public PacienteRmiDTO getPaciente(Long id) {
        try {
            return usuarioService.getPacientePorId(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
