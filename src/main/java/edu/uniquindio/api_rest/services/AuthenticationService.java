package edu.uniquindio.api_rest.services;

import edu.uniquindio.api_rest.models.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import edu.uniquindio.api_rest.repositories.UserRepository;

public class AuthenticationService {
    private final UserRepository userRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
        UserRepository userRepository,
        AuthenticationManager authenticationManager,
        PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario signup(UsuarioRegistro input) {
        Usuario user = new Usuario();
        user.setNombreUsuario(input.getNombreUsuario());
        user.setCorreo(input.getCorreo());
        user.setContraseña(passwordEncoder.encode(input.getContraseña()));
        return userRepository.save(user);
    }

    public Usuario authenticate(Login input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getNombreUsuario(),
                        input.getContraseña()
                )
        );

        return userRepository.findByEmail(input.getNombreUsuario())
                .orElseThrow();
    }
}
