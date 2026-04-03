package cn.elytra.mod.rutile.core.mixin;

import cn.elytra.mod.rutile.common.RutileConfig;
import com.gregtechceu.gtceu.common.machine.storage.DrumMachine;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = DrumMachine.class, remap = false)
public class DrumMachineMixin {

    @ModifyReturnValue(method = "isAllowInputFromOutputSideFluids", at = @At("RETURN"))
    private boolean rutile$inputFromOutput(boolean original) {
        if (RutileConfig.get().misc.drumBottomInput) {
            return true;
        }
        return original;
    }

}
