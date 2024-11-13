package IJI.Backend.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table (name= "inmobiliaria")
public class Inmobiliaria implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inmobiliaria")
    private Long idInmobiliaria;

    @Column(name = "nombre_inmobiliaria")
    private String nombreInmobiliaria;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "correo")
    private String username;

    @Column(name = "contrasena")
    private String password;

    @Column(name = "ruc")
    private String ruc;

    @Column(name = "telefono_contacto")
    private String telefonoContacto;

    private String role;

    @Column(name = "id_agente")
    private Long idAgente;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @OneToMany(mappedBy = "inmobiliaria", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Agente> agentes = new ArrayList<>();

    // Add agent to list
    public void addAgente(Agente agente) {
        agentes.add(agente);
        agente.setInmobiliaria(this);
    }
}
