package com.clinix.api.clinixschedulingservice.rmi;

import com.clinix.api.dto.ClinicaRmiDTO;
import com.clinix.api.interfaces.ClinicaService;
import com.clinix.api.rmi.RmiClientHelper;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class ClinicaServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(ClinicaServiceClient.class);
    private final ClinicaService clinicaService;
    private final RedisTemplate<String, ClinicaRmiDTO> clinicaRedisTemplate;
    private final Timer rmiTimer;

    public ClinicaServiceClient(MeterRegistry meterRegistry, RedisTemplate<String, ClinicaRmiDTO> clinicaRedisTemplate) {
        this.rmiTimer = meterRegistry.timer("rmi_calls", "service", "ClinicaService");
        this.clinicaRedisTemplate = clinicaRedisTemplate;
        this.clinicaService = RmiClientHelper.connect("rmi://localhost:1099/ClinicaService", ClinicaService.class);

        if (this.clinicaService != null) {
            logger.info("Cliente RMI de Clínica conectado com sucesso!");
        } else {
            logger.error("Falha ao conectar ao serviço de Clínica RMI");
        }
    }

    public ClinicaRmiDTO getClinica(Long id) {
        String cacheKey = "clinica:" + id;
        ClinicaRmiDTO clinica = clinicaRedisTemplate.opsForValue().get(cacheKey);

        if (clinica != null) {
            logger.info("Clínica ID {} recuperada do cache Redis.", id);
            return clinica;
        }

        logger.info("Clínica ID {} não encontrada no cache, buscando via RMI...", id);
        clinica = buscarClinicaComMonitoramento(id);

        if (clinica != null) {
            clinicaRedisTemplate.opsForValue().set(cacheKey, clinica, 10, TimeUnit.MINUTES);
            logger.info("Clínica ID {} armazenada no cache Redis.", id);
        }

        return clinica;
    }

    private ClinicaRmiDTO buscarClinicaComMonitoramento(Long id) {
        Timer.Sample sample = Timer.start();
        try {
            ClinicaRmiDTO clinica = clinicaService.getClinicaPorId(id);
            sample.stop(rmiTimer);
            return clinica;
        } catch (Exception e) {
            logger.error("Erro ao buscar clínica via RMI! ID: {}", id, e);
            return null;
        }
    }
}