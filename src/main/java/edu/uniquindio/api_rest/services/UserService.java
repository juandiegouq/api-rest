package edu.uniquindio.api_rest.services;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import edu.uniquindio.api_rest.models.Error;
import edu.uniquindio.api_rest.models.Login;
import edu.uniquindio.api_rest.models.RecuperacionClave;
import edu.uniquindio.api_rest.models.RegistroExitoso;
import edu.uniquindio.api_rest.models.Usuario;
import edu.uniquindio.api_rest.models.UsuarioActualizacion;
import edu.uniquindio.api_rest.models.UsuarioRegistro;
import edu.uniquindio.api_rest.repositories.*;


@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Registro de usuario
    public ResponseEntity<?> registrarUsuario(UsuarioRegistro usuarioRegistro) {
        // Verificar si el usuario ya existe
        if (userRepository.existsByCorreo(usuarioRegistro.getCorreo())) {
            // Devolver error 409 si el usuario ya existe
            Error error = new Error("El usuario ya existe");
            return new ResponseEntity<>(error, HttpStatus.CONFLICT);
        }

        // Validar datos de entrada 
        if (usuarioRegistro.getCorreo() == null || usuarioRegistro.getContraseña() == null || usuarioRegistro.getNombreUsuario() == null) {
            // Devolver error 400 si hay datos inválidos
            Error error = new Error("Entrada inválida, falta información");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        // Crear nuevo usuario
        Usuario nuevoUsuario = new Usuario(usuarioRegistro.getNombreUsuario(), usuarioRegistro.getCorreo(), usuarioRegistro.getContraseña());
        userRepository.save(nuevoUsuario);

        // Registro exitoso
        RegistroExitoso registroExitoso = new RegistroExitoso("Usuario registrado con éxito", nuevoUsuario.getUsuarioId());
        return new ResponseEntity<>(registroExitoso, HttpStatus.CREATED);
    }


    public ResponseEntity<?> obtenerUsuario(String usuarioId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'obtenerUsuario'");
    }

    public ResponseEntity<?> actualizarUsuario(String usuarioId, UsuarioActualizacion usuarioActualizacion) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actualizarUsuario'");
    }

    public ResponseEntity<?> eliminarUsuario(String usuarioId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eliminarUsuario'");
    }
    
}
