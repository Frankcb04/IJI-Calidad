package IJI.Backend.Service;

import IJI.Backend.Exception.CorreoYaRegistradoException;
import IJI.Backend.Exception.DatosAsociadosException;
import IJI.Backend.Repository.PropiedadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import IJI.Backend.Entity.Agente;
import IJI.Backend.Entity.Inmobiliaria;
import IJI.Backend.Jwt.JwtService;
import IJI.Backend.Repository.AgenteRepository;
import IJI.Backend.Repository.InmobiliariaRepository;
import IJI.Backend.Request.RegisterAgenteRequest;
import IJI.Backend.Request.RegisterInmobiliariaRequest;
import IJI.Backend.Response.AuthResponse;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InmobiliariaService {

    private final InmobiliariaRepository inmobiliariaRepository;
    private final PropiedadRepository propiedadRepository;
    private final AgenteRepository agenteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthService authService;

    //Agregar Inmobiliaria
    public AuthResponse agregarInmobiliaria(RegisterInmobiliariaRequest request) {

        // Verificar que el correo no esté registrado en ningún rol
        authService.verificarCorreoUnico(request.getCorreo());

        System.out.println("Solicitud para agregar inmobiliaria recibida: " + request);
        Inmobiliaria inmobiliaria = new Inmobiliaria();
        inmobiliaria.setNombreInmobiliaria(request.getNombreInmobiliaria());
        inmobiliaria.setRole("Inmobiliaria");
        inmobiliaria.setUsername(request.getCorreo());
        inmobiliaria.setPassword(passwordEncoder.encode(request.getContrasena()));
        inmobiliaria.setRuc(request.getRuc());
        inmobiliaria.setTelefonoContacto(request.getTelefonoContacto());
        inmobiliaria.setDireccion(request.getDireccion());
        inmobiliariaRepository.save(inmobiliaria);

        String token = jwtService.getToken(inmobiliaria);
        return AuthResponse.builder()
                .token(token)
                .role("Inmobiliaria")
                .build();
    }

    // Listar datos de la inmobiliaria por token
    public Inmobiliaria obtenerInmobiliariaPorToken(String token) {
        String correo = jwtService.getUsernameFromToken(token);
        return inmobiliariaRepository.findByUsername(correo)
                .orElseThrow(() -> new RuntimeException("Inmobiliaria no encontrada para el token proporcionado"));
    }

    //AGENTE

    //Listar agentes por inmobiliaria
    public List<Agente> listarAgentesInmobiliaria(String nombreInmobiliaria) {
        return agenteRepository.findByNombreInmobiliaria(nombreInmobiliaria);
    }

    //Listar a todos los agentes
    public List<Agente> listarAgentes() {
        return agenteRepository.findAll();
    }

    // Obtener agente por ID
    public Agente obtenerAgentePorId(Long id) {
        return agenteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Agente no encontrado con el ID: "+ id));
    }

    //Agregar a un agente
    public AuthResponse agregarAgente(RegisterAgenteRequest request) {
        System.out.println("Solicitud para agregar agentes recibida: " + request);

        // Verificar que el correo no esté registrado en ningún rol
        authService.verificarCorreoUnico(request.getCorreo());

        // Verificar si el correo ya está registrado en la base de datos
        Optional<Agente> existingAgente = agenteRepository.findByUsername(request.getCorreo());
        if (existingAgente.isPresent()) {
            throw new CorreoYaRegistradoException("El correo ya está registrado");
        }

        // Buscar la inmobiliaria por nombre
        Inmobiliaria inmobiliaria = inmobiliariaRepository.findByNombreInmobiliaria(request.getNombreInmobiliaria())
                .orElseThrow(() -> new UsernameNotFoundException("Inmobiliaria no encontrada"));

        // Crear el nuevo agente
        Agente agente = new Agente();
        agente.setNombre(request.getNombre());
        agente.setApellido(request.getApellido());
        agente.setRole("Agente");
        agente.setInmobiliaria(inmobiliaria);
        inmobiliaria.addAgente(agente);
        agente.setUsername(request.getCorreo());
        agente.setPassword(passwordEncoder.encode(request.getContrasena()));
        agente.setDni(request.getDni());
        agente.setTelefono(request.getTelefono());
        agente.setNombreInmobiliaria(request.getNombreInmobiliaria());
        agenteRepository.save(agente);

        String token = jwtService.getToken(agente);
        return AuthResponse.builder()
                .token(token)
                .role("Agente")
                .build();
    }

    //Modificar datos del agente
    public Agente modificarAgente(Long id, RegisterAgenteRequest request) {
        Agente agente = agenteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Agente no encontrado con el ID: " + id));

        // Verificar si el correo ya está registrado en otro agente
        Optional<Agente> existingAgente = agenteRepository.findByUsername(request.getCorreo());
        if (existingAgente.isPresent() && !existingAgente.get().getIdAgente().equals(id)) {
            throw new CorreoYaRegistradoException("El correo ya está registrado");
        }

        // Actualizar los datos del agente
        agente.setNombre(request.getNombre());
        agente.setApellido(request.getApellido());
        agente.setUsername(request.getCorreo());
        agente.setDni(request.getDni());
        agente.setTelefono(request.getTelefono());
        agente.setNombreInmobiliaria(request.getNombreInmobiliaria());

        if (request.getContrasena() != null && !request.getContrasena().isEmpty()) {
            agente.setPassword(passwordEncoder.encode(request.getContrasena()));
        }

        agenteRepository.save(agente);
        return agente;
    }

    //Eliminar al agente
    public ResponseEntity<?> eliminarAgente(Long id) {
        Optional<Agente> agenteOptional = agenteRepository.findById(id);
        if (agenteOptional.isPresent()) {
            Agente agente = agenteOptional.get();
            // Verificar si el agente tiene propiedades activas
            boolean tienePropiedades = propiedadRepository.existsByIdAgente(agente.getIdAgente());
            if (tienePropiedades) {
                throw new DatosAsociadosException("El agente tiene datos asociados (propiedades activas) y no puede ser eliminado sin reasignación.");
            }
            // Si no tiene propiedades, proceder con la eliminación
            agenteRepository.delete(agente);
            return ResponseEntity.status(HttpStatus.OK).body("Agente eliminado");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El agente no se ha encontrado");
        }
    }
}
