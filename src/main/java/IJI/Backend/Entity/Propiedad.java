package IJI.Backend.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "propiedad")
@DiscriminatorValue("Propiedad")
@Inheritance(strategy = InheritanceType.JOINED)
public class Propiedad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_propiedad")
    private Long idPropiedad;

    //Dirección de las propiedades

    @Column(name = "latitud")
    private String latitud;

    @Column(name = "longitud")
    private String longitud;

    @Column(name = "pais")
    private String pais;

    @Column(name = "region")
    private String region;

    @Column(name = "provincia")
    private String provincia;

    @Column(name = "distrito")
    private String distrito;

    @Column(name = "direccion")
    private String direccion;

    //Descripción de las propiedades

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "otras_comodidades")
    private String otrasComodidades;

    @Column(name = "tipo_propiedad")
    private String tipoPropiedad;

    @Column(name = "area_terreno")
    private double areaTerreno;

    @Column(name = "costo_total")
    private double costoTotal;

    @Column(name = "costo_inicial")
    private double costoInicial;

    @Column(name = "cochera")
    private boolean cochera;

    @Column(name = "cant_banos")
    private int cantBanos;

    @Column(name = "cant_dormitorios")
    private int cantDormitorios;

    @Column(name = "cant_cochera")
    private int cantCochera;

    @Column(name = "id_agente")
    private Long idAgente;

    @OneToMany(mappedBy = "propiedad", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Foto> fotos;
}
