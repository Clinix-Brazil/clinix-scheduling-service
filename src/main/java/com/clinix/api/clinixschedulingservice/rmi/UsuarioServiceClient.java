package com.clinix.api.clinixschedulingservice.rmi;

import com.clinix.api.dto.MedicoRmiDTO;
import com.clinix.api.dto.PacienteRmiDTO;
import com.clinix.api.interfaces.UsuarioService;
import com.clinix.api.rmi.RmiClientHelper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

import java.util.concurrent.TimeUnit;

@Service
public class UsuarioServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioServiceClient.class);

    private final UsuarioService usuarioService;
    private final Cache<Long, MedicoRmiDTO> medicoCache;
    private final Cache<Long, PacienteRmiDTO> pacienteCache;
    private final Timer rmiTimer;

    public UsuarioServiceClient(MeterRegistry meterRegistry) {
        this.rmiTimer = meterRegistry.timer("rmi_calls", "service", "UsuarioService");

        this.medicoCache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build();

        this.pacienteCache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build();

        this.usuarioService = RmiClientHelper.connect("rmi://localhost:1099/UsuarioService", UsuarioService.class);
        if (this.usuarioService != null) {
            logger.info("Cliente RMI de Usuário conectado com sucesso!");
        } else {
            logger.error("Falha ao conectar ao serviço de Usuário RMI");
        }
    }

    public MedicoRmiDTO getMedico(Long id) {
        MedicoRmiDTO medico = medicoCache.getIfPresent(id);
        if (medico != null) {
            logger.info("Médico ID {} recuperado do cache.", id);
            return medico;
        }

        logger.info("Médico ID {} não encontrado no cache, realizando busca via RMI...", id);
        medico = buscarMedicoComMonitoramento(id);

        if (medico != null) {
            medicoCache.put(id, medico);
            logger.info("Médico ID {} armazenado no cache.", id);
        }

        return medico;
    }

    private MedicoRmiDTO buscarMedicoComMonitoramento(Long id) {
        Timer.Sample sample = Timer.start();
        try {
            logger.info("Buscando médico ID {} via RMI...", id);
            MedicoRmiDTO medico = usuarioService.getMedicoPorId(id);
            sample.stop(rmiTimer);
            return medico;
        } catch (Exception e) {
            logger.error("Erro ao buscar médico via RMI! ID: {}", id, e);
            return null;
        }
    }

    public PacienteRmiDTO getPaciente(Long id) {
        PacienteRmiDTO paciente = pacienteCache.getIfPresent(id);
        if (paciente != null) {
            logger.info("Paciente ID {} recuperado do cache.", id);
            return paciente;
        }

        logger.info("Paciente ID {} não encontrado no cache, realizando busca via RMI...", id);
        paciente = buscarPacienteComMonitoramento(id);

        if (paciente != null) {
            pacienteCache.put(id, paciente);
            logger.info("Paciente ID {} armazenado no cache.", id);
        }

        return paciente;
    }

    private PacienteRmiDTO buscarPacienteComMonitoramento(Long id) {
        Timer.Sample sample = Timer.start();
        try {
            logger.info("Buscando paciente ID {} via RMI...", id);
            PacienteRmiDTO paciente = usuarioService.getPacientePorId(id);
            sample.stop(rmiTimer);
            return paciente;
        } catch (Exception e) {
            logger.error("Erro ao buscar paciente via RMI! ID: {}", id, e);
            return null;
        }
    }
}