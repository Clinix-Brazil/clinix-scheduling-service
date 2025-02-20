package clinix.com.clinix_sistema_usuarios.service;

import clinix.com.clinix_sistema_usuarios.dto.MedicoRmiDTO;
import clinix.com.clinix_sistema_usuarios.dto.PacienteRmiDTO;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UsuarioService extends Remote {
    MedicoRmiDTO getMedicoPorId(Long id) throws RemoteException;
    PacienteRmiDTO getPacientePorId(Long id) throws RemoteException;
}

