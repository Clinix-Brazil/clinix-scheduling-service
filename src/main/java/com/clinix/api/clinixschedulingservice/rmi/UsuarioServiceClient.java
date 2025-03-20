package com.clinix.api.clinixschedulingservice.rmi;

import com.clinix.api.dto.MedicoRmiDTO;
import com.clinix.api.dto.PacienteRmiDTO;
import com.clinix.api.interfaces.UsuarioService;
import com.clinix.api.rmi.RmiClientHelper;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class UsuarioServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioServiceClient.class);
    private final UsuarioService usuarioService;
    private final RedisTemplate<String, MedicoRmiDTO> medicoRedisTemplate;
    private final RedisTemplate<String, PacienteRmiDTO> pacienteRedisTemplate;
    private final Timer rmiTimer;

    public UsuarioServiceClient(MeterRegistry meterRegistry, RedisTemplate<String, MedicoRmiDTO> medicoRedisTemplate,
                                RedisTemplate<String, PacienteRmiDTO> pacienteRedisTemplate) {
        this.rmiTimer = meterRegistry.timer("rmi_calls", "service", "UsuarioService");
        this.medicoRedisTemplate = medicoRedisTemplate;
        this.pacienteRedisTemplate = pacienteRedisTemplate;
        this.usuarioService = RmiClientHelper.connect("rmi://localhost:1099/UsuarioService", UsuarioService.class);

        if (this.usuarioService != null) {
            logger.info("Cliente RMI de Usuário conectado com sucesso!");
        } else {
            logger.error("Falha ao conectar ao serviço de Usuário RMI");
        }
    }

    public MedicoRmiDTO getMedico(Long id) {
        String cacheKey = "medico:" + id;
        MedicoRmiDTO medico = medicoRedisTemplate.opsForValue().get(cacheKey);

        if (medico != null) {
            logger.info("Médico ID {} recuperado do cache Redis.", id);
            return medico;
        }

        logger.info("Médico ID {} não encontrado no cache, buscando via RMI...", id);
        medico = buscarMedicoComMonitoramento(id);

        if (medico != null) {
            medicoRedisTemplate.opsForValue().set(cacheKey, medico, 10, TimeUnit.MINUTES);
            logger.info("Médico ID {} armazenado no cache Redis.", id);
        }

        return medico;
    }

    private MedicoRmiDTO buscarMedicoComMonitoramento(Long id) {
        Timer.Sample sample = Timer.start();
        try {
            MedicoRmiDTO medico = usuarioService.getMedicoPorId(id);
            sample.stop(rmiTimer);
            return medico;
        } catch (Exception e) {
            logger.error("Erro ao buscar médico via RMI! ID: {}", id, e);
            return null;
        }
    }

    public PacienteRmiDTO getPaciente(Long id) {
        String cacheKey = "paciente:" + id;
        PacienteRmiDTO paciente = pacienteRedisTemplate.opsForValue().get(cacheKey);

        if (paciente != null) {
            logger.info("Paciente ID {} recuperado do cache Redis.", id);
            return paciente;
        }

        logger.info("Paciente ID {} não encontrado no cache, buscando via RMI...", id);
        paciente = buscarPacienteComMonitoramento(id);

        if (paciente != null) {
            pacienteRedisTemplate.opsForValue().set(cacheKey, paciente, 10, TimeUnit.MINUTES);
            logger.info("Paciente ID {} armazenado no cache Redis.", id);
        }

        return paciente;
    }

    private PacienteRmiDTO buscarPacienteComMonitoramento(Long id) {
        Timer.Sample sample = Timer.start();
        try {
            PacienteRmiDTO paciente = usuarioService.getPacientePorId(id);
            sample.stop(rmiTimer);
            return paciente;
        } catch (Exception e) {
            logger.error("Erro ao buscar paciente via RMI! ID: {}", id, e);
            return null;
        }
    }
}