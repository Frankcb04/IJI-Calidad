package IJI.Backend.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "departamento")
@DiscriminatorValue("Departamento")
public class Departamento extends Propiedad{

    @Column(name = "pisos")
    private int pisos;

    @Column(name = "interior")
    private int interior;

    @Column(name = "ascensor")
    private boolean ascensor;

    @Column(name = "areas_comunes")
    private boolean areasComunes;

    @Column(name = "areas_comunes_especificas")
    private String areasComunesEspecificas;
}
