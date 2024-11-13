package IJI.Backend.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartamentoDto extends PropiedadDto{

    @NotNull
    @NotBlank(message = "La cantidad de pisos no debe estar en blanco")
    private int pisos;

    @NotNull
    @NotBlank(message = "La cantidad de interiores no debe estar en blanco")
    private int interior;

    @NotNull
    private boolean ascensor;

    @NotNull
    private boolean areasComunes;

    @NotNull
    @NotBlank(message = "Las áreas comunes no debe estar en blanco")
    private String areasComunesEspecificas;

    private List<String> fotosUrls;

    private Long idAgente;

    public DepartamentoDto(String latitud, String longitud, String pais, String region, String provincia, String distrito, String direccion, String descripcion, String otrasComodidades, String tipoPropiedad, double areaTerreno, double costoTotal, double costoInicial, boolean cochera, int cantBanos, int cantDormitorios, int cantCochera, int pisos, int interior, boolean ascensor, boolean areasComunes, String areasComunesEspecificas, List<String> fotosUrls, Long idAgente) {
        super(latitud, longitud, pais, region, provincia, distrito, direccion, descripcion, otrasComodidades, tipoPropiedad, areaTerreno, costoTotal, costoInicial, cochera, cantBanos, cantDormitorios, cantCochera, idAgente);
        this.pisos = pisos;
        this.interior = interior;
        this.ascensor = ascensor;
        this.areasComunes = areasComunes;
        this.areasComunesEspecificas = areasComunesEspecificas;
        this.fotosUrls = fotosUrls;
        this.idAgente = idAgente;
    }
}
