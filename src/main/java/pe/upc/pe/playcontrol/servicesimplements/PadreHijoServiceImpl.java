package pe.upc.pe.playcontrol.servicesimplements;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.upc.pe.playcontrol.dtos.PadreHijoDTO;
import pe.upc.pe.playcontrol.entities.JugadorJoven;
import pe.upc.pe.playcontrol.entities.PadreHijo;
import pe.upc.pe.playcontrol.entities.PadreTutor;
import pe.upc.pe.playcontrol.repositories.JugadorJovenRepository;
import pe.upc.pe.playcontrol.repositories.PadreHijoRepository;
import pe.upc.pe.playcontrol.repositories.PadreTutorRepository;
import pe.upc.pe.playcontrol.services.PadreHijoService;

@Service
public class PadreHijoServiceImpl implements PadreHijoService {
    @Autowired
    private PadreHijoRepository repository;
    @Autowired
    private PadreTutorRepository padreTutorRepository;
    @Autowired
    private JugadorJovenRepository jugadorJovenRepository;
    @Autowired
    private ModelMapper modelMapper;

    public PadreHijoServiceImpl() {
    }

    private PadreHijoDTO toDTO(PadreHijo e) {
        PadreHijoDTO dto = (PadreHijoDTO)this.modelMapper.map(e, PadreHijoDTO.class);
        dto.setIdPadreTutor(e.getPadreTutor().getIdPadreTutor());
        dto.setIdJugadorJoven(e.getJugadorJoven().getIdJugadorJoven());
        return dto;
    }

    private PadreHijo toEntity(PadreHijoDTO dto) {
        PadreHijo e = (PadreHijo)this.modelMapper.map(dto, PadreHijo.class);
        PadreTutor padre = (PadreTutor)this.padreTutorRepository.findById(dto.getIdPadreTutor()).orElseThrow(() -> new RuntimeException("PadreTutor no encontrado"));
        JugadorJoven joven = (JugadorJoven)this.jugadorJovenRepository.findById(dto.getIdJugadorJoven()).orElseThrow(() -> new RuntimeException("JugadorJoven no encontrado"));
        e.setPadreTutor(padre);
        e.setJugadorJoven(joven);
        return e;
    }

    public List<PadreHijoDTO> listar() {
        return (List)this.repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public PadreHijoDTO buscarPorId(Long id) {
        return this.toDTO((PadreHijo)this.repository.findById(id).orElseThrow(() -> new RuntimeException("Relacion no encontrada")));
    }

    public PadreHijoDTO insertar(PadreHijoDTO dto) {
        dto.setIdPadreHijo((Long)null);
        PadreHijo e = this.toEntity(dto);
        if (e.getFechaAsociacion() == null) {
            e.setFechaAsociacion(LocalDateTime.now());
        }

        return this.toDTO((PadreHijo)this.repository.save(e));
    }

    public PadreHijoDTO actualizar(Long id, PadreHijoDTO dto) {
        if (!this.repository.existsById(id)) {
            throw new RuntimeException("Relacion no encontrada");
        } else {
            PadreHijo e = this.toEntity(dto);
            e.setIdPadreHijo(id);
            return this.toDTO((PadreHijo)this.repository.save(e));
        }
    }

    public void eliminar(Long id) {
        if (!this.repository.existsById(id)) {
            throw new RuntimeException("Relacion no encontrada");
        } else {
            this.repository.deleteById(id);
        }
    }

    public List<PadreHijoDTO> buscarHijosPorPadre(Long idPadre) {
        return (List)this.repository.buscarHijosPorPadre(idPadre).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<PadreHijoDTO> buscarPorJovenYEstado(Long idJoven, String estado) {
        return (List)this.repository.buscarPorJovenYEstado(idJoven, estado).stream().map(this::toDTO).collect(Collectors.toList());
    }
}
