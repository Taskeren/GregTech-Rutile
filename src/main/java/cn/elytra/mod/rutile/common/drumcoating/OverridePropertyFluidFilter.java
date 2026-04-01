package cn.elytra.mod.rutile.common.drumcoating;

import com.gregtechceu.gtceu.api.capability.IPropertyFluidFilter;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.fluids.PropertyFluidFilter;
import com.gregtechceu.gtceu.api.fluids.attribute.FluidAttributes;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import java.util.Objects;

public class OverridePropertyFluidFilter {
    public static final OverridePropertyFluidFilter PTFE = new OverridePropertyFluidFilter(
        null,
        true,
        true,
        null,
        null
    );
    public static final OverridePropertyFluidFilter NEUTRONIUM = new OverridePropertyFluidFilter(
        Objects.requireNonNull(GTMaterials.Neutronium.getProperty(PropertyKey.FLUID_PIPE),
            "Neutronium Fluid Pipe Property").getMaxFluidTemperature(),
        true,
        true,
        true,
        null
    );

    public final Integer maxTemperature;
    public final Boolean gasProof, acidProof, cryoProof, plasmaProof;

    public OverridePropertyFluidFilter(Integer maxTemperature, Boolean gasProof, Boolean acidProof, Boolean cryoProof,
                                       Boolean plasmaProof) {
        this.maxTemperature = maxTemperature;
        this.gasProof = gasProof;
        this.acidProof = acidProof;
        this.cryoProof = cryoProof;
        this.plasmaProof = plasmaProof;
    }

    public PropertyFluidFilter build(IPropertyFluidFilter defaultValue) {
        return new PropertyFluidFilter(
            maxTemperature != null ? maxTemperature : defaultValue.getMaxFluidTemperature(),
            gasProof != null ? gasProof : defaultValue.isGasProof(),
            acidProof != null ? acidProof : defaultValue.canContain(FluidAttributes.ACID),
            cryoProof != null ? cryoProof : defaultValue.isCryoProof(),
            plasmaProof != null ? plasmaProof : defaultValue.isPlasmaProof()
        );
    }
}
