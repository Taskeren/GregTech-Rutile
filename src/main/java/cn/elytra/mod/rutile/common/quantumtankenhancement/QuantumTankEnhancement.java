package cn.elytra.mod.rutile.common.quantumtankenhancement;

import cn.elytra.mod.rutile.Rutile;
import cn.elytra.mod.rutile.common.RutileConfig;
import com.gregtechceu.gtceu.api.item.MetaMachineItem;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.common.data.GTMachines;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.ArrayUtils;

@Mod.EventBusSubscriber
public class QuantumTankEnhancement {

    private static final Lazy<MachineDefinition[]> SUPER_TANKS = Lazy.of(() -> ArrayUtils.addAll(GTMachines.SUPER_TANK,
        GTMachines.QUANTUM_TANK));

    private static final boolean enable = Rutile.isGTVersionLowerThan("7.0.0", false)
        .orElseThrow(() -> new RuntimeException("Unable to get GregTech version"));

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        if (!enable || !RutileConfig.get().misc.quantumTankFluidHandlerItem) return;

        ItemStack itemStack = event.getObject();
        if (itemStack.getItem() instanceof MetaMachineItem metaMachineItem) {
            MachineDefinition definition = metaMachineItem.getDefinition();
            if (ArrayUtils.contains(SUPER_TANKS.get(), definition)) {
                long capacity = getCapacity(definition.getTier());
                event.addCapability(Rutile.id("fluid"), new QuantumFluidHandlerItemStack(itemStack, capacity));
            }
        }
    }

    private static long getCapacity(int tier) {
        return 4000 * FluidType.BUCKET_VOLUME * (long) Math.pow(2, tier - 1);
    }

}
