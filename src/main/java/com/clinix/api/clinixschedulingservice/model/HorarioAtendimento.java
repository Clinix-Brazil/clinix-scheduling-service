package com.clinix.api.clinixschedulingservice.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "tb_horario_atendimento")
public class HorarioAtendimento {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long medico;

    @Getter
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime horario;

    private boolean reservado;

    private Long paciente;

    public HorarioAtendimento() {
    }
}