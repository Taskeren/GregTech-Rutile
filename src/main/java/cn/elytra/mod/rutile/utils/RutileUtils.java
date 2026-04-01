package cn.elytra.mod.rutile.utils;

import cn.elytra.mod.rutile.common.unitcell.UnitCell;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

public final class RutileUtils {

    public static String toFluidAmount(int fluidAmount, int unitAmount) {
        int units = fluidAmount / unitAmount;
        int remainder = fluidAmount % unitAmount;

        if (units == 0) {
            return String.valueOf(remainder);
        }

        String s = units + " x " + unitAmount;
        if (remainder > 0) {
            s += " + " + remainder;
        }
        return s;
    }

    public static String toFluidAmount(FluidStack fluidStack) {
        int unit = UnitCell.getFluidUnitAmount(fluidStack);
        return toFluidAmount(fluidStack.getAmount(), unit);
    }

    public static String toFluidAmount(Fluid fluid, int amount) {
        int unit = UnitCell.getFluidUnitAmount(fluid);
        return toFluidAmount(amount, unit);
    }

}
