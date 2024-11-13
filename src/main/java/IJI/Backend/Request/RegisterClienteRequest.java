package IJI.Backend.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterClienteRequest {
    String nombre;
    String apellido;
    String correo;
    String contrasena;
    String telefono;
    String dni;
}