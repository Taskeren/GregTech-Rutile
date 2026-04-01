package cn.elytra.mod.rutile.core.mixin;

import cn.elytra.mod.rutile.common.drumcoating.CoatedDrum;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.common.data.machines.GTMachineUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = GTMachineUtils.class, remap = false)
public class GTMachineUtilsMixin {

    @Inject(method = "registerDrum", at = @At("TAIL"))
    private static void rutile$hookDrumRegister(Material material, int capacity, String lang,
        CallbackInfoReturnable<MachineDefinition> cir) {
        CoatedDrum.register(material, capacity, lang);
    }

}
