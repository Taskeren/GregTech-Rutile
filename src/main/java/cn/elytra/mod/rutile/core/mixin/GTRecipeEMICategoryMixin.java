package cn.elytra.mod.rutile.core.mixin;

import cn.elytra.mod.rutile.common.RutileConfig;
import cn.elytra.mod.rutile.common.betterworkstation.BetterWorkstation;
import com.gregtechceu.gtceu.integration.emi.recipe.GTRecipeEMICategory;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.emi.emi.api.EmiRegistry;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = GTRecipeEMICategory.class, remap = false)
public class GTRecipeEMICategoryMixin {

    @WrapMethod(method = "registerWorkStations")
    private static void rutile$replaceRegisterWorkStationsLogic(EmiRegistry registry, Operation<Void> original) {
        if (RutileConfig.get().misc.useRutileStyleEMIWorkstationList) {
            BetterWorkstation.registerWorkStations(registry);
        } else {
            original.call(registry);
        }
    }

}
