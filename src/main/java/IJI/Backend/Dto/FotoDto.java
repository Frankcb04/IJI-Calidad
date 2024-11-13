package IJI.Backend.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FotoDto {

    @NotNull
    @NotBlank(message = "La dirección URL de la foto no debe estar en blanco")
    private String nombreFoto;
}
