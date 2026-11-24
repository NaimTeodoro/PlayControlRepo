package pe.upc.pe.playcontrol.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.upc.pe.playcontrol.dtos.ReportePadreDTO;
import pe.upc.pe.playcontrol.entities.*;
import pe.upc.pe.playcontrol.repositories.*;
import pe.upc.pe.playcontrol.services.ReportePadreService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Carga datos de prueba coherentes para TODAS las tablas al arrancar la aplicacion.
 * <p>
 * app.seed (APP_SEED): auto = solo si la BD es local; true = en cualquier BD; false = nunca.
 * En todos los casos solo carga si la tabla usuario esta vacia. Todos los usuarios tienen la contrasena 123456.
 * <p>
 * Escenarios que usa el front:
 * - jugador1: 45 min hoy (dentro de su limite de 2 h), 3 logros obtenidos (uno reclamado), completa 3 recursos.
 * - jugador2: 100 min hoy (supera su limite de 1,5 h) y una alerta LIMITE_DIARIO pendiente hoy.
 * - jugador3: sesion EN_CURSO ahora, sin limite activo, casi todos sus logros en progreso.
 * - jugador4: vinculo con padre1 PENDIENTE de autorizar.
 * - jugador5 y jugador8: ranking de puntos alto.
 */
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired private Environment env;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private UserRepository userRepository;
    @Autowired private JugadorJovenRepository jugadorJovenRepository;
    @Autowired private PadreTutorRepository padreTutorRepository;
    @Autowired private ProfesionalSaludRepository profesionalSaludRepository;
    @Autowired private PlataformaRepository plataformaRepository;
    @Autowired private VideojuegoRepository videojuegoRepository;
    @Autowired private PadreHijoRepository padreHijoRepository;
    @Autowired private DispositivoRepository dispositivoRepository;
    @Autowired private LimiteJuegoRepository limiteJuegoRepository;
    @Autowired private SesionJuegoRepository sesionJuegoRepository;
    @Autowired private AlertaJuegoRepository alertaJuegoRepository;
    @Autowired private HistorialAlertaRepository historialAlertaRepository;
    @Autowired private LogroRepository logroRepository;
    @Autowired private RecompensaRepository recompensaRepository;
    @Autowired private ProfesionalClienteRepository profesionalClienteRepository;
    @Autowired private RecursoEducativoRepository recursoEducativoRepository;
    @Autowired private UsuarioRecursoEducativoRepository usuarioRecursoEducativoRepository;
    @Autowired private ReportePadreRepository reportePadreRepository;
    @Autowired private ReportePadreService reportePadreService;

    private final Random rnd = new Random(42);
    private final Map<String, Integer> conteo = new LinkedHashMap<>();
    private String hash;
    private LocalDateTime ahora;

    // Datos creados que reutilizan los bloques siguientes
    private final Map<String, User> usuarios = new HashMap<>();
    private final Map<String, JugadorJoven> jovenes = new HashMap<>();
    private final Map<String, PadreTutor> padres = new HashMap<>();
    private final Map<String, ProfesionalSalud> profesionales = new HashMap<>();
    private final Map<String, List<Dispositivo>> dispositivosPorJugador = new HashMap<>();
    private final Map<String, LimiteJuego> limiteActivo = new HashMap<>();
    private final List<PadreHijo> vinculosActivos = new ArrayList<>();
    private final List<AlertaJuego> alertas = new ArrayList<>();
    private final List<RecursoEducativo> recursos = new ArrayList<>();
    private List<Videojuego> juegos;

    @Override
    @Transactional
    public void run(String... args) {
        String url = env.getProperty("spring.datasource.url", "");
        String seed = env.getProperty("app.seed", "auto").trim().toLowerCase();
        boolean local = url.contains("localhost") || url.contains("127.0.0.1");

        if ("false".equals(seed)) {
            System.out.println(">>> DataLoader: app.seed=false, no se cargan datos");
            return;
        }
        if ("auto".equals(seed) && !local) {
            System.out.println(">>> DataLoader: app.seed=auto y la BD no es local, no se cargan datos");
            return;
        }
        if (userRepository.count() > 0) {
            System.out.println(">>> DataLoader: la tabla usuario ya tiene datos, no se inserta nada");
            return;
        }
        System.out.println(">>> DataLoader: cargando datos de prueba (app.seed=" + seed + ")");

        ahora = LocalDateTime.now();
        hash = passwordEncoder.encode("123456");

        crearUsuariosYPerfiles();
        crearCatalogo();
        crearVinculos();
        crearLimites();
        crearDispositivos();
        crearSesionesYAlertas();
        crearHistorialAlertas();
        crearLogrosYRecompensas();
        crearProfesionales();
        crearRecursos();
        crearConsumoRecursos();
        crearReportesPadre();

        System.out.println(">>> DataLoader: carga terminada. Filas insertadas por tabla:");
        conteo.forEach((k, v) -> System.out.println("    " + k + ": " + v));
    }

    // ------------------------------------------------------------------ utilidades

    private void contar(String tabla) {
        conteo.merge(tabla, 1, Integer::sum);
    }

    private User u(String username) {
        return usuarios.get(username);
    }

    private static String fmt(BigDecimal h) {
        return h.stripTrailingZeros().toPlainString();
    }

    private <T> T elegir(List<T> lista) {
        return lista.get(rnd.nextInt(lista.size()));
    }

    private <T> T elegir(T[] arreglo) {
        return arreglo[rnd.nextInt(arreglo.length)];
    }

    private User crearUsuario(String username, String rol, String nombre, String apellido, LocalDate nac,
                              String genero, String ciudad, String nivelTec) {
        User x = new User();
        x.setUsername(username);
        x.setPassword(hash);
        x.setEnabled(true);
        x.setNombre(nombre);
        x.setApellido(apellido);
        x.setEmail(username + "@playcontrol.test");
        x.setTelefono("9" + (10000000 + rnd.nextInt(89999999)));
        x.setFechaNacimiento(nac);
        x.setGenero(genero);
        x.setEstado("ACTIVO");
        x.setFechaRegistro(ahora.minusDays(60));
        x.setUltimoAcceso(ahora.minusHours(1 + rnd.nextInt(48)));
        x.setPais("Perú");
        x.setCiudad(ciudad);
        x.setDireccion("Av. Los Álamos " + (100 + rnd.nextInt(900)) + ", " + ciudad);
        x.setNivelTecnologia(nivelTec);
        x.setConfiguracionPrivacidad("{\"compartirEstadisticas\":true,\"mostrarEnRanking\":true}");
        Authority a = new Authority();
        a.setAuthority(rol);
        x.setRoles(new ArrayList<>(List.of(a)));
        x = userRepository.save(x);
        usuarios.put(username, x);
        contar("usuario");
        contar("rol_usuario");
        return x;
    }

    // ------------------------------------------------------------------ 1-2. usuarios y perfiles

    private void crearUsuariosYPerfiles() {
        crearUsuario("admin", "ROLE_ADMIN", "Administrador", "Sistema", LocalDate.of(1985, 5, 20), "OTRO", "Lima", "ALTO");

        User p1 = crearUsuario("padre1", "ROLE_PADRE", "Carlos", "Ramírez Soto", LocalDate.of(1980, 3, 12), "MASCULINO", "Lima", "MEDIO");
        User p2 = crearUsuario("padre2", "ROLE_PADRE", "Lucía", "Fernández Vega", LocalDate.of(1983, 9, 3), "FEMENINO", "Arequipa", "BASICO");

        Object[][] js = {
                {"jugador1", "Mateo", "Ramírez Torres", LocalDate.of(2011, 3, 14), "MASCULINO", "Lima", "MEDIO"},
                {"jugador2", "Valentina", "Fernández Rojas", LocalDate.of(2010, 7, 22), "FEMENINO", "Arequipa", "ALTO"},
                {"jugador3", "Diego", "Fernández Rojas", LocalDate.of(2012, 11, 5), "MASCULINO", "Arequipa", "MEDIO"},
                {"jugador4", "Sofía", "Ramírez Torres", LocalDate.of(2009, 5, 19), "FEMENINO", "Lima", "ALTO"},
                {"jugador5", "Andrés", "Quispe Huamán", LocalDate.of(2008, 9, 30), "MASCULINO", "Cusco", "ALTO"},
                {"jugador6", "Camila", "Paredes Díaz", LocalDate.of(2013, 1, 12), "FEMENINO", "Trujillo", "BASICO"},
                {"jugador7", "Luis", "Mendoza Castro", LocalDate.of(2007, 12, 2), "MASCULINO", "Piura", "MEDIO"},
                {"jugador8", "Ana", "Salazar Núñez", LocalDate.of(2010, 4, 27), "FEMENINO", "Chiclayo", "ALTO"}
        };
        String[] habilidad = {"INTERMEDIO", "AVANZADO", "PRINCIPIANTE", "AVANZADO", "EXPERTO", "PRINCIPIANTE", "INTERMEDIO", "AVANZADO"};
        String[] motivacion = {"DIVERSION", "COMPETENCIA", "SOCIAL", "COMPETENCIA", "COMPETENCIA", "DIVERSION", "SOCIAL", "LOGROS"};
        String[] generoJuego = {"Aventura", "Shooter", "Sandbox", "Deportes", "Battle Royale", "Carreras", "Aventura", "Deportes"};
        for (int i = 0; i < js.length; i++) {
            Object[] d = js[i];
            User usr = crearUsuario((String) d[0], "ROLE_JUGADOR", (String) d[1], (String) d[2], (LocalDate) d[3],
                    (String) d[4], (String) d[5], (String) d[6]);
            JugadorJoven j = new JugadorJoven();
            j.setUsuario(usr);
            j.setCodigoVinculacion(String.format("PC-J%05d", i + 1));
            j.setNivelHabilidad(habilidad[i]);
            j.setGeneroPreferido(generoJuego[i]);
            j.setHoraPreferidaInicio(LocalTime.of(15, 0));
            j.setHoraPreferidaFin(LocalTime.of(20 + (i % 2), 0));
            j.setDiasJuegoPreferidos(i % 2 == 0 ? "LUN,MIE,VIE,SAB" : "MAR,JUE,SAB,DOM");
            j.setMotivacionPrincipal(motivacion[i]);
            boolean problematico = i == 1 || i == 2;
            j.setProblemasIdentificados(problematico ? "Tiende a exceder el tiempo de juego diario" : "Sin problemas identificados");
            j.setHistorialProblemas(problematico ? "Supera el límite diario con frecuencia desde hace varias semanas" : "Sin historial");
            j.setFechaRegistroSegmento(ahora.minusDays(55));
            jovenes.put((String) d[0], jugadorJovenRepository.save(j));
            contar("jugador_joven");
        }

        PadreTutor t1 = new PadreTutor();
        t1.setUsuario(p1);
        t1.setMetodosControlActual("Horarios acordados y revisión semanal");
        t1.setPreocupacionesPrincipales("Exceso de horas de juego entre semana");
        t1.setObjetivosSupervision("Equilibrar estudio, juego y descanso");
        t1.setFrecuenciaRevisionReportes("SEMANAL");
        t1.setConfiguracionAlertas("{\"limiteDiario\":true,\"horario\":true}");
        t1.setFechaRegistro(ahora.minusDays(58));
        padres.put("padre1", padreTutorRepository.save(t1));
        contar("padre_tutor");

        PadreTutor t2 = new PadreTutor();
        t2.setUsuario(p2);
        t2.setMetodosControlActual("Ninguno formal");
        t2.setPreocupacionesPrincipales("Juego nocturno y bajo rendimiento escolar");
        t2.setObjetivosSupervision("Reducir el tiempo de pantalla de ambos hijos");
        t2.setFrecuenciaRevisionReportes("DIARIA");
        t2.setConfiguracionAlertas("{\"limiteDiario\":true,\"horario\":true,\"descanso\":true}");
        t2.setFechaRegistro(ahora.minusDays(57));
        padres.put("padre2", padreTutorRepository.save(t2));
        contar("padre_tutor");

        User prof1 = crearUsuario("profesional1", "ROLE_PROFESIONAL", "Marina", "Vargas Lozano",
                LocalDate.of(1982, 6, 8), "FEMENINO", "Lima", "ALTO");
        ProfesionalSalud ps1 = new ProfesionalSalud();
        ps1.setUsuario(prof1);
        ps1.setEspecialidad("Psicología clínica infantil y adolescente");
        ps1.setNumeroLicencia("CPsP-28451");
        ps1.setInstitucion("Clínica Bienestar Digital");
        ps1.setAniosExperiencia(12);
        ps1.setAreaTrabajo("ADICCIONES_TECNOLOGICAS");
        ps1.setCertificaciones("Terapia cognitivo-conductual; Uso saludable de tecnología");
        ps1.setTelefonoConsulta("014567890");
        ps1.setHorarioConsulta("Lunes a viernes, 9:00 - 17:00");
        ps1.setVerificadoPor("Colegio de Psicólogos del Perú");
        ps1.setFechaVerificacion(ahora.minusDays(80));
        ps1.setEstado("ACTIVO");
        profesionales.put("profesional1", profesionalSaludRepository.save(ps1));
        contar("profesional_salud");

        User prof2 = crearUsuario("profesional2", "ROLE_PROFESIONAL", "Jorge", "Castillo Ríos",
                LocalDate.of(1978, 10, 15), "MASCULINO", "Trujillo", "ALTO");
        ProfesionalSalud ps2 = new ProfesionalSalud();
        ps2.setUsuario(prof2);
        ps2.setEspecialidad("Psiquiatría infantil y del adolescente");
        ps2.setNumeroLicencia("CMP-41872");
        ps2.setInstitucion("Hospital Regional Docente de Trujillo");
        ps2.setAniosExperiencia(15);
        ps2.setAreaTrabajo("SALUD_MENTAL_ADOLESCENTE");
        ps2.setCertificaciones("Especialidad en psiquiatría del adolescente; Trastornos por uso de videojuegos");
        ps2.setTelefonoConsulta("044231870");
        ps2.setHorarioConsulta("Martes y jueves, 14:00 - 19:00");
        ps2.setVerificadoPor("Colegio Médico del Perú");
        ps2.setFechaVerificacion(ahora.minusDays(70));
        ps2.setEstado("ACTIVO");
        profesionales.put("profesional2", profesionalSaludRepository.save(ps2));
        contar("profesional_salud");
    }

    // ------------------------------------------------------------------ 3. plataformas y videojuegos

    private void crearCatalogo() {
        Object[][] ps = {
                {"PC", "PC", "Computadora personal con Windows", "Varios", "Windows 11"},
                {"PlayStation 5", "CONSOLA", "Consola de sobremesa de Sony", "Sony", "PlayStation OS"},
                {"Nintendo Switch", "CONSOLA", "Consola híbrida de Nintendo", "Nintendo", "Nintendo Switch OS"},
                {"Android", "MOVIL", "Teléfonos y tablets Android", "Google", "Android 14"}
        };
        List<Plataforma> plat = new ArrayList<>();
        for (Object[] p : ps) {
            Plataforma x = new Plataforma();
            x.setNombrePlataforma((String) p[0]);
            x.setTipo((String) p[1]);
            x.setDescripcion((String) p[2]);
            x.setDesarrollador((String) p[3]);
            x.setSistemaOperativo((String) p[4]);
            x.setActiva(true);
            x.setFechaCreacion(ahora.minusDays(90));
            plat.add(plataformaRepository.save(x));
            contar("plataforma");
        }
        Object[][] js = {
                {"Minecraft", "Sandbox", "Mojang Studios", LocalDate.of(2011, 11, 18), "7", "Construye y explora mundos de bloques", 60, 0, "minecraft"},
                {"Fortnite", "Battle Royale", "Epic Games", LocalDate.of(2017, 7, 25), "12", "Battle royale con construcción", 45, 1, "fortnite"},
                {"EA Sports FC 25", "Deportes", "EA Sports", LocalDate.of(2024, 9, 27), "3", "Simulador de fútbol", 40, 1, "ea-sports-fc-25"},
                {"Zelda: Tears of the Kingdom", "Aventura", "Nintendo", LocalDate.of(2023, 5, 12), "12", "Aventura de mundo abierto", 75, 2, "zelda-tears-of-the-kingdom"},
                {"Mario Kart 8 Deluxe", "Carreras", "Nintendo", LocalDate.of(2017, 4, 28), "3", "Carreras arcade para toda la familia", 30, 2, "mario-kart-8-deluxe"},
                {"Free Fire", "Battle Royale", "Garena", LocalDate.of(2017, 8, 24), "12", "Battle royale para móviles", 25, 3, "free-fire"}
        };
        juegos = new ArrayList<>();
        for (Object[] g : js) {
            Videojuego j = new Videojuego();
            j.setNombreJuego((String) g[0]);
            j.setGenero((String) g[1]);
            j.setDesarrollador((String) g[2]);
            j.setFechaLanzamiento((LocalDate) g[3]);
            j.setClasificacionPEGI((String) g[4]);
            j.setDescripcion((String) g[5]);
            j.setTiempoPromedioSesion((Integer) g[6]);
            j.setPlataforma(plat.get((Integer) g[7]));
            j.setUrlImagen("https://recursos.playcontrol.test/juegos/" + g[8] + ".png");
            j.setActivo(true);
            j.setFechaRegistro(ahora.minusDays(90));
            juegos.add(videojuegoRepository.save(j));
            contar("videojuego");
        }
    }

    // ------------------------------------------------------------------ 4. vinculos padre-hijo

    private void crearVinculos() {
        Object[][] vs = {
                {"padre1", "jugador1", "ACTIVO", true, "PADRE"},
                {"padre1", "jugador4", "PENDIENTE", false, "PADRE"},
                {"padre2", "jugador2", "ACTIVO", true, "MADRE"},
                {"padre2", "jugador3", "ACTIVO", true, "MADRE"}
        };
        for (Object[] v : vs) {
            PadreHijo ph = new PadreHijo();
            ph.setPadreTutor(padres.get((String) v[0]));
            ph.setJugadorJoven(jovenes.get((String) v[1]));
            ph.setEstado((String) v[2]);
            ph.setAutorizado((Boolean) v[3]);
            ph.setTipoRelacion((String) v[4]);
            ph.setFechaAsociacion(ahora.minusDays(50));
            ph.setPermisosSupervision("VER_REPORTES,CONFIGURAR_LIMITES");
            ph = padreHijoRepository.save(ph);
            if ("ACTIVO".equals(ph.getEstado())) {
                vinculosActivos.add(ph);
            }
            contar("padre_hijo");
        }
    }

    // ------------------------------------------------------------------ 6. limites

    private void crearLimites() {
        String todos = "LUN,MAR,MIE,JUE,VIE,SAB,DOM";
        // jugador, diarias, semanal, mensual, dias, estado, metodo, accion, horaInicio, horaFin, avisoPrevio, observaciones, creadoPor
        Object[][] ls = {
                {"jugador1", "2.00", "14.00", "60.00", "LUN,MAR,MIE,JUE,VIE", "ACTIVO", "NOTIFICACION", "ALERTAR", 15, 21, 15, "Mensajes de SpaceDog: SI", "padre1"},
                {"jugador1", "3.00", "6.00", "24.00", "SAB,DOM", "INACTIVO", "BLOQUEO_SIMPLE", "BLOQUEAR", 10, 20, 10, "Mensajes de SpaceDog: NO", "padre1"},
                {"jugador2", "1.50", "10.00", "45.00", todos, "ACTIVO", "BLOQUEO_TOTAL", "BLOQUEAR", 15, 21, 15, "Mensajes de SpaceDog: SI", "padre2"},
                {"jugador3", "3.00", "20.00", "80.00", todos, "INACTIVO", "NOTIFICACION", "ALERTAR", 15, 23, 15, "Sin límite activo", "padre2"},
                {"jugador4", "2.50", "15.00", "60.00", todos, "ACTIVO", "BLOQUEO_SIMPLE", "BLOQUEAR", 16, 20, 10, "Mensajes de SpaceDog: SI", "jugador4"},
                {"jugador5", "2.00", "12.00", "50.00", todos, "ACTIVO", "NOTIFICACION", "ALERTAR", 15, 21, 15, "Mensajes de SpaceDog: SI", "jugador5"},
                {"jugador6", "2.00", "12.00", "50.00", todos, "ACTIVO", "NOTIFICACION", "ALERTAR", 15, 21, 15, "Mensajes de SpaceDog: SI", "jugador6"},
                {"jugador7", "2.00", "12.00", "50.00", todos, "ACTIVO", "NOTIFICACION", "ALERTAR", 15, 21, 15, "Mensajes de SpaceDog: SI", "jugador7"},
                {"jugador8", "2.00", "12.00", "50.00", todos, "ACTIVO", "NOTIFICACION", "ALERTAR", 15, 21, 15, "Mensajes de SpaceDog: SI", "jugador8"}
        };
        for (Object[] l : ls) {
            LimiteJuego x = new LimiteJuego();
            x.setUsuario(u((String) l[0]));
            x.setCreadoPor(u((String) l[12]));
            x.setLimiteHorasDiarias(new BigDecimal((String) l[1]));
            x.setLimiteHorasSemanal(new BigDecimal((String) l[2]));
            x.setLimiteHorasMensual(new BigDecimal((String) l[3]));
            x.setDiasPermitidos((String) l[4]);
            x.setEstado((String) l[5]);
            x.setMetodoBloqueo((String) l[6]);
            x.setAccionAlExceder((String) l[7]);
            x.setHoraInicioDia(LocalTime.of((Integer) l[8], 0));
            x.setHoraFinDia(LocalTime.of((Integer) l[9], 0));
            x.setMinutosAvisoPrevio((Integer) l[10]);
            x.setObservaciones((String) l[11]);
            x.setFechaCreacion(ahora.minusDays(45));
            x.setUltimaModificacion(ahora.minusDays(10));
            x = limiteJuegoRepository.save(x);
            contar("limite_juego");
            if ("ACTIVO".equals(x.getEstado())) {
                limiteActivo.put((String) l[0], x);
            }
        }
    }

    // ------------------------------------------------------------------ 5. dispositivos (antes que las sesiones)

    private void crearDispositivos() {
        Object[][] ds = {
                {"jugador1", "PC", "ASUS ROG Strix G15", "Windows 11"},
                {"jugador1", "MOVIL", "Samsung Galaxy A54", "Android 14"},
                {"jugador2", "CONSOLA", "PlayStation 5", "PlayStation OS"},
                {"jugador3", "MOVIL", "Xiaomi Redmi Note 12", "Android 13"},
                {"jugador4", "PC", "HP Pavilion Gaming", "Windows 11"},
                {"jugador5", "MOVIL", "Motorola Moto G54", "Android 14"},
                {"jugador6", "PC", "Lenovo IdeaPad 3", "Windows 10"},
                {"jugador7", "MOVIL", "Honor X7b", "Android 13"},
                {"jugador8", "PC", "Acer Nitro 5", "Windows 11"}
        };
        int i = 1;
        for (Object[] d : ds) {
            Dispositivo x = new Dispositivo();
            x.setUsuario(u((String) d[0]));
            x.setTipoDispositivo((String) d[1]);
            x.setModelo((String) d[2]);
            x.setSistemaOperativo((String) d[3]);
            x.setIdentificadorDisp("DEV-" + String.format("%03d", i) + "-" + ((String) d[0]).toUpperCase());
            x.setFechaAsociacion(ahora.minusDays(40));
            x.setEstado("ACTIVO");
            x.setUltimaActividad(ahora.minusHours(rnd.nextInt(72)));
            x.setVersionApp("1.0." + (i % 5));
            x = dispositivoRepository.save(x);
            dispositivosPorJugador.computeIfAbsent((String) d[0], k -> new ArrayList<>()).add(x);
            contar("dispositivo");
            i++;
        }
    }

    // ------------------------------------------------------------------ 7-8. sesiones y alertas

    private static final String[] EFECTOS = {"Ninguno", "Ninguno", "Cansancio visual leve", "Dolor de cabeza leve",
            "Irritabilidad al terminar", "Dificultad para concentrarse en las tareas"};
    private static final String[] ANOTACIONES = {"Jugué con amigos del colegio", "Practiqué para un torneo en línea",
            "Terminé una misión difícil", "Me costó dejar de jugar", "Hice una pausa a la mitad",
            "Jugué después de terminar las tareas"};
    private static final String[][] RESPUESTAS = {
            {"Entendido, termino la partida", "TERMINO_SESION"},
            {"Voy a hacer una pausa", "PAUSA"},
            {"Ok, solo una partida más", "IGNORADA"},
            {"Ya terminé", "TERMINO_SESION"}
    };

    private void crearSesionesYAlertas() {
        LocalDate hoy = ahora.toLocalDate();
        LocalDateTime medianoche = hoy.atStartOfDay();

        for (int n = 1; n <= 8; n++) {
            String un = "jugador" + n;
            User usr = u(un);
            LimiteJuego lim = limiteActivo.get(un);
            BigDecimal limHoras = lim != null ? lim.getLimiteHorasDiarias() : null;
            int refMin = limHoras != null ? limHoras.multiply(BigDecimal.valueOf(60)).intValue() : 180;
            int aviso = lim != null ? lim.getMinutosAvisoPrevio() : 0;
            // Sin limite activo se usa 22:00 como hora nocturna de referencia
            LocalTime horaNocturna = lim != null ? lim.getHoraFinDia() : LocalTime.of(22, 0);

            for (int d = 56; d >= 1; d--) {
                LocalDate dia = hoy.minusDays(d);
                boolean finSemana = dia.getDayOfWeek() == DayOfWeek.SATURDAY || dia.getDayOfWeek() == DayOfWeek.SUNDAY;
                if (rnd.nextDouble() >= (finSemana ? 0.85 : 0.70)) {
                    continue; // dia sin juego
                }
                int dur = finSemana ? 40 + rnd.nextInt(131) : 20 + rnd.nextInt(91);
                if (n == 2 || n == 3) {
                    dur += 30;
                }
                int ini = finSemana ? 600 + rnd.nextInt(661) : 900 + rnd.nextInt(301);
                if (ini + dur > 1439) {
                    ini = 1439 - dur;
                }
                LocalDateTime inicio = dia.atStartOfDay().plusMinutes(ini);
                SesionJuego s = guardarSesion(usr, un, inicio, dur, "FINALIZADA", dur <= refMin);
                generarAlertas(usr, s, limHoras, refMin, aviso, horaNocturna, false);
            }
        }

        // --- Sesiones de HOY (obligatorias)
        guardarSesion(u("jugador1"), "jugador1", inicioHoy(45, medianoche), 45, "FINALIZADA", true);

        LimiteJuego lim2 = limiteActivo.get("jugador2");
        SesionJuego s2 = guardarSesion(u("jugador2"), "jugador2", inicioHoy(100, medianoche), 100, "FINALIZADA", false);
        generarAlertas(u("jugador2"), s2, lim2.getLimiteHorasDiarias(), 90, lim2.getMinutosAvisoPrevio(),
                lim2.getHoraFinDia(), true);

        SesionJuego enCurso = new SesionJuego();
        LocalDateTime ini3 = ahora.minusMinutes(40 + rnd.nextInt(51));
        if (ini3.isBefore(medianoche)) {
            ini3 = medianoche;
        }
        enCurso.setUsuario(u("jugador3"));
        enCurso.setVideojuego(elegir(juegos));
        enCurso.setDispositivo(elegir(dispositivosPorJugador.get("jugador3")));
        enCurso.setFechaInicio(ini3);
        enCurso.setFechaFin(null);
        enCurso.setDuracionMinutos(null);
        enCurso.setPausasRealizadas(0);
        enCurso.setNumeroInterrupciones(0);
        enCurso.setEstadoSesion("EN_CURSO");
        enCurso.setEnergiaFisica("NORMAL");
        enCurso.setEstadoAnimo("BIEN");
        enCurso.setRespetoLimites(true);
        sesionJuegoRepository.save(enCurso);
        contar("sesion_juego");
    }

    /** Inicio aleatorio de una sesion de hoy que termine antes de "ahora" (si cabe). */
    private LocalDateTime inicioHoy(int dur, LocalDateTime medianoche) {
        LocalDateTime masTarde = ahora.minusMinutes(dur);
        if (masTarde.isBefore(medianoche)) {
            return medianoche;
        }
        long span = Duration.between(medianoche, masTarde).toMinutes();
        return medianoche.plusMinutes(rnd.nextInt((int) span + 1));
    }

    private SesionJuego guardarSesion(User usr, String username, LocalDateTime inicio, int dur, String estado, boolean respeto) {
        SesionJuego s = new SesionJuego();
        s.setUsuario(usr);
        s.setVideojuego(elegir(juegos));
        s.setDispositivo(elegir(dispositivosPorJugador.get(username)));
        s.setFechaInicio(inicio);
        s.setFechaFin(inicio.plusMinutes(dur));
        s.setDuracionMinutos(dur);
        s.setPausasRealizadas(dur / 45 + rnd.nextInt(2));
        s.setNumeroInterrupciones(rnd.nextInt(4));
        s.setEstadoSesion(estado);
        s.setRespetoLimites(respeto);
        // Sesiones largas -> mas cansancio; exceder el limite -> peor animo
        s.setEnergiaFisica(dur > 120 ? (rnd.nextInt(10) < 6 ? "BAJA" : "NORMAL") : elegir(new String[]{"NORMAL", "NORMAL", "ALTA", "BAJA"}));
        s.setEstadoAnimo(!respeto ? elegir(new String[]{"REGULAR", "MAL", "REGULAR", "BIEN"}) : elegir(new String[]{"BIEN", "BIEN", "BIEN", "REGULAR"}));
        boolean nocturna = s.getFechaFin().toLocalTime().isAfter(LocalTime.of(22, 0));
        s.setEfectosPostJuego(nocturna ? "Dificultad para dormir" : elegir(EFECTOS));
        if (rnd.nextInt(10) < 3) {
            s.setAnotacionesUsuario(elegir(ANOTACIONES));
        }
        s = sesionJuegoRepository.save(s);
        contar("sesion_juego");
        return s;
    }

    /** AVISO_PREVIO al acercarse al limite, LIMITE_DIARIO al superarlo y HORARIO_NOCTURNO si termina tarde. */
    private void generarAlertas(User usr, SesionJuego s, BigDecimal limHoras, int refMin, int aviso,
                                LocalTime horaNocturna, boolean forzarPendiente) {
        int dur = s.getDuracionMinutos();
        LocalDateTime inicio = s.getFechaInicio();
        if (limHoras != null) {
            int minAviso = refMin - aviso;
            if (aviso > 0 && dur >= minAviso) {
                guardarAlerta(usr, s, "AVISO_PREVIO",
                        "Te quedan " + aviso + " minutos para llegar a tu límite diario de " + fmt(limHoras) + " h",
                        "MEDIA", minAviso, limHoras, "Ve cerrando la partida", inicio.plusMinutes(minAviso), forzarPendiente);
            }
            if (dur > refMin) {
                guardarAlerta(usr, s, "LIMITE_DIARIO", "Superaste tu límite diario de " + fmt(limHoras) + " h",
                        "ALTA", refMin, limHoras, "Detén la partida y descansa", inicio.plusMinutes(refMin), forzarPendiente);
            }
        }
        LocalDateTime limiteNoche = inicio.toLocalDate().atTime(horaNocturna);
        if (s.getFechaFin().isAfter(limiteNoche)) {
            LocalDateTime envio = inicio.isAfter(limiteNoche) ? inicio.plusMinutes(5) : limiteNoche;
            if (envio.isAfter(s.getFechaFin())) {
                envio = s.getFechaFin();
            }
            guardarAlerta(usr, s, "HORARIO_NOCTURNO",
                    "Estás jugando después de las " + horaNocturna + ", fuera de tu horario permitido",
                    "ALTA", (int) Duration.between(inicio, envio).toMinutes(), limHoras,
                    "Termina la sesión y prepárate para descansar", envio, forzarPendiente);
        }
    }

    private void guardarAlerta(User usr, SesionJuego s, String tipo, String mensaje, String intensidad, int tiempo,
                               BigDecimal limite, String accion, LocalDateTime envio, boolean forzarPendiente) {
        AlertaJuego a = new AlertaJuego();
        a.setUsuario(usr);
        a.setSesion(s);
        a.setTipoAlerta(tipo);
        a.setMensaje(mensaje);
        a.setIntensidad(intensidad);
        a.setTiempoTranscurrido(tiempo);
        a.setLimiteAlcanzado(limite);
        a.setAccionSugerida(accion);
        a.setFechaEnvio(envio);
        boolean antigua = !forzarPendiente && envio.isBefore(ahora.minusDays(7));
        if (antigua) {
            String[] r = elegir(RESPUESTAS);
            a.setEstadoAlerta("RESPONDIDA");
            a.setRespondidoUsuario(true);
            a.setRespuestaUsuario(r[0]);
            a.setTiempoRespuesta(1 + rnd.nextInt(10));
        } else {
            a.setEstadoAlerta("ENVIADA");
            a.setRespondidoUsuario(false);
        }
        alertas.add(alertaJuegoRepository.save(a));
        contar("alerta_juego");
    }

    // ------------------------------------------------------------------ 9. historial de alertas

    private void crearHistorialAlertas() {
        for (AlertaJuego a : alertas) {
            if (!"RESPONDIDA".equals(a.getEstadoAlerta())) {
                continue;
            }
            String accion = "IGNORADA";
            for (String[] r : RESPUESTAS) {
                if (r[0].equals(a.getRespuestaUsuario())) {
                    accion = r[1];
                }
            }
            HistorialAlerta h = new HistorialAlerta();
            h.setAlerta(a);
            h.setDescripcion("Alerta " + a.getTipoAlerta() + " respondida por " + a.getUsuario().getUsername()
                    + " a los " + a.getTiempoRespuesta() + " min: \"" + a.getRespuestaUsuario() + "\"");
            h.setAccionRealizada(accion);
            switch (accion) {
                case "TERMINO_SESION" -> {
                    h.setImpactoEnSesion("Sesión finalizada " + a.getTiempoRespuesta() + " min después de la alerta");
                    h.setEfectividadAlerta("ALTA");
                }
                case "PAUSA" -> {
                    h.setImpactoEnSesion("Pausa de 10 minutos y retomó el juego");
                    h.setEfectividadAlerta("MEDIA");
                }
                default -> {
                    h.setImpactoEnSesion("Siguió jugando unos " + (10 + rnd.nextInt(30)) + " minutos más");
                    h.setEfectividadAlerta("BAJA");
                    h.setNotasAdicionales("Conviene revisar el límite y el horario con la familia");
                }
            }
            h.setFecha(a.getFechaEnvio().plusMinutes(a.getTiempoRespuesta()));
            h.setEstado("CERRADO");
            historialAlertaRepository.save(h);
            contar("historial_alerta");
        }
    }

    // ------------------------------------------------------------------ 10-11. logros y recompensas

    private void crearLogrosYRecompensas() {
        // nombre, descripcion, puntos, tipo, condicion, condicionTipo, valor, slug
        Object[][] ls = {
                {"Descanso activo", "Haz pausas activas durante tus sesiones de juego", 50, "SEMANAL", "Realiza 5 pausas activas durante tus sesiones", "PAUSAS_REALIZADAS", 5, "descanso-activo"},
                {"Respeta tu límite", "Termina tus sesiones dentro del límite diario", 150, "SEMANAL", "Respeta tu límite diario 3 días", "DIAS_RESPETANDO_LIMITE", 3, "respeta-tu-limite"},
                {"Día sin pantallas", "Pasa un día completo sin videojuegos", 300, "DIARIO", "Completa 1 día sin jugar", "DIAS_SIN_JUEGO", 1, "dia-sin-pantallas"},
                {"Juega al aire libre", "Cambia la pantalla por actividades al aire libre", 100, "SEMANAL", "Completa 2 días sin jugar en la semana", "DIAS_SIN_JUEGO", 2, "juega-al-aire-libre"},
                {"Semana equilibrada", "Cumple tus límites durante 7 días seguidos", 200, "HITO", "Respeta tu límite diario 7 días seguidos", "DIAS_RESPETANDO_LIMITE", 7, "semana-equilibrada"},
                {"Pausa de ojos", "Descansa la vista 5 minutos cada hora de juego", 80, "HITO", "Realiza 10 pausas para descansar la vista", "PAUSAS_REALIZADAS", 10, "pausa-de-ojos"},
                {"Sesiones cortas", "Juega en sesiones de menos de 45 minutos", 120, "SEMANAL", "Completa 5 sesiones de menos de 45 minutos", "SESIONES_CORTAS", 5, "sesiones-cortas"},
                {"Aprende jugando", "Aprende sobre uso saludable de la tecnología", 100, "HITO", "Completa 3 recursos educativos", "RECURSOS_COMPLETADOS", 3, "aprende-jugando"}
        };
        List<Logro> logros = new ArrayList<>();
        for (Object[] l : ls) {
            Logro x = new Logro();
            x.setNombreLogro((String) l[0]);
            x.setDescripcion((String) l[1]);
            x.setPuntosAsignados((Integer) l[2]);
            x.setTipo((String) l[3]);
            x.setCondicion((String) l[4]);
            x.setCondicionTipo((String) l[5]);
            x.setValorCondicion((Integer) l[6]);
            x.setImagenUrl("https://recursos.playcontrol.test/logros/" + l[7] + ".png");
            x.setActivo(true);
            logros.add(logroRepository.save(x));
            contar("logro");
        }

        // jugador -> logros OBTENIDA (indices 1..8), logros RECLAMADA, rango de % para los EN_PROGRESO
        Object[][] asig = {
                {"jugador1", new int[]{4, 8}, new int[]{1}, 20, 80},
                {"jugador2", new int[]{6}, new int[]{1}, 10, 60},
                {"jugador3", new int[]{}, new int[]{}, 0, 25},
                {"jugador4", new int[]{3, 5}, new int[]{}, 15, 75},
                {"jugador5", new int[]{1, 6, 7, 3}, new int[]{2, 5}, 40, 90},
                {"jugador6", new int[]{1, 2}, new int[]{}, 10, 70},
                {"jugador7", new int[]{4}, new int[]{}, 10, 60},
                {"jugador8", new int[]{1, 2, 3, 7}, new int[]{5, 6, 4}, 50, 95}
        };
        for (Object[] a : asig) {
            String un = (String) a[0];
            List<Integer> obtenidas = toList((int[]) a[1]);
            List<Integer> reclamadas = toList((int[]) a[2]);
            int min = (Integer) a[3];
            int max = (Integer) a[4];
            for (int idx = 1; idx <= logros.size(); idx++) {
                Recompensa r = new Recompensa();
                r.setUsuario(u(un));
                r.setLogro(logros.get(idx - 1));
                if (obtenidas.contains(idx) || reclamadas.contains(idx)) {
                    LocalDateTime fechaObt = ahora.minusDays(1 + rnd.nextInt(27)).withHour(18 + rnd.nextInt(4)).withMinute(0).withSecond(0).withNano(0);
                    r.setPorcentajeProgreso(100);
                    r.setFechaObtenida(fechaObt);
                    r.setNotificacionEnviada(true);
                    if (reclamadas.contains(idx)) {
                        LocalDateTime fechaRec = fechaObt.plusDays(1 + rnd.nextInt(3));
                        r.setEstado("RECLAMADA");
                        r.setFechaReclamada(fechaRec.isAfter(ahora) ? ahora : fechaRec);
                    } else {
                        r.setEstado("OBTENIDA");
                    }
                } else {
                    r.setEstado("EN_PROGRESO");
                    r.setPorcentajeProgreso(min + rnd.nextInt(max - min + 1));
                    r.setNotificacionEnviada(false);
                }
                recompensaRepository.save(r);
                contar("recompensa");
            }
        }
    }

    private static List<Integer> toList(int[] arr) {
        List<Integer> l = new ArrayList<>();
        for (int v : arr) {
            l.add(v);
        }
        return l;
    }

    // ------------------------------------------------------------------ 12. profesional - cliente

    private void crearProfesionales() {
        // profesional, jugador, diagnostico, plan, frecuencia, dias hasta la proxima consulta
        Object[][] cs = {
                {"profesional1", "jugador3", "Uso problemático de videojuegos en horario nocturno", "Reducir sesiones nocturnas y establecer rutina de sueño", "Semanal", 3},
                {"profesional1", "jugador2", "Uso intensivo en fines de semana", "Pausas programadas y actividades alternativas", "Quincenal", 10},
                {"profesional2", "jugador5", "Uso intensivo competitivo con sesiones largas antes de torneos", "Planificar entrenamientos con descansos y metas fuera del juego", "Quincenal", 7}
        };
        for (Object[] c : cs) {
            ProfesionalCliente pc = new ProfesionalCliente();
            pc.setProfesional(profesionales.get((String) c[0]));
            pc.setJugadorJoven(jovenes.get((String) c[1]));
            pc.setFechaAsociacion(ahora.minusDays(30));
            pc.setEstado("ACTIVO");
            pc.setDiagnostico((String) c[2]);
            pc.setPlanTratamiento((String) c[3]);
            pc.setFrecuenciaConsulta((String) c[4]);
            pc.setProximaConsulta(ahora.plusDays((Integer) c[5]).withHour(10).withMinute(0).withSecond(0).withNano(0));
            pc.setNotas("Evolución favorable; se revisa en la próxima consulta");
            profesionalClienteRepository.save(pc);
            contar("profesional_cliente");
        }
    }

    // ------------------------------------------------------------------ 13. recursos educativos

    private void crearRecursos() {
        Object[][] rs = {
                {"Cómo equilibrar el tiempo de juego", "Guía para familias sobre límites saludables", "Define horarios fijos, pausas cada 45 minutos y días sin pantallas.", "ARTICULO", "PDF", 12, "PADRES", "Salud digital", "INTERMEDIO"},
                {"Señales de uso excesivo de videojuegos", "Cómo reconocer cuándo el juego se vuelve un problema", "Irritabilidad al dejar de jugar, descuido de tareas y alteración del sueño.", "VIDEO", "MP4", 8, "PADRES", "Prevención", "BASICO"},
                {"Pausas activas para jugadores", "Ejercicios cortos para hacer entre partidas", "Estiramientos de cuello, muñecas y vista para practicar cada hora.", "GUIA", "PDF", 5, "JUGADORES", "Bienestar", "BASICO"},
                {"Acompañamiento profesional ante el uso problemático", "Orientación para profesionales de salud", "Pautas de evaluación y seguimiento de jóvenes con uso problemático de videojuegos.", "ARTICULO", "HTML", 20, "PROFESIONALES", "Clínica", "AVANZADO"}
        };
        int n = 1;
        for (Object[] r : rs) {
            RecursoEducativo x = recursoBase((String) r[0], (String) r[2]);
            x.setDescripcion((String) r[1]);
            x.setTipoRecurso((String) r[3]);
            x.setFormato((String) r[4]);
            x.setDuracionMinutos((Integer) r[5]);
            x.setAudienciaObjetivo((String) r[6]);
            x.setCategoria((String) r[7]);
            x.setSubcategoria("General");
            x.setNivelDificultad((String) r[8]);
            x.setUrlRecurso("https://recursos.playcontrol.test/" + n);
            x.setUrlImagen("https://recursos.playcontrol.test/img/" + n + ".png");
            x.setVisitas(20 + rnd.nextInt(200));
            recursos.add(recursoEducativoRepository.save(x));
            contar("recurso_educativo");
            n++;
        }

        String[][] faqs = {
                {"¿Cómo configuro mi límite diario de juego?", "Entra en Bloqueo, elige las horas diarias y los días permitidos y guarda. Tus padres pueden ayudarte a ajustarlo."},
                {"¿Qué pasa cuando supero mi límite?", "Recibirás una alerta y, según el método elegido, la sesión se bloqueará o se te avisará para que descanses."},
                {"¿Mis padres pueden ver todo lo que hago?", "No. Solo ven totales de tiempo de juego, alertas y reportes, no detalles privados de tus partidas."},
                {"¿Cómo vinculo a mi hijo con mi cuenta de padre?", "Pídele a tu hijo su código de vinculación (empieza por PC-, por ejemplo PC-J00001), que aparece en su perfil. Ingrésalo en la sección de vínculos: la solicitud queda PENDIENTE hasta que tu hijo la autorice desde su cuenta."},
                {"¿Cómo gano puntos y recompensas?", "Completando logros como respetar tu límite, hacer pausas activas o completar recursos educativos. Cada logro obtenido suma puntos y puedes reclamarlo desde tu perfil."}
        };
        for (String[] f : faqs) {
            RecursoEducativo x = recursoBase(f[0], f[1]);
            x.setDescripcion("Pregunta frecuente");
            x.setTipoRecurso("FAQ");
            x.setCategoria("FAQ");
            x.setAudienciaObjetivo("TODOS");
            x.setFormato("HTML");
            x.setDuracionMinutos(2);
            x.setNivelDificultad("BASICO");
            recursos.add(recursoEducativoRepository.save(x));
            contar("recurso_educativo");
        }
    }

    private RecursoEducativo recursoBase(String titulo, String contenido) {
        RecursoEducativo x = new RecursoEducativo();
        x.setTitulo(titulo);
        x.setContenido(contenido);
        x.setAutor("Equipo PlayControl");
        x.setActivo(true);
        x.setVisitas(0);
        x.setValoracion(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        x.setFechaPublicacion(ahora.minusDays(30));
        x.setFechaActualizacion(ahora.minusDays(5));
        return x;
    }

    // ------------------------------------------------------------------ 14. consumo de recursos

    private void crearConsumoRecursos() {
        // recursos: 1..4 = articulos/guias, 5..9 = FAQ (5 limite, 6 superar limite, 7 privacidad, 8 vinculacion, 9 puntos)
        // usuario, recurso, completado, valoracion, comentario
        Object[][] cs = {
                {"padre1", 1, true, 5, "Muy útil para organizar los horarios en casa"},
                {"padre1", 2, true, 4, null},
                {"padre1", 8, true, 5, "Clarísimo, ya vinculé a mi hijo"},
                {"padre2", 1, true, 4, null},
                {"padre2", 2, false, 4, "Lo terminaré de ver el fin de semana"},
                {"padre2", 8, true, 5, null},
                {"jugador1", 3, true, 5, "Los estiramientos de muñeca me sirven"},
                {"jugador1", 5, true, 4, null},
                {"jugador1", 9, true, 5, null},
                {"jugador2", 3, false, 3, null},
                {"jugador2", 6, true, 3, "No me gusta que me bloquee"},
                {"jugador3", 6, true, 3, null},
                {"jugador4", 3, true, 4, null},
                {"jugador4", 5, false, 4, null},
                {"jugador5", 3, true, 4, "Lo uso entre partidas del torneo"},
                {"jugador5", 9, true, 5, null},
                {"jugador8", 3, true, 5, null},
                {"jugador8", 9, true, 4, null},
                {"profesional1", 4, true, 5, "Buen resumen de las pautas de evaluación"},
                {"profesional2", 4, true, 4, null}
        };
        Map<Long, List<Integer>> valoraciones = new HashMap<>();
        for (Object[] c : cs) {
            RecursoEducativo rec = recursos.get((Integer) c[1] - 1);
            boolean completado = (Boolean) c[2];
            int duracion = rec.getDuracionMinutos() != null ? rec.getDuracionMinutos() : 5;
            UsuarioRecursoEducativo x = new UsuarioRecursoEducativo();
            x.setUsuario(u((String) c[0]));
            x.setRecurso(rec);
            x.setFechaAcceso(ahora.minusDays(1 + rnd.nextInt(25)).minusHours(rnd.nextInt(10)));
            x.setTiempoVisualizacionMinutos(completado ? duracion : Math.max(1, duracion / 2));
            x.setCompletado(completado);
            x.setValoracionUsuario((Integer) c[3]);
            x.setComentario((String) c[4]);
            usuarioRecursoEducativoRepository.save(x);
            contar("usuario_recurso_educativo");
            valoraciones.computeIfAbsent(rec.getIdRecurso(), k -> new ArrayList<>()).add((Integer) c[3]);
        }
        // visitas y valoracion promedio coherentes con el consumo registrado
        for (RecursoEducativo rec : recursos) {
            List<Integer> vals = valoraciones.getOrDefault(rec.getIdRecurso(), List.of());
            rec.setVisitas(rec.getVisitas() + vals.size());
            double prom = vals.stream().mapToInt(Integer::intValue).average().orElse(0);
            rec.setValoracion(BigDecimal.valueOf(prom).setScale(2, RoundingMode.HALF_UP));
            recursoEducativoRepository.save(rec);
        }
    }

    // ------------------------------------------------------------------ 15. reportes para padres

    private void crearReportesPadre() {
        LocalDate hoy = ahora.toLocalDate();
        for (PadreHijo ph : vinculosActivos) {
            // 4 semanas: de la mas antigua (k=3) a la actual (k=0, termina hoy)
            for (int k = 3; k >= 0; k--) {
                LocalDate fin = hoy.minusDays(7L * k);
                LocalDate inicio = fin.minusDays(6);
                // Se calcula con el mismo servicio que usa POST /api/reportes-padre/generar (datos reales)
                ReportePadreDTO dto = reportePadreService.generarPeriodo(ph.getIdPadreHijo(), "SEMANAL", inicio, fin);
                ReportePadre r = reportePadreRepository.findById(dto.getIdReporte()).orElseThrow();
                LocalDateTime generado = k == 0 ? ahora : fin.plusDays(1).atTime(8, 0);
                r.setFechaGeneracion(generado);
                r.setEnviado(true);
                r.setFechaEnvio(generado);
                if (k == 0) {
                    r.setVisto(false);
                    r.setFechaVisto(null);
                } else {
                    r.setVisto(true);
                    r.setFechaVisto(generado.plusHours(2 + rnd.nextInt(30)));
                }
                reportePadreRepository.save(r);
                contar("reporte_padre");
            }
        }
    }
}
