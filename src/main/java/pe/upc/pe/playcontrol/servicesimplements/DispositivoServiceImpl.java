package pe.upc.pe.playcontrol.servicesimplements;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.upc.pe.playcontrol.dtos.DispositivoDTO;
import pe.upc.pe.playcontrol.entities.Dispositivo;
import pe.upc.pe.playcontrol.exception.ResourceNotFoundException;
import pe.upc.pe.playcontrol.repositories.DispositivoRepository;
import pe.upc.pe.playcontrol.repositories.UserRepository;
import pe.upc.pe.playcontrol.services.DispositivoService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DispositivoServiceImpl implements DispositivoService {

    @Autowired
    private DispositivoRepository dispositivoRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    private DispositivoDTO toDTO(Dispositivo e) {
        DispositivoDTO dto = modelMapper.map(e, DispositivoDTO.class);
        dto.setIdUsuario(e.getUsuario().getIdUsuario());
        return dto;
    }

    private Dispositivo toEntity(DispositivoDTO dto) {
        Dispositivo e = modelMapper.map(dto, Dispositivo.class);
        e.setUsuario(userRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", dto.getIdUsuario())));
        return e;
    }

    private Dispositivo buscar(Long id) {
        return dispositivoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Dispositivo", id));
    }

    @Override
    public List<DispositivoDTO> listar() {
        return dispositivoRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public DispositivoDTO buscarPorId(Long id) {
        return toDTO(buscar(id));
    }

    @Override
    public DispositivoDTO insertar(DispositivoDTO dto) {
        dto.setIdDispositivo(null);
        Dispositivo e = toEntity(dto);
        if (e.getFechaAsociacion() == null) {
            e.setFechaAsociacion(LocalDateTime.now());
        }
        if (e.getEstado() == null) {
            e.setEstado("ACTIVO");
        }
        return toDTO(dispositivoRepository.save(e));
    }

    @Override
    public DispositivoDTO actualizar(Long id, DispositivoDTO dto) {
        Dispositivo existente = buscar(id);
        Dispositivo e = toEntity(dto);
        e.setIdDispositivo(id);
        if (e.getFechaAsociacion() == null) {
            e.setFechaAsociacion(existente.getFechaAsociacion());
        }
        if (e.getEstado() == null) {
            e.setEstado(existente.getEstado());
        }
        return toDTO(dispositivoRepository.save(e));
    }

    @Override
    public void eliminar(Long id) {
        if (!dispositivoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Dispositivo", id);
        }
        dispositivoRepository.deleteById(id);
    }

    @Override
    public List<DispositivoDTO> listarPorUsuario(Long idUsuario) {
        return dispositivoRepository.listarPorUsuarioNativo(idUsuario).stream().map(this::toDTO).toList();
    }

    @Override
    public List<DispositivoDTO> buscarPorSistemaOperativo(String os) {
        return dispositivoRepository.buscarPorSistemaOperativoNativo(os).stream().map(this::toDTO).toList();
    }
}
