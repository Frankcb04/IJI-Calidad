package IJI.Backend.Controller;

import IJI.Backend.Entity.Propiedad;
import IJI.Backend.Service.PropiedadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/propiedad")
@RequiredArgsConstructor
public class PropiedadController {

    private final PropiedadService propiedadService;

    @GetMapping("/listarPropiedades")
    public ResponseEntity<List<Propiedad>> listarPropiedades() {
        List<Propiedad> propiedades =propiedadService.listarPropiedades();
        return ResponseEntity.ok(propiedades);
    }

    @GetMapping("/listarPropiedad/{id}")
    public ResponseEntity<Propiedad> obtenerPropiedadPorId(@PathVariable Long id) {
        Propiedad propiedad = propiedadService.obtenerPropiedadPorId(id);
        return ResponseEntity.ok(propiedad);
    }

    @GetMapping("/listarPropiedadesAgente/{idAgente}")
    public ResponseEntity<List<Propiedad>> listarPropiedadesAgente(@PathVariable Long idAgente) {
        List<Propiedad> propiedad = propiedadService.listarPropiedadesPorAgente(idAgente);
        if (propiedad.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(propiedad, HttpStatus.OK);
    }
}