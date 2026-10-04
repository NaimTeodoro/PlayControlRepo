package pe.upc.pe.playcontrol.servicesimplements;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.upc.pe.playcontrol.dtos.JuegoDTO;
import pe.upc.pe.playcontrol.entities.Juego;
import pe.upc.pe.playcontrol.entities.Plataforma;
import pe.upc.pe.playcontrol.repositories.JuegoRepository;
import pe.upc.pe.playcontrol.repositories.PlataformaRepository;
import pe.upc.pe.playcontrol.services.JuegoService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JuegoServiceImpl implements JuegoService {

    @Autowired
    private JuegoRepository juegoRepository;

    @Autowired
    private PlataformaRepository plataformaRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public JuegoDTO registrar(JuegoDTO dto) {
        Plataforma plataforma = plataformaRepository.findById(dto.getIdPlataforma())
                .orElseThrow(() -> new RuntimeException("Plataforma no encontrada"));
        Juego juego = modelMapper.map(dto, Juego.class);
        juego.setPlataforma(plataforma);
        juego = juegoRepository.save(juego);
        dto.setIdVideojuego(juego.getIdVideojuego());
        return dto;
    }

    @Override
    public List<JuegoDTO> listar() {
        return juegoRepository.findAll().stream()
                .map(j -> {
                    JuegoDTO dto = modelMapper.map(j, JuegoDTO.class);
                    dto.setIdPlataforma(j.getPlataforma().getIdPlataforma());
                    return dto;
                }).collect(Collectors.toList());
    }

    @Override
    public JuegoDTO obtenerPorId(Long id) {
        Juego j = juegoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Juego no encontrado"));
        JuegoDTO dto = modelMapper.map(j, JuegoDTO.class);
        dto.setIdPlataforma(j.getPlataforma().getIdPlataforma());
        return dto;
    }

    @Override
    public JuegoDTO actualizar(Long id, JuegoDTO dto) {
        Juego j = juegoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Juego no encontrado"));
        Plataforma plataforma = plataformaRepository.findById(dto.getIdPlataforma())
                .orElseThrow(() -> new RuntimeException("Plataforma no encontrada"));

        j.setNombreJuego(dto.getNombreJuego());
        j.setGenero(dto.getGenero());
        j.setDesarrollador(dto.getDesarrollador());
        j.setFechaLanzamiento(dto.getFechaLanzamiento());
        j.setClasificacionPEGI(dto.getClasificacionPEGI());
        j.setDescripcion(dto.getDescripcion());
        j.setTiempoPromedioSesion(dto.getTiempoPromedioSesion());
        j.setActivo(dto.getActivo());
        j.setPlataforma(plataforma);

        juegoRepository.save(j);
        dto.setIdVideojuego(id);
        return dto;
    }

    @Override
    public void eliminar(Long id) {
        juegoRepository.deleteById(id);
    }

    @Override
    public List<JuegoDTO> buscarPorGenero(String genero) {
        return juegoRepository.buscarPorGeneroNativo(genero).stream()
                .map(j -> modelMapper.map(j, JuegoDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<JuegoDTO> obtenerMasJugados() {
        return juegoRepository.obtenerJuegosMasJugadosNativo().stream()
                .map(j -> modelMapper.map(j, JuegoDTO.class))
                .collect(Collectors.toList());
    }
}