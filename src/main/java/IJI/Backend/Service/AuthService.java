package IJI.Backend.Service;

import IJI.Backend.Exception.ContrasenaIncorrectaException;
import IJI.Backend.Exception.CorreoNoRegistradoException;
import IJI.Backend.Exception.CorreoYaRegistradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import IJI.Backend.Entity.Agente;
import IJI.Backend.Entity.Cliente;
import IJI.Backend.Entity.Inmobiliaria;
import IJI.Backend.Jwt.JwtService;
import IJI.Backend.Repository.AgenteRepository;
import IJI.Backend.Repository.ClienteRepository;
import IJI.Backend.Repository.InmobiliariaRepository;
import IJI.Backend.Request.LoginRequest;
import IJI.Backend.Request.RegisterClienteRequest;
import IJI.Backend.Response.AuthResponse;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ClienteRepository clienteRepository;
    private final AgenteRepository agenteRepository;
    private final InmobiliariaRepository inmobiliariaRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(LoginRequest request) {
        System.out.println("Intento de inicio de sesión para el usuario: " + request.getCorreo());

        // Verificar si el correo está registrado
        UserDetails userDetails;
        try {
            userDetails = loadUserByUsername(request.getCorreo());
        } catch (UsernameNotFoundException e) {
            throw new CorreoNoRegistradoException("Correo no registrado");
        }

        // Verificar la contraseña
        if (!passwordEncoder.matches(request.getContrasena(), userDetails.getPassword())) {
            throw new ContrasenaIncorrectaException("Contraseña Incorrecta");
        }

        Long idAgente = null;

        // Obtener el idAgente si el usuario es un agente
        Optional<Agente> agenteOptional = agenteRepository.findByUsername(request.getCorreo());
        if (agenteOptional.isPresent()) {
            Agente agente = agenteOptional.get();
            idAgente = agente.getIdAgente();
        }

        // Generar token
        String token = (idAgente != null) ? jwtService.getTokenAgente(userDetails, idAgente) : jwtService.getToken(userDetails);
        String role = userDetails.getAuthorities().stream().findFirst().get().getAuthority();

        return AuthResponse.builder()
                .token(token)
                .role(role)
                .build();
    }

    public UserDetails loadUserByUsername(String correo) {
        Optional<Cliente> clienteOptional = clienteRepository.findByUsername(correo);
        if (clienteOptional.isPresent()) {
            Cliente cliente = clienteOptional.get();
            return new User(cliente.getUsername(), cliente.getPassword(), Collections.singletonList(new SimpleGrantedAuthority("Cliente")));
        }

        Optional<Agente> agenteOptional = agenteRepository.findByUsername(correo);
        if (agenteOptional.isPresent()) {
            Agente agente = agenteOptional.get();
            return new User(agente.getUsername(), agente.getPassword(), Collections.singletonList(new SimpleGrantedAuthority("Agente")));
        }

        Optional<Inmobiliaria> inmobiliariaOptional = inmobiliariaRepository.findByUsername(correo);
        if (inmobiliariaOptional.isPresent()) {
            Inmobiliaria inmobiliaria = inmobiliariaOptional.get();
            return new User(inmobiliaria.getUsername(), inmobiliaria.getPassword(), Collections.singletonList(new SimpleGrantedAuthority("Inmobiliaria")));
        }
        throw new UsernameNotFoundException("Usuario no encontrado con correo: " + correo);
    }

    // Método para verificar si el correo ya está registrado en cualquiera de los roles
    public void verificarCorreoUnico(String correo) {
        boolean correoRegistrado = clienteRepository.findByUsername(correo).isPresent() ||
                agenteRepository.findByUsername(correo).isPresent() ||
                inmobiliariaRepository.findByUsername(correo).isPresent();

        if (correoRegistrado) {
            throw new CorreoYaRegistradoException("El correo ya está registrado");
        }
    }

    //Método para registar al cliente
    public AuthResponse registerCliente(RegisterClienteRequest request) {

        // Verificar que el correo no esté registrado en ningún rol
        verificarCorreoUnico(request.getCorreo());

        // Registrar al cliente si el correo no está registrado
        Cliente cliente = new Cliente();
        cliente.setUsername(request.getCorreo());
        cliente.setPassword(passwordEncoder.encode(request.getContrasena()));
        cliente.setRole("Cliente");
        cliente.setNombre(request.getNombre());
        cliente.setApellido(request.getApellido());
        cliente.setDni(request.getDni());
        cliente.setTelefono(request.getTelefono());
        clienteRepository.save(cliente);

        String token = jwtService.getToken(cliente);
        return AuthResponse.builder()
                .token(token)
                .role("Cliente")
                .build();
    }
}

