package IJI.Backend.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "casa")
@DiscriminatorValue("Casa")
public class Casa extends Propiedad{

    @Column(name = "cant_pisos")
    private int cantPisos;

    @Column(name = "area_jardin")
    private int areaJardin;

    @Column(name = "jardin")
    private boolean jardin;

    @Column(name = "atico")
    private boolean atico;

    @Column(name = "sotano")
    private boolean sotano;
}
