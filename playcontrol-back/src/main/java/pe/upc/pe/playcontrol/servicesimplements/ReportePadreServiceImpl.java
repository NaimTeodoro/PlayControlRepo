package pe.upc.pe.playcontrol.servicesimplements;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.upc.pe.playcontrol.dtos.ReportePadreDTO;
import pe.upc.pe.playcontrol.entities.AlertaJuego;
import pe.upc.pe.playcontrol.entities.PadreHijo;
import pe.upc.pe.playcontrol.entities.Recompensa;
import pe.upc.pe.playcontrol.entities.ReportePadre;
import pe.upc.pe.playcontrol.entities.SesionJuego;
import pe.upc.pe.playcontrol.entities.User;
import pe.upc.pe.playcontrol.exception.ConflictException;
import pe.upc.pe.playcontrol.exception.ResourceNotFoundException;
import pe.upc.pe.playcontrol.repositories.AlertaJuegoRepository;
import pe.upc.pe.playcontrol.repositories.PadreHijoRepository;
import pe.upc.pe.playcontrol.repositories.RecompensaRepository;
import pe.upc.pe.playcontrol.repositories.ReportePadreRepository;
import pe.upc.pe.playcontrol.repositories.SesionJuegoRepository;
import pe.upc.pe.playcontrol.services.ReportePadreService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@Service
public class ReportePadreServiceImpl implements ReportePadreService {

    private static final BigDecimal CIEN = BigDecimal.valueOf(100);

    @Autowired
    private ReportePadreRepository repository;
    @Autowired
    private PadreHijoRepository padreHijoRepository;
    @Autowired
    private SesionJuegoRepository sesionJuegoRepository;
    @Autowired
    private AlertaJuegoRepository alertaJuegoRepository;
    @Autowired
    private RecompensaRepository recompensaRepository;
    @Autowired
    private ModelMapper modelMapper;

    private ReportePadreDTO toDTO(ReportePadre e) {
        ReportePadreDTO dto = modelMapper.map(e, ReportePadreDTO.class);
        PadreHijo ph = e.getPadreHijo();
        User hijo = ph.getJugadorJoven().getUsuario();
        dto.setIdPadreHijo(ph.getIdPadreHijo());
        dto.setIdPadreTutor(ph.getPadreTutor().getIdPadreTutor());
        dto.setIdJugadorJoven(ph.getJugadorJoven().getIdJugadorJoven());
        dto.setNombreHijo(((hijo.getNombre() == null ? "" : hijo.getNombre()) + " "
                + (hijo.getApellido() == null ? "" : hijo.getApellido())).trim());
        return dto;
    }

    private ReportePadre toEntity(ReportePadreDTO dto) {
        ReportePadre e = modelMapper.map(dto, ReportePadre.class);
        e.setPadreHijo(buscarPadreHijo(dto.getIdPadreHijo()));
        return e;
    }

    private PadreHijo buscarPadreHijo(Long id) {
        return padreHijoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("PadreHijo", id));
    }

    private ReportePadre buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ReportePadre", id));
    }

    // ------------------------------------------------------------------ CRUD

    @Override
    public List<ReportePadreDTO> listar() {
        return repository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public ReportePadreDTO buscarPorId(Long id) {
        return toDTO(buscar(id));
    }

    @Override
    public ReportePadreDTO insertar(ReportePadreDTO dto) {
        dto.setIdReporte(null);
        ReportePadre e = toEntity(dto);
        if (e.getFechaGeneracion() == null) {
            e.setFechaGeneracion(LocalDateTime.now());
        }
        if (e.getEnviado() == null) {
            e.setEnviado(false);
        }
        if (e.getVisto() == null) {
            e.setVisto(false);
        }
        return toDTO(repository.save(e));
    }

    @Override
    public ReportePadreDTO actualizar(Long id, ReportePadreDTO dto) {
        ReportePadre existente = buscar(id);
        ReportePadre e = toEntity(dto);
        e.setIdReporte(id);
        if (e.getFechaGeneracion() == null) {
            e.setFechaGeneracion(existente.getFechaGeneracion());
        }
        // Al marcar como visto se guarda la fecha
        if (Boolean.TRUE.equals(e.getVisto()) && e.getFechaVisto() == null) {
            e.setFechaVisto(existente.getFechaVisto() != null ? existente.getFechaVisto() : LocalDateTime.now());
        }
        return toDTO(repository.save(e));
    }

    @Override
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("ReportePadre", id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<ReportePadreDTO> buscarPorPadre(Long idPadreTutor) {
        return repository.findByPadreHijo_PadreTutor_IdPadreTutorOrderByFechaGeneracionDesc(idPadreTutor)
                .stream().map(this::toDTO).toList();
    }

    @Override
    public List<ReportePadreDTO> buscarPorPadreHijo(Long idPadreHijo) {
        return repository.findByPadreHijo_IdPadreHijoOrderByFechaInicioPeriodoDesc(idPadreHijo)
                .stream().map(this::toDTO).toList();
    }

    // ------------------------------------------------------------------ generacion

    @Override
    @Transactional
    public ReportePadreDTO generar(Long idPadreHijo, String periodo) {
        String per = normalizarPeriodo(periodo);
        LocalDate fin = LocalDate.now();
        LocalDate inicio = fin.minusDays("MENSUAL".equals(per) ? 29 : 6);
        return generarPeriodo(idPadreHijo, per, inicio, fin);
    }

    @Override
    @Transactional
    public ReportePadreDTO generarPeriodo(Long idPadreHijo, String periodo, LocalDate inicio, LocalDate fin) {
        String per = normalizarPeriodo(periodo);
        if (fin.isBefore(inicio)) {
            throw new IllegalArgumentException("La fecha fin del periodo no puede ser anterior a la de inicio");
        }
        PadreHijo ph = buscarPadreHijo(idPadreHijo);
        if (!"ACTIVO".equals(ph.getEstado())) {
            throw new ConflictException("Solo se generan reportes de vínculos ACTIVOS (estado actual: " + ph.getEstado() + ")");
        }
        Long idUsuario = ph.getJugadorJoven().getUsuario().getIdUsuario();
        LocalDateTime ahora = LocalDateTime.now();
        int diasPeriodo = (int) ChronoUnit.DAYS.between(inicio, fin) + 1;

        // --- Sesiones
        Metricas actual = metricas(idUsuario, inicio, fin, ahora);
        LocalDate inicioAnterior = inicio.minusDays(diasPeriodo);
        Metricas anterior = metricas(idUsuario, inicioAnterior, inicio.minusDays(1), ahora);

        // --- Alertas
        List<AlertaJuego> alertas = alertaJuegoRepository.buscarPorUsuarioEntre(
                idUsuario, inicio.atStartOfDay(), fin.plusDays(1).atStartOfDay());
        int respondidas = (int) alertas.stream().filter(a -> Boolean.TRUE.equals(a.getRespondidoUsuario())).count();
        Map<String, Integer> importantes = new TreeMap<>();
        for (AlertaJuego a : alertas) {
            if ("ALTA".equals(a.getIntensidad())) {
                importantes.merge(a.getTipoAlerta(), 1, Integer::sum);
            }
        }

        // --- Logros obtenidos en el periodo
        List<Recompensa> obtenidas = recompensaRepository.buscarObtenidasEntre(
                idUsuario, inicio.atStartOfDay(), fin.plusDays(1).atStartOfDay());
        int puntos = obtenidas.stream()
                .mapToInt(r -> r.getLogro().getPuntosAsignados() != null ? r.getLogro().getPuntosAsignados() : 0)
                .sum();

        ReportePadre r = new ReportePadre();
        r.setPadreHijo(ph);
        r.setFechaGeneracion(ahora);
        r.setPeriodoReporte(per);
        r.setFechaInicioPeriodo(inicio);
        r.setFechaFinPeriodo(fin);
        r.setTiempoTotalJuegoMinutos(actual.minutosTotales);
        r.setSesionesJuegoTotal(actual.sesiones);
        r.setTiempoPromedioSesionMinutos(actual.sesiones == 0 ? 0 : Math.round((float) actual.minutosTotales / actual.sesiones));
        r.setVideojuegosJugados(actual.videojuegos.size());
        r.setDiasActivos(actual.diasConJuego.size());
        r.setDiasSinJuego(diasPeriodo - actual.diasConJuego.size());
        r.setSesionesRespetuosas(actual.respetuosas);
        r.setRespetoLimitesPorc(actual.porcentajeRespeto());
        r.setAlertasRecibidas(alertas.size());
        r.setAlertasRespondidas(respondidas);
        r.setLogrosAlcanzados(obtenidas.size());
        r.setPuntosObtenidos(puntos);
        r.setRachaDias(racha(inicio, fin, actual.diasExcedidos));
        r.setAlertasImportantes(importantes.isEmpty()
                ? "Sin alertas de intensidad ALTA en el periodo"
                : "Alertas de intensidad ALTA: " + String.join(", ",
                importantes.entrySet().stream().map(en -> en.getKey() + " x" + en.getValue()).toList()));
        r.setTendencia(tendencia(actual, anterior));
        r.setRecomendacionesSistema(recomendaciones(r, alertas.size() - respondidas));
        r.setEnviado(true);
        r.setFechaEnvio(ahora);
        r.setVisto(false);
        return toDTO(repository.save(r));
    }

    private static String normalizarPeriodo(String periodo) {
        String per = periodo == null || periodo.isBlank() ? "SEMANAL" : periodo.trim().toUpperCase();
        if (!per.equals("SEMANAL") && !per.equals("MENSUAL")) {
            throw new IllegalArgumentException("periodo debe ser SEMANAL o MENSUAL");
        }
        return per;
    }

    /** Metricas de sesiones de un usuario entre dos fechas (ambas incluidas). */
    private Metricas metricas(Long idUsuario, LocalDate inicio, LocalDate fin, LocalDateTime ahora) {
        LocalDateTime hasta = fin.plusDays(1).atStartOfDay();
        LocalDateTime corte = hasta.isAfter(ahora) ? ahora : hasta;
        Metricas m = new Metricas();
        for (SesionJuego s : sesionJuegoRepository.buscarPorUsuarioEntre(idUsuario, inicio.atStartOfDay(), hasta)) {
            m.sesiones++;
            m.minutosTotales += SesionJuegoServiceImpl.minutosJugados(s, corte);
            m.videojuegos.add(s.getVideojuego().getIdVideojuego());
            LocalDate dia = s.getFechaInicio().toLocalDate();
            m.diasConJuego.add(dia);
            if (Boolean.FALSE.equals(s.getRespetoLimites())) {
                m.diasExcedidos.add(dia);
            } else {
                m.respetuosas++;
            }
        }
        return m;
    }

    /** Dias seguidos sin exceder el limite, contando hacia atras desde el ultimo dia del periodo. */
    private static int racha(LocalDate inicio, LocalDate fin, Set<LocalDate> diasExcedidos) {
        int racha = 0;
        for (LocalDate d = fin; !d.isBefore(inicio); d = d.minusDays(1)) {
            if (diasExcedidos.contains(d)) {
                break;
            }
            racha++;
        }
        return racha;
    }

    /**
     * Compara con el periodo anterior de la misma duracion: MEJORA si baja el tiempo de juego un 10 % o mas
     * (o sube el respeto de limites 10 puntos o mas); EMPEORA en el caso contrario; si no, ESTABLE.
     */
    private static String tendencia(Metricas actual, Metricas anterior) {
        if (anterior.sesiones == 0) {
            return "ESTABLE";
        }
        double variacionTiempo = (actual.minutosTotales - anterior.minutosTotales) / (double) anterior.minutosTotales;
        double variacionRespeto = actual.porcentajeRespeto().subtract(anterior.porcentajeRespeto()).doubleValue();
        int puntaje = 0;
        if (variacionTiempo <= -0.10) {
            puntaje++;
        } else if (variacionTiempo >= 0.10) {
            puntaje--;
        }
        if (variacionRespeto >= 10) {
            puntaje++;
        } else if (variacionRespeto <= -10) {
            puntaje--;
        }
        return puntaje > 0 ? "MEJORA" : puntaje < 0 ? "EMPEORA" : "ESTABLE";
    }

    private static String recomendaciones(ReportePadre r, int alertasSinResponder) {
        List<String> lista = new ArrayList<>();
        if (r.getRespetoLimitesPorc().compareTo(BigDecimal.valueOf(70)) < 0) {
            lista.add("Revisen juntos el límite diario: se respetó en menos del 70 % de las sesiones.");
        }
        if (r.getTiempoPromedioSesionMinutos() > 90) {
            lista.add("Las sesiones son largas: propongan una pausa activa cada 45 minutos.");
        }
        if (r.getDiasSinJuego() == 0) {
            lista.add("Acuerden al menos un día sin videojuegos en la semana.");
        }
        if (alertasSinResponder > 0) {
            lista.add("Hay " + alertasSinResponder + " alerta(s) sin responder: conversen sobre ellas.");
        }
        if ("EMPEORA".equals(r.getTendencia())) {
            lista.add("El uso empeoró respecto al periodo anterior; consideren ajustar horarios.");
        }
        if (lista.isEmpty()) {
            lista.add("Buen equilibrio en el periodo. Reconozcan el esfuerzo y mantengan la rutina.");
        }
        return String.join(" ", lista);
    }

    /** Acumulador de metricas de sesiones. */
    private static class Metricas {
        int sesiones;
        int minutosTotales;
        int respetuosas;
        final Set<Long> videojuegos = new HashSet<>();
        final Set<LocalDate> diasConJuego = new HashSet<>();
        final Set<LocalDate> diasExcedidos = new HashSet<>();

        BigDecimal porcentajeRespeto() {
            if (sesiones == 0) {
                return CIEN.setScale(2, RoundingMode.HALF_UP);
            }
            return BigDecimal.valueOf(respetuosas).multiply(CIEN)
                    .divide(BigDecimal.valueOf(sesiones), 2, RoundingMode.HALF_UP);
        }
    }
}
