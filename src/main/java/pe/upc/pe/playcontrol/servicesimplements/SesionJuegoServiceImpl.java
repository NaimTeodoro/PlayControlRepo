package pe.upc.pe.playcontrol.servicesimplements;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.upc.pe.playcontrol.dtos.ResumenSesionesDTO;
import pe.upc.pe.playcontrol.dtos.SesionJuegoDTO;
import pe.upc.pe.playcontrol.entities.Dispositivo;
import pe.upc.pe.playcontrol.entities.LimiteJuego;
import pe.upc.pe.playcontrol.entities.SesionJuego;
import pe.upc.pe.playcontrol.exception.ConflictException;
import pe.upc.pe.playcontrol.exception.ResourceNotFoundException;
import pe.upc.pe.playcontrol.repositories.DispositivoRepository;
import pe.upc.pe.playcontrol.repositories.LimiteJuegoRepository;
import pe.upc.pe.playcontrol.repositories.SesionJuegoRepository;
import pe.upc.pe.playcontrol.repositories.UserRepository;
import pe.upc.pe.playcontrol.repositories.VideojuegoRepository;
import pe.upc.pe.playcontrol.services.SesionJuegoService;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class SesionJuegoServiceImpl implements SesionJuegoService {

    public static final String EN_CURSO = "EN_CURSO";
    public static final String FINALIZADA = "FINALIZADA";

    @Autowired
    private SesionJuegoRepository sesionJuegoRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VideojuegoRepository videojuegoRepository;
    @Autowired
    private DispositivoRepository dispositivoRepository;
    @Autowired
    private LimiteJuegoRepository limiteJuegoRepository;
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Minutos jugados en una sesion. Una sesion EN_CURSO (sin fechaFin) cuenta los minutos transcurridos hasta "corte".
     */
    public static int minutosJugados(SesionJuego s, LocalDateTime corte) {
        if (EN_CURSO.equals(s.getEstadoSesion()) || s.getFechaFin() == null) {
            if (s.getDuracionMinutos() != null && s.getFechaFin() != null) {
                return s.getDuracionMinutos();
            }
            return (int) Math.max(0, Duration.between(s.getFechaInicio(), corte).toMinutes());
        }
        if (s.getDuracionMinutos() != null) {
            return s.getDuracionMinutos();
        }
        return (int) Math.max(0, Duration.between(s.getFechaInicio(), s.getFechaFin()).toMinutes());
    }

    private SesionJuegoDTO toDTO(SesionJuego e) {
        SesionJuegoDTO dto = modelMapper.map(e, SesionJuegoDTO.class);
        dto.setIdUsuario(e.getUsuario().getIdUsuario());
        dto.setIdVideojuego(e.getVideojuego().getIdVideojuego());
        dto.setNombreJuego(e.getVideojuego().getNombreJuego());
        dto.setIdDispositivo(e.getDispositivo() != null ? e.getDispositivo().getIdDispositivo() : null);
        return dto;
    }

    private SesionJuego toEntity(SesionJuegoDTO dto) {
        SesionJuego e = modelMapper.map(dto, SesionJuego.class);
        e.setUsuario(userRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", dto.getIdUsuario())));
        e.setVideojuego(videojuegoRepository.findById(dto.getIdVideojuego())
                .orElseThrow(() -> new ResourceNotFoundException("Videojuego", dto.getIdVideojuego())));
        if (dto.getIdDispositivo() != null) {
            Dispositivo d = dispositivoRepository.findById(dto.getIdDispositivo())
                    .orElseThrow(() -> new ResourceNotFoundException("Dispositivo", dto.getIdDispositivo()));
            if (!d.getUsuario().getIdUsuario().equals(dto.getIdUsuario())) {
                throw new IllegalArgumentException("El dispositivo no pertenece al usuario de la sesión");
            }
            e.setDispositivo(d);
        } else {
            e.setDispositivo(null);
        }
        return e;
    }

    /** Completa estado, fechas, duracion y respetoLimites de forma coherente. */
    private void normalizar(SesionJuego e, Long idSesionActual) {
        if (e.getFechaFin() != null && e.getFechaFin().isBefore(e.getFechaInicio())) {
            throw new IllegalArgumentException("fechaFin no puede ser anterior a fechaInicio");
        }
        if (e.getEstadoSesion() == null) {
            e.setEstadoSesion(e.getFechaFin() == null && e.getDuracionMinutos() == null ? EN_CURSO : FINALIZADA);
        }
        if (EN_CURSO.equals(e.getEstadoSesion())) {
            boolean otraEnCurso = sesionJuegoRepository
                    .findByUsuario_IdUsuarioAndEstadoSesion(e.getUsuario().getIdUsuario(), EN_CURSO).stream()
                    .anyMatch(s -> !s.getIdSesion().equals(idSesionActual));
            if (otraEnCurso) {
                throw new ConflictException("El usuario ya tiene una sesión EN_CURSO");
            }
            e.setFechaFin(null);
            e.setDuracionMinutos(null);
        } else {
            if (e.getDuracionMinutos() == null && e.getFechaFin() != null) {
                e.setDuracionMinutos((int) Duration.between(e.getFechaInicio(), e.getFechaFin()).toMinutes());
            } else if (e.getFechaFin() == null && e.getDuracionMinutos() != null) {
                e.setFechaFin(e.getFechaInicio().plusMinutes(e.getDuracionMinutos()));
            } else if (e.getFechaFin() == null) {
                throw new IllegalArgumentException("Una sesión " + e.getEstadoSesion() + " necesita fechaFin o duracionMinutos");
            }
        }
        if (e.getPausasRealizadas() == null) {
            e.setPausasRealizadas(0);
        }
        if (e.getNumeroInterrupciones() == null) {
            e.setNumeroInterrupciones(0);
        }
        if (e.getRespetoLimites() == null) {
            e.setRespetoLimites(calcularRespeto(e, idSesionActual));
        }
    }

    /** true si los minutos del dia (incluida esta sesion) no superan el limite diario ACTIVO. Sin limite: true. */
    private boolean calcularRespeto(SesionJuego e, Long idSesionActual) {
        Integer limite = limiteDiarioMinutos(e.getUsuario().getIdUsuario());
        if (limite == null) {
            return true;
        }
        LocalDateTime ahora = LocalDateTime.now();
        LocalDate dia = e.getFechaInicio().toLocalDate();
        int minutosDia = sesionJuegoRepository
                .buscarPorUsuarioEntre(e.getUsuario().getIdUsuario(), dia.atStartOfDay(), dia.plusDays(1).atStartOfDay())
                .stream()
                .filter(s -> !Objects.equals(s.getIdSesion(), idSesionActual))
                .mapToInt(s -> minutosJugados(s, ahora))
                .sum();
        return minutosDia + minutosJugados(e, ahora) <= limite;
    }

    private Integer limiteDiarioMinutos(Long idUsuario) {
        return limiteJuegoRepository.buscarActivosPorUsuario(idUsuario).stream()
                .map(LimiteJuego::getLimiteHorasDiarias)
                .filter(Objects::nonNull)
                .findFirst()
                .map(h -> h.multiply(BigDecimal.valueOf(60)).intValue())
                .orElse(null);
    }

    private SesionJuego buscar(Long id) {
        return sesionJuegoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("SesionJuego", id));
    }

    @Override
    public List<SesionJuegoDTO> listar() {
        return sesionJuegoRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public SesionJuegoDTO buscarPorId(Long id) {
        return toDTO(buscar(id));
    }

    @Override
    public SesionJuegoDTO insertar(SesionJuegoDTO dto) {
        dto.setIdSesion(null);
        SesionJuego e = toEntity(dto);
        normalizar(e, null);
        return toDTO(sesionJuegoRepository.save(e));
    }

    @Override
    public SesionJuegoDTO actualizar(Long id, SesionJuegoDTO dto) {
        buscar(id);
        SesionJuego e = toEntity(dto);
        e.setIdSesion(id);
        normalizar(e, id);
        return toDTO(sesionJuegoRepository.save(e));
    }

    @Override
    public void eliminar(Long id) {
        if (!sesionJuegoRepository.existsById(id)) {
            throw new ResourceNotFoundException("SesionJuego", id);
        }
        sesionJuegoRepository.deleteById(id);
    }

    @Override
    public List<SesionJuegoDTO> listarPorUsuario(Long idUsuario) {
        return sesionJuegoRepository.listarPorUsuarioNativo(idUsuario).stream().map(this::toDTO).toList();
    }

    @Override
    public ResumenSesionesDTO resumen(Long idUsuario) {
        if (!userRepository.existsById(idUsuario)) {
            throw new ResourceNotFoundException("Usuario", idUsuario);
        }
        LocalDateTime ahora = LocalDateTime.now();
        LocalDate hoy = ahora.toLocalDate();
        LocalDateTime inicioHoy = hoy.atStartOfDay();
        LocalDateTime inicioSemana = hoy.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
        LocalDateTime inicioMes = hoy.withDayOfMonth(1).atStartOfDay();
        LocalDateTime desde = inicioSemana.isBefore(inicioMes) ? inicioSemana : inicioMes;

        int minutosHoy = 0;
        int minutosSemana = 0;
        int minutosMes = 0;
        int sesionesHoy = 0;
        for (SesionJuego s : sesionJuegoRepository.buscarPorUsuarioEntre(idUsuario, desde, hoy.plusDays(1).atStartOfDay())) {
            int m = minutosJugados(s, ahora);
            if (!s.getFechaInicio().isBefore(inicioHoy)) {
                minutosHoy += m;
                sesionesHoy++;
            }
            if (!s.getFechaInicio().isBefore(inicioSemana)) {
                minutosSemana += m;
            }
            if (!s.getFechaInicio().isBefore(inicioMes)) {
                minutosMes += m;
            }
        }

        Integer limite = limiteDiarioMinutos(idUsuario);
        Integer porcentaje = (limite == null || limite == 0) ? null : Math.round(minutosHoy * 100f / limite);
        SesionJuegoDTO enCurso = sesionJuegoRepository.findByUsuario_IdUsuarioAndEstadoSesion(idUsuario, EN_CURSO).stream()
                .max(Comparator.comparing(SesionJuego::getFechaInicio))
                .map(this::toDTO)
                .orElse(null);

        return new ResumenSesionesDTO(minutosHoy, minutosSemana, minutosMes, sesionesHoy, limite, porcentaje, enCurso);
    }
}
