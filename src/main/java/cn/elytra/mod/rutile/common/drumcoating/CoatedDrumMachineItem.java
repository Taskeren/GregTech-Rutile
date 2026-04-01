package cn.elytra.mod.rutile.common.drumcoating;

import com.gregtechceu.gtceu.api.block.IMachineBlock;
import com.gregtechceu.gtceu.api.capability.IPropertyFluidFilter;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.fluids.attribute.FluidAttributes;
import com.gregtechceu.gtceu.api.item.DrumMachineItem;
import com.gregtechceu.gtceu.api.misc.forge.ThermalFluidHandlerItemStack;
import com.gregtechceu.gtceu.common.data.machines.GTMachineUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

public class CoatedDrumMachineItem extends DrumMachineItem {
    private final @NotNull IPropertyFluidFilter filter;

    public CoatedDrumMachineItem(IMachineBlock block, Properties properties, Material mat,
                                 @NotNull IPropertyFluidFilter filter) {
        super(block, properties, mat);
        this.filter = filter;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(ItemStack itemStack, @NotNull Capability<T> cap) {
        if (cap == ForgeCapabilities.FLUID_HANDLER_ITEM) {
            return ForgeCapabilities.FLUID_HANDLER_ITEM.orEmpty(cap, LazyOptional.of(
                () -> new ThermalFluidHandlerItemStack(
                    itemStack,
                    Math.toIntExact(GTMachineUtils.DRUM_CAPACITY.getInt(getDefinition())),
                    filter.getMaxFluidTemperature(),
                    filter.isGasProof(),
                    filter.canContain(FluidAttributes.ACID),
                    filter.isCryoProof(),
                    filter.isPlasmaProof()
                )
            ));
        }
        return LazyOptional.empty();
    }
}
