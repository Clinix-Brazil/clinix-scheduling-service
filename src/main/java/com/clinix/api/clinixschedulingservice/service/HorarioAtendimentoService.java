package com.clinix.api.clinixschedulingservice.service;

import com.clinix.api.clinixschedulingservice.model.HorarioAtendimento;
import com.clinix.api.clinixschedulingservice.repository.HorarioAtendimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class HorarioAtendimentoService {

    private final HorarioAtendimentoRepository horarioAtendimentoRepository;

    @Autowired
    public HorarioAtendimentoService(HorarioAtendimentoRepository horarioAtendimentoRepository) {
        this.horarioAtendimentoRepository = horarioAtendimentoRepository;
    }

    public List<HorarioAtendimento> listarHorarios() {
        return horarioAtendimentoRepository.findAll();
    }

    public Optional<HorarioAtendimento> buscarPorId(Long horarioId) {
        return horarioAtendimentoRepository.findById(horarioId);
    }

    public HorarioAtendimento salvarHorario(HorarioAtendimento horario) {
        return horarioAtendimentoRepository.save(horario);
    }


//    public List<HorarioAtendimento> listarHorariosDisponiveis(Long medicoId) {
//        return horarioAtendimentoRepository.findByMedicoIdAndReservadoFalse(medicoId);
//    }
//
//    public List<HorarioAtendimento> listarHorariosIndisponiveis(Long medicoId) {
//        return horarioAtendimentoRepository.findByMedicoIdAndReservadoTrue(medicoId);
//    }

//    public List<HorarioAtendimento> listarTodosHorarios(Long medicoId) {
//        return horarioAtendimentoRepository.findByMedicoId(medicoId);
//    }

//    public HorarioAtendimento reservarHorario(Long horarioId, Long paciente) {
//        Optional<HorarioAtendimento> optionalHorario = horarioAtendimentoRepository.findById(horarioId);
//
//        if (optionalHorario.isPresent()) {
//            HorarioAtendimento horario = optionalHorario.get();
//            if (!horario.isReservado()) {
//                horario.setReservado(true);
//                horario.setPaciente(paciente);
//                return horarioAtendimentoRepository.save(horario);
//            }
//        }
//        throw new RuntimeException("Horário não disponível.");
//    }

    public void excluirHorario(Long horarioId) {
        if (horarioAtendimentoRepository.existsById(horarioId)) {
            horarioAtendimentoRepository.deleteById(horarioId);
        } else {
            throw new RuntimeException("Horário não encontrado.");
        }
    }


}
