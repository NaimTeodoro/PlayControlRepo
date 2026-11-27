package pe.upc.pe.playcontrol.servicesimplements;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.upc.pe.playcontrol.dtos.VideojuegoDTO;
import pe.upc.pe.playcontrol.entities.Videojuego;
import pe.upc.pe.playcontrol.exception.ResourceNotFoundException;
import pe.upc.pe.playcontrol.repositories.PlataformaRepository;
import pe.upc.pe.playcontrol.repositories.VideojuegoRepository;
import pe.upc.pe.playcontrol.services.VideojuegoService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VideojuegoServiceImpl implements VideojuegoService {

    @Autowired
    private VideojuegoRepository videojuegoRepository;
    @Autowired
    private PlataformaRepository plataformaRepository;
    @Autowired
    private ModelMapper modelMapper;

    private VideojuegoDTO toDTO(Videojuego e) {
        VideojuegoDTO dto = modelMapper.map(e, VideojuegoDTO.class);
        dto.setIdPlataforma(e.getPlataforma().getIdPlataforma());
        return dto;
    }

    private Videojuego toEntity(VideojuegoDTO dto) {
        Videojuego e = modelMapper.map(dto, Videojuego.class);
        e.setPlataforma(plataformaRepository.findById(dto.getIdPlataforma())
                .orElseThrow(() -> new ResourceNotFoundException("Plataforma", dto.getIdPlataforma())));
        return e;
    }

    private Videojuego buscar(Long id) {
        return videojuegoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Videojuego", id));
    }

    @Override
    public List<VideojuegoDTO> listar() {
        return videojuegoRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public VideojuegoDTO buscarPorId(Long id) {
        return toDTO(buscar(id));
    }

    @Override
    public VideojuegoDTO insertar(VideojuegoDTO dto) {
        dto.setIdVideojuego(null);
        Videojuego e = toEntity(dto);
        if (e.getActivo() == null) {
            e.setActivo(true);
        }
        if (e.getFechaRegistro() == null) {
            e.setFechaRegistro(LocalDateTime.now());
        }
        return toDTO(videojuegoRepository.save(e));
    }

    @Override
    public VideojuegoDTO actualizar(Long id, VideojuegoDTO dto) {
        Videojuego existente = buscar(id);
        Videojuego e = toEntity(dto);
        e.setIdVideojuego(id);
        if (e.getActivo() == null) {
            e.setActivo(existente.getActivo());
        }
        if (e.getFechaRegistro() == null) {
            e.setFechaRegistro(existente.getFechaRegistro());
        }
        return toDTO(videojuegoRepository.save(e));
    }

    @Override
    public void eliminar(Long id) {
        if (!videojuegoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Videojuego", id);
        }
        videojuegoRepository.deleteById(id);
    }

    @Override
    public List<VideojuegoDTO> buscarPorGenero(String genero) {
        return videojuegoRepository.buscarPorGeneroNativo(genero).stream().map(this::toDTO).toList();
    }

    @Override
    public List<VideojuegoDTO> obtenerMasJugados() {
        return videojuegoRepository.obtenerJuegosMasJugadosNativo().stream().map(this::toDTO).toList();
    }
}
