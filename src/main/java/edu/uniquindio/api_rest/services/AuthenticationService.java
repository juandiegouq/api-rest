package edu.uniquindio.api_rest.services;

import edu.uniquindio.api_rest.models.Error;
import edu.uniquindio.api_rest.models.Login;
import edu.uniquindio.api_rest.models.RecuperacionClave;
import edu.uniquindio.api_rest.models.RegistroExitoso;
import edu.uniquindio.api_rest.models.Token;
import edu.uniquindio.api_rest.models.Usuario;
import edu.uniquindio.api_rest.models.UsuarioActualizacion;
import edu.uniquindio.api_rest.models.UsuarioRegistro;

import java.util.Optional;

import org.apache.catalina.connector.Response;
import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

    private JavaMailSender mailSender;
    
    @Value("${app.recovery.token.expiry}")
    private long recoveryTokenExpiry;

    @Value("${app.base.url}")
    private String baseUrl;

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



    //Recuperar contraseña
    public ResponseEntity<?> recuperarContraseña(RecuperacionClave recuperacionClave) {
            //Formato no valido, 400
            if (recuperacionClave.getCorreo() == null) {
                Error error = new Error("Correo electrónico no proporcionado");
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            }

            //Correo no encontrado, 404
            if(userRepository.findByCorreo(recuperacionClave.getCorreo()) == null){
                Error error = new Error("No se encontró el usuario");
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
            }

            Optional <Usuario> usuarioo = userRepository.findByCorreo(recuperacionClave.getCorreo());
            Usuario usuario = usuarioo.get();
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(recuperacionClave.getCorreo());
            message.setSubject("Contraseña olvidada");
            message.setText(usuario.getContraseña());
            mailSender.send(message);
            return new ResponseEntity<>("Correo de recuperación enviado con éxito", HttpStatus.OK);
    }
 
   
    }




