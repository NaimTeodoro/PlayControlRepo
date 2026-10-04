package pe.upc.pe.playcontrol.servicesimplements;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.upc.pe.playcontrol.dtos.RetoUsuarioDTO;
import pe.upc.pe.playcontrol.entities.Reto;
import pe.upc.pe.playcontrol.entities.RetoUsuario;
import pe.upc.pe.playcontrol.entities.User;
import pe.upc.pe.playcontrol.repositories.RetoUsuarioRepository;
import pe.upc.pe.playcontrol.services.RetoUsuarioService;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RetoUsuarioServiceImpl implements RetoUsuarioService {
    @Autowired
    private RetoUsuarioRepository retoUsuarioRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public RetoUsuarioDTO insert(RetoUsuarioDTO dto) {
        RetoUsuario retoUsuario = modelMapper.map(dto, RetoUsuario.class);

        User user = new User();
        user.setIdUsuario(dto.getIdUsuario());
        retoUsuario.setUser(user);

        Reto reto = new Reto();
        reto.setIdReto(dto.getIdReto());
        retoUsuario.setReto(reto);

        retoUsuario = retoUsuarioRepository.save(retoUsuario);

        RetoUsuarioDTO savedDto = modelMapper.map(retoUsuario, RetoUsuarioDTO.class);
        savedDto.setIdUsuario(retoUsuario.getUser().getIdUsuario());
        savedDto.setIdReto(retoUsuario.getReto().getIdReto());

        return savedDto;
    }

    @Override
    public List<RetoUsuarioDTO> list() {
        return retoUsuarioRepository.findAll().stream().map(ru -> {
            RetoUsuarioDTO dto = modelMapper.map(ru, RetoUsuarioDTO.class);
            dto.setIdUsuario(ru.getUser().getIdUsuario());
            dto.setIdReto(ru.getReto().getIdReto());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<RetoUsuarioDTO> listByUsuario(Long idUsuario) {
        return retoUsuarioRepository.findRetosByUsuarioNativo(idUsuario).stream().map(ru -> {
            RetoUsuarioDTO dto = modelMapper.map(ru, RetoUsuarioDTO.class);
            dto.setIdUsuario(ru.getUser().getIdUsuario());
            dto.setIdReto(ru.getReto().getIdReto());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public Integer countCompletados(Long idUsuario) {
        return retoUsuarioRepository.countRetosCompletadosByUsuarioNativo(idUsuario);
    }
}
