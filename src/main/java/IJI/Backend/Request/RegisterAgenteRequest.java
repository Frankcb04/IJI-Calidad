package IJI.Backend.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterAgenteRequest {
    String nombre;
    String apellido;
    String correo;
    String contrasena;
    String nombreInmobiliaria;
    String telefono;
    String dni;
}