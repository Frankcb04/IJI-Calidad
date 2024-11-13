package IJI.Backend.Service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import IJI.Backend.Dto.CasaDto;
import IJI.Backend.Dto.DepartamentoDto;
import IJI.Backend.Dto.PropiedadDto;
import IJI.Backend.Entity.Agente;
import IJI.Backend.Jwt.JwtService;
import IJI.Backend.Entity.Casa;
import IJI.Backend.Entity.Departamento;
import IJI.Backend.Entity.Foto;
import IJI.Backend.Entity.Propiedad;
import IJI.Backend.Repository.PropiedadRepository;
import IJI.Backend.Repository.AgenteRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AgenteService {

    private final PropiedadRepository propiedadRepository;
    private final AgenteRepository agenteRepository;
    private final JwtService jwtService;


    //Listar datos del agente por token
    public Agente obtenerAgentePorToken(String token) {
        String correo = jwtService.getUsernameFromToken(token);
        return agenteRepository.findByUsername(correo)
                .orElseThrow(() -> new RuntimeException("Agente no encontrado con el token proporcionado"));
    }

    //Convertir la propeidad en base al tipo de propiedad
    private <T extends Propiedad> T convertirPropiedad(PropiedadDto propiedadDto, T propiedad) {
        propiedad.setLatitud(propiedadDto.getLatitud());
        propiedad.setLongitud(propiedadDto.getLongitud());
        propiedad.setPais(propiedadDto.getPais());
        propiedad.setRegion(propiedadDto.getRegion());
        propiedad.setProvincia(propiedadDto.getProvincia());
        propiedad.setDistrito(propiedadDto.getDistrito());
        propiedad.setDireccion(propiedadDto.getDireccion());
        propiedad.setDescripcion(propiedadDto.getDescripcion());
        propiedad.setOtrasComodidades(propiedadDto.getOtrasComodidades());
        propiedad.setTipoPropiedad(propiedadDto.getTipoPropiedad());
        propiedad.setAreaTerreno(propiedadDto.getAreaTerreno());
        propiedad.setCostoTotal(propiedadDto.getCostoTotal());
        propiedad.setCostoInicial(propiedadDto.getCostoInicial());
        propiedad.setCochera(propiedadDto.isCochera());
        propiedad.setCantBanos(propiedadDto.getCantBanos());
        propiedad.setCantDormitorios(propiedadDto.getCantDormitorios());
        propiedad.setCantCochera(propiedadDto.getCantCochera());
        propiedad.setIdAgente(propiedadDto.getIdAgente());

        // Asignar fotos
        List<Foto> fotos = convertirFotos(propiedadDto.getFotosUrls(), propiedad);
        propiedad.setFotos(fotos);

        return propiedad;
    }

    // Lista de fotos para las propiedades
    private List<Foto> convertirFotos(List<String> fotosUrls, Propiedad propiedad) {
        return fotosUrls.stream()
                .map(fotoUrl -> {
                    Foto foto = new Foto();
                    foto.setNombreFoto(fotoUrl);
                    foto.setPropiedad(propiedad);
                    return foto;
                })
                .collect(Collectors.toList());
    }

    public Long extraerIdAgenteToken(String token) {
        String jwtToken = token.substring(7); // Remover "Bearer " del inicio del token
        return jwtService.getIdAgenteFromToken(jwtToken);
    }

    //CASA

    // Agregar Casa
    public void agregarCasa(CasaDto casaDto, String token) {
        Long idAgente = jwtService.getIdAgenteFromToken(token);
        Casa casa = convertirPropiedad(casaDto, new Casa());

        //Atributos de la propiedad casa
        casa.setSotano(casaDto.isSotano());
        casa.setAreaJardin(casaDto.getAreaJardin());
        casa.setAtico(casaDto.isAtico());
        casa.setJardin(casaDto.isJardin());
        casa.setCantPisos(casaDto.getCantPisos());
        casa.setIdAgente(idAgente);
        propiedadRepository.save(casa);
    }

    //Modificar datos de la casa
    public void modificarCasa(Long id, CasaDto casaDto) {
        Casa casa = (Casa) propiedadRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Casa no encontrada con el ID: " + id));

        //Convierte y actualiza los campos de Propiedad
        convertirPropiedad(casaDto, casa);

        // Atributos específicos de Casa
        casa.setSotano(casaDto.isSotano());
        casa.setAreaJardin(casaDto.getAreaJardin());
        casa.setAtico(casaDto.isAtico());
        casa.setJardin(casaDto.isJardin());
        casa.setCantPisos(casaDto.getCantPisos());
        casa.setIdAgente(casaDto.getIdAgente());
        propiedadRepository.save(casa);
    }

    //Eliminar la casa del repositorio
    public void eliminarCasa(Long id) {
        Casa casa = (Casa) propiedadRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Casa no encontrada"));
        propiedadRepository.delete(casa);
    }

    //DEPARTAMENTO

    // Agregar Departamento
    public void agregarDepartamento(DepartamentoDto departamentoDto) {
        Departamento departamento = convertirPropiedad(departamentoDto, new Departamento());

        //Atributos de la propiedad departamento
        departamento.setPisos(departamentoDto.getPisos());
        departamento.setInterior(departamentoDto.getInterior());
        departamento.setAscensor(departamentoDto.isAscensor());
        departamento.setAreasComunes(departamentoDto.isAreasComunes());
        departamento.setAreasComunesEspecificas(departamentoDto.getAreasComunesEspecificas());
        departamento.setIdAgente(departamento.getIdAgente());
        propiedadRepository.save(departamento);
    }

    //Modificar datos del departamento
    public void modificarDepartamento(Long id, DepartamentoDto departamentoDto) {
        Departamento departamento = (Departamento) propiedadRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Departamento no encontrado con el ID: " + id));

        //Convierte y actualiza los campos de Propiedad
        convertirPropiedad(departamentoDto, departamento);

        // Atributos específicos de Departamento
        departamento.setPisos(departamentoDto.getPisos());
        departamento.setInterior(departamentoDto.getInterior());
        departamento.setAscensor(departamentoDto.isAscensor());
        departamento.setAreasComunes(departamentoDto.isAreasComunes());
        departamento.setAreasComunesEspecificas(departamentoDto.getAreasComunesEspecificas());
        departamento.setIdAgente(departamento.getIdAgente());
        propiedadRepository.save(departamento);
    }

    //Eliminar el departamento del repositorio
    public void eliminarDepartamento(Long id) {
        Departamento departamento = (Departamento) propiedadRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Departamento no encontrado"));
        propiedadRepository.delete(departamento);
    }
}
