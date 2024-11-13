package IJI.Backend.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterInmobiliariaRequest {
    String nombreInmobiliaria;
    String correo;
    String contrasena;
    String direccion;
    String telefonoContacto;
    String ruc;
}
