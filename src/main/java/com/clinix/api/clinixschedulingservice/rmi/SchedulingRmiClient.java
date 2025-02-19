package com.clinix.api.clinixschedulingservice.rmi;

import com.clinix.api.clinixschedulingservice.usuarios.MedicoRmiDTO;
import com.clinix.api.clinixschedulingservice.usuarios.PacienteRmiDTO;
import com.clinix.api.clinixschedulingservice.usuarios.UsuarioService;

import java.rmi.Naming;

public class SchedulingRmiClient {
    public static void main(String[] args) {
        try {
            UsuarioService service = (UsuarioService) Naming.lookup("rmi://localhost:1099/UsuarioService");

            MedicoRmiDTO medicoRmiDTO = service.getMedicoPorId(1L);
            PacienteRmiDTO pacienteRmiDTO = service.getPacientePorId(1L);

            System.out.println("Médico Obtido: " + medicoRmiDTO);
            System.out.println("PacienteRmiDTO Obtido: " + pacienteRmiDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

