package com.clinix.api.clinixschedulingservice.controller;

import com.clinix.api.clinixschedulingservice.model.HorarioAtendimento;
import com.clinix.api.clinixschedulingservice.service.HorarioAtendimentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/horario")
public class HorarioAtendimentoController {

    private final HorarioAtendimentoService horarioAtendimentoService;

    @Autowired
    public HorarioAtendimentoController(HorarioAtendimentoService horarioAtendimentoService) {
        this.horarioAtendimentoService = horarioAtendimentoService;
    }

    @GetMapping("/list")
    public List<HorarioAtendimento> listarHorarios() {
        return this.horarioAtendimentoService.listarHorarios();
    }

//    @GetMapping("/disponiveis/{medicoId}")
//    public List<HorarioAtendimento> listarHorariosDisponiveis(@PathVariable Long medicoId) {
//        return horarioAtendimentoService.listarHorariosDisponiveis(medicoId);
//    }

//    @GetMapping("/indisponiveis/{medicoId}")
//    public List<HorarioAtendimento> listarHorariosIndisponiveis(@PathVariable Long medicoId) {
//        return horarioAtendimentoService.listarHorariosIndisponiveis(medicoId);
//    }

//    @GetMapping("/listHorarios/{medicoId}")
//    public List<HorarioAtendimento> listarTodosHorarios(@PathVariable Long medicoId) {
//        return horarioAtendimentoService.listarTodosHorarios(medicoId);
//    }

//    @PostMapping("/reservar/{horarioId}/{pacienteId}")
//    public HorarioAtendimento reservarHorario(@PathVariable Long horarioId, @PathVariable Long pacienteId) {
//        PacienteRmiDTO paciente = pacienteService.buscarPorId(pacienteId);
//        return horarioAtendimentoService.reservarHorario(horarioId, paciente);
//    }

}
