package edu.uniquindio.api_rest.services;

import edu.uniquindio.api_rest.models.Error;
import edu.uniquindio.api_rest.models.Login;
import edu.uniquindio.api_rest.models.RecuperacionClave;
import edu.uniquindio.api_rest.models.RegistroExitoso;
import edu.uniquindio.api_rest.models.Token;
import edu.uniquindio.api_rest.models.Usuario;
import edu.uniquindio.api_rest.models.UsuarioActualizacion;
import edu.uniquindio.api_rest.models.UsuarioRegistro;

import org.apache.catalina.connector.Response;
import org.aspectj.bridge.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import edu.uniquindio.api_rest.repositories.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
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


    public ResponseEntity<?> authenticate(Login input) {
        try {
            // Validar si el formato del JSON es correcto
            if (input.getNombreUsuario() == null || input.getContraseña() == null) {
                //Formato incorrecto 
                Error error = new Error("Entrada inválida, falta información");
                return new ResponseEntity<>(error.getMessage(), HttpStatus.BAD_REQUEST);
            }

            // Autenticar el usuario
         Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(input.getNombreUsuario(), input.getContraseña())
            );
                // Si la autenticación es exitosa, generar el token JWT
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String jwt = JwtService.generateToken((UserDetails) authentication.getPrincipal());
                Token token = new Token(jwt);
                return new ResponseEntity<>(token, HttpStatus.OK);
                
    
            } catch (BadCredentialsException e) {
                // Credenciales inválidas
                Error error = new Error("Credenciales no validas");
                return new ResponseEntity<>(error.getMessage(), HttpStatus.UNAUTHORIZED);
            }
        }

        
    public ResponseEntity<?> recuperacionClave(RecuperacionClave recuperacionClave) {
        // TODO Auto-generated method stub
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    }




