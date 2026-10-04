package pe.upc.pe.playcontrol.servicesimplements;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.upc.pe.playcontrol.dtos.DispositivoDTO;
import pe.upc.pe.playcontrol.entities.Dispositivo;
import pe.upc.pe.playcontrol.entities.User;
import pe.upc.pe.playcontrol.repositories.DispositivoRepository;
import pe.upc.pe.playcontrol.repositories.UserRepository;
import pe.upc.pe.playcontrol.services.DispositivoService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DispositivoServiceImpl implements DispositivoService {

    @Autowired
    private DispositivoRepository dispositivoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public DispositivoDTO registrar(DispositivoDTO dto) {
        User usuario = userRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Dispositivo dispositivo = modelMapper.map(dto, Dispositivo.class);
        dispositivo.setUsuario(usuario);
        dispositivo = dispositivoRepository.save(dispositivo);

        dto.setIdDispositivo(dispositivo.getIdDispositivo());
        return dto;
    }

    @Override
    public List<DispositivoDTO> listar() {
        return dispositivoRepository.findAll().stream().map(d -> {
            DispositivoDTO dto = modelMapper.map(d, DispositivoDTO.class);
            dto.setIdUsuario(d.getUsuario().getIdUsuario());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public DispositivoDTO obtenerPorId(Long id) {
        Dispositivo d = dispositivoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dispositivo no encontrado"));
        DispositivoDTO dto = modelMapper.map(d, DispositivoDTO.class);
        dto.setIdUsuario(d.getUsuario().getIdUsuario());
        return dto;
    }

    @Override
    public DispositivoDTO actualizar(Long id, DispositivoDTO dto) {
        Dispositivo d = dispositivoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dispositivo no encontrado"));
        User usuario = userRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        d.setTipoDispositivo(dto.getTipoDispositivo());
        d.setModelo(dto.getModelo());
        d.setSistemaOperativo(dto.getSistemaOperativo());
        d.setIdentificadorDisp(dto.getIdentificadorDisp());
        d.setFechaAsociacion(dto.getFechaAsociacion());
        d.setEstado(dto.getEstado());
        d.setUltimaActividad(dto.getUltimaActividad());
        d.setVersionApp(dto.getVersionApp());
        d.setUsuario(usuario);

        dispositivoRepository.save(d);
        dto.setIdDispositivo(id);
        return dto;
    }

    @Override
    public void eliminar(Long id) {
        dispositivoRepository.deleteById(id);
    }

    @Override
    public List<DispositivoDTO> listarPorUsuario(Long idUsuario) {
        return dispositivoRepository.listarPorUsuarioNativo(idUsuario).stream()
                .map(d -> modelMapper.map(d, DispositivoDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<DispositivoDTO> buscarPorSistemaOperativo(String os) {
        return dispositivoRepository.buscarPorSistemaOperativoNativo(os).stream()
                .map(d -> modelMapper.map(d, DispositivoDTO.class))
                .collect(Collectors.toList());
    }
}