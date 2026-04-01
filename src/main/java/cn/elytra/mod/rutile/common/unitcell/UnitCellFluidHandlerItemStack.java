package cn.elytra.mod.rutile.common.unitcell;

import com.gregtechceu.gtceu.api.misc.forge.SimpleThermalFluidHandlerItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;

public class UnitCellFluidHandlerItemStack extends SimpleThermalFluidHandlerItemStack {

    public UnitCellFluidHandlerItemStack(SimpleThermalFluidHandlerItemStack delegate) {
        super(delegate.getContainer(),
            FluidType.BUCKET_VOLUME,
            delegate.getMaxFluidTemperature(),
            delegate.isGasProof(),
            delegate.isAcidProof(),
            delegate.isCryoProof(),
            delegate.isPlasmaProof());
    }

    @Override
    public int getTankCapacity(int tank) {
        return FluidType.BUCKET_VOLUME;
    }

    @Override
    public int fill(@NotNull FluidStack resource, FluidAction action) {
        int unit = UnitCell.getFluidUnitAmount(resource.getFluid());
        if (this.container.getCount() == 1 && !resource.isEmpty() && this.canFillFluidType(resource)) {
            FluidStack contained = this.getFluid();
            if (contained.isEmpty()) {
                int fillAmount = Math.min(unit, resource.getAmount());
                if (fillAmount == unit) {
                    if (action.execute()) {
                        FluidStack filled = resource.copy();
                        filled.setAmount(fillAmount);
                        this.setFluid(filled);
                    }

                    return fillAmount;
                }
            }
        }
        return 0;
    }

    private void removeTagWhenEmpty(FluidAction action) {
        if (getFluid() == FluidStack.EMPTY && action.execute()) {
            this.container.setTag(null);
        }
    }

    private FluidStack drainInteranl(int maxDrain, FluidAction action) {
        FluidStack contained = this.getFluid();
        if (contained.isEmpty() || maxDrain <= 0 || !this.canDrainFluidType(contained)) return FluidStack.EMPTY;
        int unit = UnitCell.getFluidUnitAmount(contained.getFluid());

        if (this.container.getCount() == 1) {
            int drainAmount = Math.min(contained.getAmount(), maxDrain);
            if (drainAmount >= unit) {
                FluidStack drained = contained.copy();
                if (action.execute()) {
                    this.setContainerToEmpty();
                }
                return drained;
            }
        }
        return FluidStack.EMPTY;
    }

    @Override
    public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
        FluidStack drained = drainInteranl(maxDrain, action);
        this.removeTagWhenEmpty(action);
        return drained;
    }
}
