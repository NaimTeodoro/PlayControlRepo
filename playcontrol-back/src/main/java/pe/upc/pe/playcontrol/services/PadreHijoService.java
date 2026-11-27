package pe.upc.pe.playcontrol.services;

import pe.upc.pe.playcontrol.dtos.PadreHijoDTO;
import pe.upc.pe.playcontrol.dtos.VincularDTO;

import java.util.List;

public interface PadreHijoService {
    List<PadreHijoDTO> listar();
    PadreHijoDTO buscarPorId(Long id);
    PadreHijoDTO insertar(PadreHijoDTO dto);
    PadreHijoDTO actualizar(Long id, PadreHijoDTO dto);
    void eliminar(Long id);
    List<PadreHijoDTO> buscarPorPadre(Long idPadreTutor);
    List<PadreHijoDTO> buscarPorJoven(Long idJugadorJoven);

    /** Busca al joven por codigo y crea el vinculo PENDIENTE. 404 si el codigo no existe, 409 si ya estan vinculados. */
    PadreHijoDTO vincular(VincularDTO dto);

    /** El hijo autoriza el vinculo: estado ACTIVO, autorizado true. */
    PadreHijoDTO autorizar(Long id);
}
