package cn.elytra.mod.rutile;

import cn.elytra.mod.rutile.common.RutileConfig;
import cn.elytra.mod.rutile.common.RutileRegistration;
import cn.elytra.mod.rutile.data.RutileData;
import com.gregtechceu.gtceu.GTCEu;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forgespi.language.IModInfo;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Mod(Rutile.MOD_ID)
public class Rutile {

    public static final String MOD_ID = "rutile";

    public static final Logger log = LoggerFactory.getLogger(MOD_ID);

    public Rutile() {
        log.info("Rutile initialization");
        log.info("GregTech version: {}", gtVersion().map(Object::toString).orElse("<null version>"));

        // initialize the config
        RutileConfig.get();

        RutileGregTechAddon.registrate().registerRegistrate();
        RutileRegistration.init();
        RutileData.init();
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static Optional<ArtifactVersion> gtVersion() {
        return ModList.get().getMods().stream()
            .filter(m -> m.getModId().equals(GTCEu.MOD_ID))
            .findFirst()
            .map(IModInfo::getVersion);
    }

    public static Optional<Boolean> isGTVersionLowerThan(String version, boolean orEquals) {
        return gtVersion().map(v -> {
            int compare = v.compareTo(new DefaultArtifactVersion(version));
            return compare < 0 || orEquals && compare == 0;
        });
    }
}
