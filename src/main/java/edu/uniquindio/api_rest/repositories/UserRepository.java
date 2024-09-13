package edu.uniquindio.api_rest.repositories;

import java.util.*;
import edu.uniquindio.api_rest.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Usuario, Integer> {
    // Método para verificar si un usuario con el correo dado ya existe
    boolean existsByCorreo(String correo);
    //Encontrar por el email al usuario
    Optional<Usuario> findByCorreo(String correo);
    //Encontrar por el usuarioId
    Optional<Usuario> findByUsuarioId(int usuarioId);
    Optional<Usuario> findBynombreUsuario(String nombreUsuario);
    

}


