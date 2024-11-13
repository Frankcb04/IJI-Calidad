package IJI.Backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import IJI.Backend.Entity.Agente;
import IJI.Backend.Entity.Cliente;
import IJI.Backend.Entity.Inmobiliaria;
import IJI.Backend.Repository.AgenteRepository;
import IJI.Backend.Repository.ClienteRepository;
import IJI.Backend.Repository.InmobiliariaRepository;

import java.util.Optional;

@Service
@Primary
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AgenteRepository agenteRepository;

    @Autowired
    private InmobiliariaRepository inmobiliariaRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Buscar en la entidad Cliente
        Optional<Cliente> cliente = clienteRepository.findByUsername(username);
        if (cliente.isPresent()) {
            return cliente.get();
        }

        // Buscar en la entidad Agente
        Optional<Agente> agente = agenteRepository.findByUsername(username);
        if (agente.isPresent()) {
            return agente.get();
        }

        // Buscar en la entidad Inmobiliaria
        Optional<Inmobiliaria> inmobiliaria = inmobiliariaRepository.findByUsername(username);
        if (inmobiliaria.isPresent()) {
            return inmobiliaria.get();
        }

        // Si no se encuentra el usuario en ninguna entidad, lanzar una excepción
        throw new UsernameNotFoundException("Usuario no encontrado");
    }
}

