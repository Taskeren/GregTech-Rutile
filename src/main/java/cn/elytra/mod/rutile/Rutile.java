package cn.elytra.mod.rutile;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forgespi.language.IModInfo;

import cn.elytra.mod.rutile.common.RutileConfig;
import cn.elytra.mod.rutile.common.RutileRegistration;
import cn.elytra.mod.rutile.common.dummyhatches.DummyHatches;
import cn.elytra.mod.rutile.core.mixin.ItemAccessor;
import cn.elytra.mod.rutile.data.RutileData;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Mod(Rutile.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Rutile {

    public static final String MOD_ID = "rutile";

    public static final Logger log = LoggerFactory.getLogger(MOD_ID);

    public Rutile(FMLJavaModLoadingContext context) {
        log.info("Rutile initialization");
        log.info("GregTech version: {}", gtVersion().map(Object::toString).orElse("<null version>"));

        // initialize the config
        RutileConfig.get();

        // register generic event listeners
        context.getModEventBus().addGenericListener(MachineDefinition.class, Rutile::onMachineRegister);

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

    @SubscribeEvent
    static void onLoadComplete(FMLLoadCompleteEvent event) {
        if (RutileConfig.get().misc.emptyBucketSizeEnlarge) {
            ((ItemAccessor) Items.BUCKET).setMaxStackSize(64);
        }
    }

    static void onMachineRegister(GTCEuAPI.RegisterEvent<?, MachineDefinition> event) {
        DummyHatches.init();
    }

    @SuppressWarnings("unchecked")
    public static <T extends Throwable> T typeErasure(final Throwable throwable) throws T {
        return (T) throwable;
    }
}
