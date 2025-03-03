package com.clinix.api.clinixschedulingservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserClientService {
    private final RestTemplate restTemplate;

    public UserClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void doctorExists(Long doctorId) {
        String url = "http://localhost:8080/clinixSistemaUsuarios/medico/" + doctorId;
        System.out.println(url);
    }

    public void patientExists(Long patientId) {
        String url = "http://localhost:8080/clinixSistemaUsuarios/paciente/" + patientId;
        System.out.println(url);
    }
}

