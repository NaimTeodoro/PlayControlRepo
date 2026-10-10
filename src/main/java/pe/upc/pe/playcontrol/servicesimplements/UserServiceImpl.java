package pe.upc.pe.playcontrol.servicesimplements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.upc.pe.playcontrol.entities.User;
import pe.upc.pe.playcontrol.repositories.UserRepository;
import pe.upc.pe.playcontrol.services.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> listarUsuarios() {
        return userRepository.findAll();
    }

    @Override
    public User registrarUsuario(User usuario) {
        // Aquí puedes agregar lógica extra si deseas encriptar la contraseña luego
        return userRepository.save(usuario);
    }
}