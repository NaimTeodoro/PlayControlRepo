package pe.upc.pe.playcontrol.servicesimplements;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.upc.pe.playcontrol.dtos.RetoDTO;
import pe.upc.pe.playcontrol.entities.Reto;
import pe.upc.pe.playcontrol.repositories.RetoRepository;
import pe.upc.pe.playcontrol.services.RetoService;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RetoServiceImpl implements RetoService {
    @Autowired
    private RetoRepository retoRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public RetoDTO insert(RetoDTO retoDTO) {
        Reto reto = modelMapper.map(retoDTO, Reto.class);
        reto = retoRepository.save(reto);
        return modelMapper.map(reto, RetoDTO.class);
    }

    @Override
    public List<RetoDTO> list() {
        return retoRepository.findAll().stream()
                .map(reto -> modelMapper.map(reto, RetoDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public RetoDTO listId(Long id) {
        Reto reto = retoRepository.findById(id).orElse(new Reto());
        return modelMapper.map(reto, RetoDTO.class);
    }

    @Override
    public void delete(Long id) {
        retoRepository.deleteById(id);
    }
}