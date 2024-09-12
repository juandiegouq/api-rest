package edu.uniquindio.api_rest.services;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import edu.uniquindio.api_rest.models.Error;
import edu.uniquindio.api_rest.models.Exito;
import edu.uniquindio.api_rest.models.Login;
import edu.uniquindio.api_rest.models.RecuperacionClave;
import edu.uniquindio.api_rest.models.RegistroExitoso;
import edu.uniquindio.api_rest.models.Usuario;
import edu.uniquindio.api_rest.models.UsuarioActualizacion;
import edu.uniquindio.api_rest.models.UsuarioRegistro;
import edu.uniquindio.api_rest.repositories.*;
import org.springframework.security.core.Authentication;


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

    //Obtener detalles de un usuario especifico
    public ResponseEntity<?> obtenerUsuario(String usuarioId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //Obtener usuario autenticado
        Usuario usuario = (Usuario) authentication.getPrincipal();
        if(usuario.getUsuarioId().equals(usuarioId)){ //Si el usuario está autenticado
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        }else if(userRepository.findByUsuarioId(usuarioId) == null){ //Usuario no encontrado
            Error error = new Error("Usuario no encontrado");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }else{
            Error error = new Error("No autorizado"); //Usuario no autenticado
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }
      
    }

    //Actualizar usuario 
    public ResponseEntity<?> actualizarUsuario(String usuarioId, UsuarioActualizacion usuarioActualizacion) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //Obtener usuario autenticado
        Usuario usuario = (Usuario) authentication.getPrincipal();
        //Datos faltantes
        if(usuarioActualizacion.getNombreUsuario() == null && usuarioActualizacion.getCorreo() == null && usuarioActualizacion.getContraseña() == null){
            Error error = new Error("Datos faltantes");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        //Si el usuario está autenticado
        if(usuario.getUsuarioId().equals(usuarioId)){ 
            //Actualizar datos
            usuario.setNombreUsuario(usuarioActualizacion.getNombreUsuario());
            usuario.setCorreo(usuarioActualizacion.getCorreo());
            usuario.setContraseña(usuarioActualizacion.getContraseña());
            userRepository.save(usuario);
            Exito exito = new Exito("Usuario actualizado con éxito");
            return new ResponseEntity<>(exito, HttpStatus.OK);
        }else if(userRepository.findByUsuarioId(usuarioId) == null){ //Usuario no encontrado
            Error error = new Error("Usuario no encontrado");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }else{
            Error error = new Error("No autorizado"); //Usuario no autenticado
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }
    }

    //Eliminar usuario
    public ResponseEntity<?> eliminarUsuario(String usuarioId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //Obtener usuario autenticado
        Usuario usuario = (Usuario) authentication.getPrincipal();
        if(usuario.getUsuarioId().equals(usuarioId)){ //Si el usuario está autenticado
            return new ResponseEntity<>("Usuario eliminado con éxito", HttpStatus.NO_CONTENT);
        }else if(userRepository.findByUsuarioId(usuarioId) == null){ //Usuario no encontrado
            Error error = new Error("Usuario no encontrado");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }else{
            Error error = new Error("No autorizado"); //Usuario no autenticado
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }
    }
    
}
