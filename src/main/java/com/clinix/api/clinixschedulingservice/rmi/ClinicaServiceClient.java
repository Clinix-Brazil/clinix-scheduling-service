package com.clinix.api.clinixschedulingservice.rmi;

import com.clinix.api.dto.ClinicaRmiDTO;
import com.clinix.api.interfaces.ClinicaService;
import com.clinix.api.rmi.RmiClientHelper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ClinicaServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(ClinicaServiceClient.class);

    private final ClinicaService clinicaService;
    private final Cache<Long, ClinicaRmiDTO> clinicaCache;
    private final Timer rmiTimer;

    public ClinicaServiceClient(MeterRegistry meterRegistry) {
        this.rmiTimer = meterRegistry.timer("rmi_calls", "service", "ClinicaService");

        this.clinicaCache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(1000)
                .build();

        this.clinicaService = RmiClientHelper.connect("rmi://localhost:1099/ClinicaService", ClinicaService.class);
        if (this.clinicaService != null) {
            logger.info("Cliente RMI de Clínica conectado com sucesso!");
        } else {
            logger.error("Falha ao conectar ao serviço de Clínica RMI");
        }
    }

    public ClinicaRmiDTO getClinica(Long id) {
        ClinicaRmiDTO clinica = clinicaCache.getIfPresent(id);
        if (clinica != null) {
            logger.info("Clínica ID {} recuperada do cache.", id);
            return clinica;
        }

        logger.info("Clínica ID {} não encontrada no cache, realizando busca via RMI...", id);
        clinica = buscarClinicaComMonitoramento(id);

        if (clinica != null) {
            clinicaCache.put(id, clinica);
            logger.info("Clínica ID {} armazenada no cache.", id);
        }

        return clinica;
    }

    private ClinicaRmiDTO buscarClinicaComMonitoramento(Long id) {
        Timer.Sample sample = Timer.start();
        try {
            logger.info("Buscando clínica ID {} via RMI...", id);
            ClinicaRmiDTO clinica = clinicaService.getClinicaPorId(id);
            sample.stop(rmiTimer);
            return clinica;
        } catch (Exception e) {
            logger.error("Erro ao buscar clínica via RMI! ID: {}", id, e);
            return null;
        }
    }
}