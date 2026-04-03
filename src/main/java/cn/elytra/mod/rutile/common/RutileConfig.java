package cn.elytra.mod.rutile.common;

import cn.elytra.mod.rutile.Rutile;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.ConfigHolder;
import dev.toma.configuration.config.Configurable;
import dev.toma.configuration.config.format.ConfigFormats;
import net.minecraftforge.common.util.Lazy;

@Config(id = Rutile.MOD_ID)
public class RutileConfig {

    private static volatile ConfigHolder<RutileConfig> CONF_HOLDER;

    public synchronized static RutileConfig get() {
        if (CONF_HOLDER == null) CONF_HOLDER = Configuration.registerConfig(RutileConfig.class, ConfigFormats.yaml());
        return CONF_HOLDER.getConfigInstance();
    }

    public static class DrumCoating {
        @Configurable
        public boolean enable = true;
        @Configurable
        @Configurable.Comment("The max temperature that Nt-coated drums can hold.")
        @Configurable.Range(min = -1, max = Integer.MAX_VALUE)
        public int ntCoatingTemperature = -1;

        public Lazy<Integer> getNtCoatingTemperature() {
            if (ntCoatingTemperature == -1) {
                return () -> GTMaterials.Neutronium.getProperty(PropertyKey.FLUID_PIPE).getMaxFluidTemperature();
            }
            return () -> ntCoatingTemperature;
        }
    }

    public static class UnitCell {
        @Configurable
        public boolean enable = true;
    }

    public static class Misc {
        @Configurable
        @Configurable.Comment("Whether inject IFluidHandlerItem to the Super/Quantum Tanks.")
        public boolean quantumTankFluidHandlerItem = true;
        @Configurable
        @Configurable.Comment("Whether allow drums input from the bottom side.")
        public boolean drumBottomInput = true;
    }

    @Configurable
    public DrumCoating drumCoating = new DrumCoating();
    @Configurable
    public UnitCell unitCell = new UnitCell();
    @Configurable
    public Misc misc = new Misc();

}
