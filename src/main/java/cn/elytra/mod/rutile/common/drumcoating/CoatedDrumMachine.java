package cn.elytra.mod.rutile.common.drumcoating;

import com.gregtechceu.gtceu.api.capability.IPropertyFluidFilter;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.common.machine.storage.DrumMachine;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

public class CoatedDrumMachine extends DrumMachine {
    protected final @NotNull IPropertyFluidFilter filter;

    public CoatedDrumMachine(IMachineBlockEntity holder, Material material, int maxStoredFluids,
                             @NotNull IPropertyFluidFilter filter, Object... args) {
        super(holder, material, maxStoredFluids, ArrayUtils.addFirst(args, filter));
        this.filter = filter;
    }

    @Override
    protected @NotNull NotifiableFluidTank createCacheFluidHandler(Object @NotNull ... args) {
        // "this.filter" is not initialized yet when the parent calls this method,
        // so we have to retrieve it via args.
        return super.createCacheFluidHandler(args).setFilter((IPropertyFluidFilter) (args[0]));
    }
}
