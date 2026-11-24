package pe.upc.pe.playcontrol.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Body de POST /api/padre-hijo/vincular. El padre ingresa el codigo de vinculacion del hijo (PC-XXXXXX).
 */
@Data
public class VincularDTO {
    @NotNull
    private Long idPadreTutor;
    @NotBlank @Size(max = 20)
    private String codigoVinculacion;
    @Pattern(regexp = "PADRE|MADRE|TUTOR")
    private String tipoRelacion;
}
