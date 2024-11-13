package IJI.Backend.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    //Correo ya registrado al registrar cuenta
    @ExceptionHandler(CorreoYaRegistradoException.class)
    public ResponseEntity<String> handleCorreoYaRegistradoException(CorreoYaRegistradoException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    //Correo no registrado al iniciar sesión
    @ExceptionHandler(CorreoNoRegistradoException.class)
    public ResponseEntity<String> handleCorreoNoRegistradoException(CorreoNoRegistradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    //Contraseña incorrecta al iniciar sesión
    @ExceptionHandler(ContrasenaIncorrectaException.class)
    public ResponseEntity<String> handleContrasenaIncorrectaException(ContrasenaIncorrectaException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    //Datos asociados de propiedades al eliminar un agente
    @ExceptionHandler(DatosAsociadosException.class)
    public ResponseEntity<String> handleDatosAsociadosException(DatosAsociadosException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}