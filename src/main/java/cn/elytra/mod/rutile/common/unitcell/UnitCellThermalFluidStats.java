package cn.elytra.mod.rutile.common.unitcell;

import cn.elytra.mod.rutile.utils.RutileUtils;
import com.gregtechceu.gtceu.api.item.component.ThermalFluidStats;
import com.gregtechceu.gtceu.api.misc.forge.SimpleThermalFluidHandlerItemStack;
import com.gregtechceu.gtceu.client.TooltipsHandler;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class UnitCellThermalFluidStats extends ThermalFluidStats {

    public UnitCellThermalFluidStats(int maxFluidTemperature, boolean gasProof, boolean acidProof,
                                     boolean cryoProof, boolean plasmaProof) {
        super(FluidType.BUCKET_VOLUME,
            maxFluidTemperature,
            gasProof,
            acidProof,
            cryoProof,
            plasmaProof,
            false);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(ItemStack itemStack, @NotNull Capability<T> cap) {
        if (cap == ForgeCapabilities.FLUID_HANDLER_ITEM) {
            LazyOptional<SimpleThermalFluidHandlerItemStack> capability = super.getCapability(itemStack, cap).cast();
            return ForgeCapabilities.FLUID_HANDLER_ITEM.orEmpty(cap,
                capability.lazyMap(UnitCellFluidHandlerItemStack::new));
        }
        return LazyOptional.empty();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents,
                                TooltipFlag isAdvanced) {
        if (stack.hasTag()) {
            FluidUtil.getFluidHandler(stack).<UnitCellFluidHandlerItemStack>cast()
                .map(FluidHandlerItemStackSimple::getFluid).ifPresent(tank -> {
                    int totalStackAmount = tank.getAmount() * stack.getCount();
                    tooltipComponents
                        .add(Component.translatable("rutile.universal.tooltip.fluid_stored",
                            tank.getDisplayName(),
                            totalStackAmount,
                            Component.literal("(" + RutileUtils.toFluidAmount(tank.getFluid(), totalStackAmount) + ")")
                                .withStyle(ChatFormatting.GRAY)));
                    TooltipsHandler.appendFluidTooltips(tank, tooltipComponents::add, null);
                });
        }

        if (GTUtil.isShiftDown()) {
            tooltipComponents.add(Component.translatable("gtceu.fluid_pipe.max_temperature",
                FormattingUtil.formatNumbers(this.maxFluidTemperature)));
            if (this.gasProof) tooltipComponents.add(Component.translatable("gtceu.fluid_pipe.gas_proof"));
            else tooltipComponents.add(Component.translatable("gtceu.fluid_pipe.not_gas_proof"));
            if (this.plasmaProof) tooltipComponents.add(Component.translatable("gtceu.fluid_pipe.plasma_proof"));
            if (this.cryoProof) tooltipComponents.add(Component.translatable("gtceu.fluid_pipe.cryo_proof"));
            if (this.acidProof) tooltipComponents.add(Component.translatable("gtceu.fluid_pipe.acid_proof"));
        } else if (this.gasProof || this.cryoProof || this.plasmaProof || this.acidProof) {
            tooltipComponents.add(Component.translatable("gtceu.tooltip.fluid_pipe_hold_shift"));
        }
    }

}
