package com.clinix.api.clinixschedulingservice;

import com.clinix.api.clinixschedulingservice.model.HorarioAtendimento;
import com.clinix.api.clinixschedulingservice.repository.HorarioAtendimentoRepository;
import com.clinix.api.clinixschedulingservice.service.HorarioAtendimentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class HorarioAtendimentoServiceTest {

    @Mock
    private HorarioAtendimentoRepository horarioAtendimentoRepository;

    @InjectMocks
    private HorarioAtendimentoService horarioAtendimentoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListarHorarios() {
        HorarioAtendimento horario1 = new HorarioAtendimento();
        HorarioAtendimento horario2 = new HorarioAtendimento();

        List<HorarioAtendimento> listaHorarios = Arrays.asList(horario1, horario2);

        when(horarioAtendimentoRepository.findAll()).thenReturn(listaHorarios);

        List<HorarioAtendimento> resultado = horarioAtendimentoService.listarHorarios();

        assertEquals(2, resultado.size());
        assertEquals(horario1.getId(), resultado.get(0).getId());
        assertEquals(horario2.getId(), resultado.get(1).getId());
    }
}
