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
public class CasaDto extends PropiedadDto{

    @NotNull
    @NotBlank(message = "La cantidad de pisos no debe estar en blanco")
    private int cantPisos;

    @NotNull
    @NotBlank(message = "El área del jardín no debe estar en blanco")
    private int areaJardin;

    @NotNull
    private boolean jardin;

    @NotNull
    private boolean atico;

    @NotNull
    private boolean sotano;

    private List<String> fotosUrls;

    private Long idAgente;

    public CasaDto(String latitud, String longitud, String pais, String region, String provincia, String distrito, String direccion, String descripcion, String otrasComodidades, String tipoPropiedad, double areaTerreno, double costoTotal, double costoInicial, boolean cochera, int cantBanos, int cantDormitorios, int cantCochera, boolean sotano, double areaJardin, boolean atico, boolean jardin, int cantPisos, List<String> fotosUrls, Long idAgente) {
        super(latitud, longitud, pais, region, provincia, distrito, direccion, descripcion, otrasComodidades, tipoPropiedad, areaTerreno, costoTotal, costoInicial, cochera, cantBanos, cantDormitorios, cantCochera, idAgente);
        this.sotano = sotano;
        this.areaJardin = (int) areaJardin;
        this.atico = atico;
        this.jardin = jardin;
        this.cantPisos = cantPisos;
        this.fotosUrls = fotosUrls;
        this.idAgente = idAgente;
    }
}
