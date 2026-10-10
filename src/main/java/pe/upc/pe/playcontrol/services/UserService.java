package pe.upc.pe.playcontrol.services;

import pe.upc.pe.playcontrol.entities.User;
import java.util.List;

public interface UserService {
    List<User> listarUsuarios();
    User registrarUsuario(User usuario);
}