package pe.upc.pe.playcontrol.servicesimplements;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.upc.pe.playcontrol.dtos.RecompensaDTO;
import pe.upc.pe.playcontrol.entities.Recompensa;
import pe.upc.pe.playcontrol.repositories.RecompensaRepository;
import pe.upc.pe.playcontrol.services.RecompensaService;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecompensaServiceImpl implements RecompensaService {
    @Autowired
    private RecompensaRepository recompensaRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public RecompensaDTO insert(RecompensaDTO recompensaDTO) {
        Recompensa recompensa = modelMapper.map(recompensaDTO, Recompensa.class);
        recompensa = recompensaRepository.save(recompensa);
        return modelMapper.map(recompensa, RecompensaDTO.class);
    }

    @Override
    public List<RecompensaDTO> list() {
        return recompensaRepository.findAll().stream()
                .map(rec -> modelMapper.map(rec, RecompensaDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public RecompensaDTO listId(Long id) {
        Recompensa recompensa = recompensaRepository.findById(id).orElse(new Recompensa());
        return modelMapper.map(recompensa, RecompensaDTO.class);
    }

    @Override
    public void delete(Long id) {
        recompensaRepository.deleteById(id);
    }
}