package com.clinix.api.clinixschedulingservice.repository;

import com.clinix.api.clinixschedulingservice.model.HorarioAtendimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HorarioAtendimentoRepository extends JpaRepository<HorarioAtendimento, Long> {
//    List<HorarioAtendimento> findByMedicoIdAndReservadoFalse(Long medicoId);
//
//    List<HorarioAtendimento> findByMedicoIdAndReservadoTrue(Long medicoId);
//
//    List<HorarioAtendimento> findByMedicoId(Long medicoId);

}
