package cn.elytra.mod.rutile.core.mixin.lowdrag;

import com.llamalad7.mixinextras.sugar.Local;
import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.EmiStackInteraction;
import dev.emi.emi.screen.EmiScreenManager;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SlotWidget.class, remap = false)
@Restriction(
    require = @Condition(value = "ldlib", versionPredicates = "<1.0.39.a")
)
public class SlotWidgetMixin {

    // https://github.com/Low-Drag-MC/LDLib-MultiLoader/pull/74/changes

    @Inject(method = "keyPressed", at = @At(value = "INVOKE", target = "Ldev/emi/emi/screen/EmiScreenManager;stackInteraction(Ldev/emi/emi/api/stack/EmiStackInteraction;Ljava/util/function/Function;)Z"), cancellable = true)
    private void rutile$fixKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir,
                                      @Local(name = "emiStack") EmiStack emiStack) {
        boolean value = EmiScreenManager.stackInteraction(new EmiStackInteraction(emiStack),
            (bind) -> bind.matchesKey(keyCode, scanCode));
        cir.setReturnValue(value);
    }

    @Inject(method = "mouseClicked", at = @At(value = "INVOKE", target = "Ldev/emi/emi/screen/EmiScreenManager;stackInteraction(Ldev/emi/emi/api/stack/EmiStackInteraction;Ljava/util/function/Function;)Z"), cancellable = true)
    private void rutile$fixMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir,
                                        @Local(name = "emiStack") EmiStack emiStack) {
        boolean value = EmiScreenManager.stackInteraction(new EmiStackInteraction(emiStack),
            (bind) -> bind.matchesMouse(button));
        cir.setReturnValue(value);
    }

}
