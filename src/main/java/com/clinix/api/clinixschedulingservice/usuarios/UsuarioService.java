package com.clinix.api.clinixschedulingservice.usuarios;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UsuarioService extends Remote {
    MedicoRmiDTO getMedicoPorId(Long id) throws RemoteException;
    PacienteRmiDTO getPacientePorId(Long id) throws RemoteException;
}

