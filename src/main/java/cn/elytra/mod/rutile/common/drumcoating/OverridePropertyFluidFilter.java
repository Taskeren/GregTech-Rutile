package cn.elytra.mod.rutile.common.drumcoating;

import cn.elytra.mod.rutile.common.RutileConfig;
import com.gregtechceu.gtceu.api.capability.IPropertyFluidFilter;
import com.gregtechceu.gtceu.api.fluids.PropertyFluidFilter;
import com.gregtechceu.gtceu.api.fluids.attribute.FluidAttributes;

public class OverridePropertyFluidFilter {
    public static final OverridePropertyFluidFilter PTFE = new OverridePropertyFluidFilter(
        null,
        true,
        true,
        null,
        null
    );
    public static final OverridePropertyFluidFilter NEUTRONIUM = new OverridePropertyFluidFilter(
        RutileConfig.get().drumCoating.getNtCoatingTemperature().get(),
        true,
        true,
        true,
        true
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
