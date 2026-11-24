package pe.upc.pe.playcontrol;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PlayControlApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlayControlApplication.class, args);
    }


    // STRICT: solo mapea propiedades con el mismo nombre. Las relaciones (idUsuario, idLogro...) se mapean a mano
    // en cada servicio; asi ModelMapper no "adivina" rutas ambiguas como usuario.idUsuario vs creadoPor.idUsuario.
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper;
    }

}
