package clinix.com.clinix_sistema_usuarios;

import clinix.com.clinix_sistema_usuarios.dto.MedicoRmiDTO;
import clinix.com.clinix_sistema_usuarios.dto.PacienteRmiDTO;
import clinix.com.clinix_sistema_usuarios.service.UsuarioService;

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

