package IJI.Backend.Controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import IJI.Backend.Dto.CasaDto;
import IJI.Backend.Dto.DepartamentoDto;
import IJI.Backend.Entity.Agente;
import IJI.Backend.Jwt.JwtService;
import IJI.Backend.Service.AgenteService;
import IJI.Backend.Storage.StorageService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/agente")
@AllArgsConstructor
public class AgenteController {

    private final AgenteService agenteService;
    private final StorageService storageService;
    private final JwtService jwtService;

    //Listar datos del agente por token
    @GetMapping("/listarAgenteToken")
    public ResponseEntity<Agente> obtenerDatosAgente(@RequestHeader("Authorization") String token) {
        String tokenSinBearer = token.replace("Bearer ", "");
        Agente agente = agenteService.obtenerAgentePorToken(tokenSinBearer);
        return ResponseEntity.ok(agente);
    }

    //CASA

    //Agregar una casa
    @PostMapping("/agregarCasa")
    public ResponseEntity<String> agregarCasa(
            @RequestParam("latitud") String latitud,
            @RequestParam("longitud") String longitud,
            @RequestParam("pais") String pais,
            @RequestParam("region") String region,
            @RequestParam("provincia") String provincia,
            @RequestParam("distrito") String distrito,
            @RequestParam("direccion") String direccion,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("otrasComodidades") String otrasComodidades,
            @RequestParam("tipoPropiedad") String tipoPropiedad,
            @RequestParam("areaTerreno") double areaTerreno,
            @RequestParam("costoTotal") double costoTotal,
            @RequestParam("costoInicial") double costoInicial,
            @RequestParam("cochera") boolean cochera,
            @RequestParam("cantBanos") int cantBanos,
            @RequestParam("cantDormitorios") int cantDormitorios,
            @RequestParam("cantCochera") int cantCochera,
            @RequestParam("sotano") boolean sotano,
            @RequestParam("areaJardin") double areaJardin,
            @RequestParam("atico") boolean atico,
            @RequestParam("jardin") boolean jardin,
            @RequestParam("cantPisos") int cantPisos,
            @RequestParam("fotos") List<MultipartFile> fotos,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {

        // Obtener el token JWT desde el encabezado de autorización
        String token = authorizationHeader.substring(7); // Remueve "Bearer " del encabezado

        // Obtener el ID del agente desde el token JWT
        Long idAgente = jwtService.getIdAgenteFromToken(token);

        // Subir las fotos y obtener las URLs
        List<String> fotoUrls = fotos.stream()
                .map(storageService::store)
                .map(filename -> ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/fotos/")
                        .path(filename)
                        .toUriString())
                .collect(Collectors.toList());

        // Crear y guardar la casa usando las URLs de las fotos
        CasaDto casaDto = new CasaDto(latitud, longitud, pais, region, provincia, distrito, direccion, descripcion, otrasComodidades, tipoPropiedad, areaTerreno, costoTotal, costoInicial, cochera, cantBanos, cantDormitorios, cantCochera, sotano, areaJardin, atico, jardin, cantPisos, fotoUrls, idAgente);
        agenteService.agregarCasa(casaDto, token);
        return ResponseEntity.ok("Casa agregada con éxito");
    }

    // Modificar datos de la casa
    @PutMapping("/modificarCasa/{id}")
    public ResponseEntity<String> modificarCasa(
            @PathVariable Long id,
            @RequestParam("latitud") String latitud,
            @RequestParam("longitud") String longitud,
            @RequestParam("pais") String pais,
            @RequestParam("region") String region,
            @RequestParam("provincia") String provincia,
            @RequestParam("distrito") String distrito,
            @RequestParam("direccion") String direccion,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("otrasComodidades") String otrasComodidades,
            @RequestParam("tipoPropiedad") String tipoPropiedad,
            @RequestParam("areaTerreno") double areaTerreno,
            @RequestParam("costoTotal") double costoTotal,
            @RequestParam("costoInicial") double costoInicial,
            @RequestParam("cochera") boolean cochera,
            @RequestParam("cantBanos") int cantBanos,
            @RequestParam("cantDormitorios") int cantDormitorios,
            @RequestParam("cantCochera") int cantCochera,
            @RequestParam("sotano") boolean sotano,
            @RequestParam("areaJardin") double areaJardin,
            @RequestParam("atico") boolean atico,
            @RequestParam("jardin") boolean jardin,
            @RequestParam("cantPisos") int cantPisos,
            @RequestParam("fotos") List<MultipartFile> fotos,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {

        // Obtener el token JWT desde el encabezado de autorización
        String token = authorizationHeader.substring(7); // Remueve "Bearer " del encabezado

        // Obtener el ID del agente desde el token JWT
        Long idAgente = jwtService.getIdAgenteFromToken(token);

        // Subir las fotos y obtener las URLs
        List<String> fotoUrls = fotos.stream()
                .map(storageService::store)
                .map(filename -> ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/fotos/")
                        .path(filename)
                        .toUriString())
                .collect(Collectors.toList());

        // Crear y modificar la casa usando las URLs de las fotos
        CasaDto casaDto = new CasaDto(latitud, longitud, pais, region, provincia, distrito, direccion, descripcion, otrasComodidades, tipoPropiedad, areaTerreno, costoTotal, costoInicial, cochera, cantBanos, cantDormitorios, cantCochera, sotano, areaJardin, atico, jardin, cantPisos, fotoUrls, idAgente);
        agenteService.modificarCasa(id, casaDto);
        return ResponseEntity.ok("Casa modificada con éxito");
    }

    //Elimianr una casa
    @DeleteMapping("/eliminarCasa/{id}")
    public ResponseEntity<String> eliminarCasa(@PathVariable Long id) {
        try {
            agenteService.eliminarCasa(id);
            return ResponseEntity.ok("Casa eliminada correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //DEPARTAMENTO

    //Agregar un departamento
    @PostMapping("/agregarDepartamento")
    public ResponseEntity<String> agregarDepartamento(
            @RequestParam("latitud") String latitud,
            @RequestParam("longitud") String longitud,
            @RequestParam("pais") String pais,
            @RequestParam("region") String region,
            @RequestParam("provincia") String provincia,
            @RequestParam("distrito") String distrito,
            @RequestParam("direccion") String direccion,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("otrasComodidades") String otrasComodidades,
            @RequestParam("tipoPropiedad") String tipoPropiedad,
            @RequestParam("areaTerreno") double areaTerreno,
            @RequestParam("costoTotal") double costoTotal,
            @RequestParam("costoInicial") double costoInicial,
            @RequestParam("cochera") boolean cochera,
            @RequestParam("cantBanos") int cantBanos,
            @RequestParam("cantDormitorios") int cantDormitorios,
            @RequestParam("cantCochera") int cantCochera,
            @RequestParam("pisos") int pisos,
            @RequestParam("interior") int interior,
            @RequestParam("ascensor") boolean ascensor,
            @RequestParam("areasComunes") boolean areasComunes,
            @RequestParam("areasComunesEspecificas") String areasComunesEspecificas,
            @RequestParam("fotos") List<MultipartFile> fotos,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {

        // Obtener el token JWT desde el encabezado de autorización
        String token = authorizationHeader.substring(7); // Remueve "Bearer " del encabezado

        // Obtener el ID del agente desde el token JWT
        Long idAgente = jwtService.getIdAgenteFromToken(token);

        // Subir las fotos y obtener las URLs
        List<String> fotoUrls = fotos.stream()
                .map(storageService::store)
                .map(filename -> ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/fotos/")
                        .path(filename)
                        .toUriString())
                .collect(Collectors.toList());

        // Crear y guardar el departamento usando las URLs de las fotos
        DepartamentoDto departamentoDto = new DepartamentoDto(latitud, longitud, pais, region, provincia, distrito, direccion, descripcion, otrasComodidades, tipoPropiedad, areaTerreno, costoTotal, costoInicial, cochera, cantBanos, cantDormitorios, cantCochera, pisos, interior, ascensor, areasComunes, areasComunesEspecificas, fotoUrls, idAgente);
        agenteService.agregarDepartamento(departamentoDto);
        return ResponseEntity.ok("Departamento agregado con éxito");
    }

    // Modificar Departamento
    @PutMapping("/modificarDepartamento/{id}")
    public ResponseEntity<String> modificarDepartamento(
            @PathVariable Long id,
            @RequestParam("latitud") String latitud,
            @RequestParam("longitud") String longitud,
            @RequestParam("pais") String pais,
            @RequestParam("region") String region,
            @RequestParam("provincia") String provincia,
            @RequestParam("distrito") String distrito,
            @RequestParam("direccion") String direccion,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("otrasComodidades") String otrasComodidades,
            @RequestParam("tipoPropiedad") String tipoPropiedad,
            @RequestParam("areaTerreno") double areaTerreno,
            @RequestParam("costoTotal") double costoTotal,
            @RequestParam("costoInicial") double costoInicial,
            @RequestParam("cochera") boolean cochera,
            @RequestParam("cantBanos") int cantBanos,
            @RequestParam("cantDormitorios") int cantDormitorios,
            @RequestParam("cantCochera") int cantCochera,
            @RequestParam("pisos") int pisos,
            @RequestParam("interior") int interior,
            @RequestParam("ascensor") boolean ascensor,
            @RequestParam("areasComunes") boolean areasComunes,
            @RequestParam("areasComunesEspecificas") String areasComunesEspecificas,
            @RequestParam("fotos") List<MultipartFile> fotos,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {

        // Obtener el token JWT desde el encabezado de autorización
        String token = authorizationHeader.substring(7); // Remueve "Bearer " del encabezado

        // Obtener el ID del agente desde el token JWT
        Long idAgente = jwtService.getIdAgenteFromToken(token);

        // Subir las fotos y obtener las URLs
        List<String> fotoUrls = fotos.stream()
                .map(storageService::store)
                .map(filename -> ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/fotos/")
                        .path(filename)
                        .toUriString())
                .collect(Collectors.toList());

        // Crear y modificar el departamento usando las URLs de las fotos
        DepartamentoDto departamentoDto = new DepartamentoDto(latitud, longitud, pais, region, provincia, distrito, direccion, descripcion, otrasComodidades, tipoPropiedad, areaTerreno, costoTotal, costoInicial, cochera, cantBanos, cantDormitorios, cantCochera, pisos, interior, ascensor, areasComunes, areasComunesEspecificas, fotoUrls, idAgente);
        agenteService.modificarDepartamento(id, departamentoDto);
        return ResponseEntity.ok("Departamento modificado con éxito");
    }

    //Eliminar los datos del departamento
    @DeleteMapping("/eliminarDepartamento/{id}")
    public ResponseEntity<String> eliminarDepartamento(@PathVariable Long id) {
        try {
            agenteService.eliminarDepartamento(id);
            return ResponseEntity.ok("Departamento eliminado correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
