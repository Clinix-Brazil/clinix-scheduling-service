package com.clinix.api.clinixschedulingservice.usuarios;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicoRmiDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    private String nome;
}
