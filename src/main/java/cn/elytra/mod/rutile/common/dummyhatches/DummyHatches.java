package cn.elytra.mod.rutile.common.dummyhatches;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.client.renderer.machine.OverlayTieredMachineRenderer;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.machine.multiblock.part.FluidHatchPartMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.ItemBusPartMachine;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.Ingredient;

import cn.elytra.mod.rutile.RutileGregTechAddon;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class DummyHatches {

    public static final MachineDefinition NEVER_INPUT_BUS = RutileGregTechAddon.registrate()
            .machine("never_input_bus", holder -> new NeverItemBusPartMachine(holder, 1, IO.IN))
            .tier(1)
            .langValue("Never Input Bus")
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.EXPORT_ITEMS)
            .renderer(() -> new OverlayTieredMachineRenderer(1, GTCEu.id("block/machine/part/item_bus.import")))
            .tooltips(Component.translatable("rutile.never_hatches.import.tooltip"))
            .register();

    public static final MachineDefinition NEVER_INPUT_HATCH = RutileGregTechAddon.registrate()
            .machine("never_input_hatch", holder -> new NeverFluidHatchPartMachine(holder, 1, IO.IN, 0, 0))
            .tier(1)
            .langValue("Never Input Hatch")
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.EXPORT_FLUIDS)
            .renderer(() -> new OverlayTieredMachineRenderer(1, GTCEu.id("block/machine/part/fluid_hatch.import")))
            .tooltips(Component.translatable("rutile.never_hatches.import.tooltip"))
            .register();

    public static final MachineDefinition NEVER_OUTPUT_BUS = RutileGregTechAddon.registrate()
            .machine("never_output_bus", holder -> new NeverItemBusPartMachine(holder, 1, IO.OUT))
            .tier(1)
            .langValue("Never Output Bus")
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.EXPORT_ITEMS)
            .renderer(() -> new OverlayTieredMachineRenderer(1, GTCEu.id("block/machine/part/item_bus.export")))
            .tooltips(Component.translatable("rutile.never_hatches.export.tooltip"))
            .register();

    public static final MachineDefinition NEVER_OUTPUT_HATCH = RutileGregTechAddon.registrate()
            .machine("never_output_hatch", holder -> new NeverFluidHatchPartMachine(holder, 1, IO.OUT, 0, 0))
            .tier(1)
            .langValue("Never Output Hatch")
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.EXPORT_FLUIDS)
            .renderer(() -> new OverlayTieredMachineRenderer(1, GTCEu.id("block/machine/part/fluid_hatch.export")))
            .tooltips(Component.translatable("rutile.never_hatches.export.tooltip"))
            .register();

    public static void init() {}

    @ApiStatus.Internal
    public static void addRecipes(Consumer<FinishedRecipe> provider) {
        VanillaRecipeHelper.addSmeltingRecipe(provider, NEVER_INPUT_BUS.getId(),
                Ingredient.of(GTMachines.ITEM_IMPORT_BUS[0].asStack()), NEVER_INPUT_BUS.asStack(), 2.0F);
        VanillaRecipeHelper.addSmeltingRecipe(provider, NEVER_INPUT_HATCH.getId(),
                Ingredient.of(GTMachines.FLUID_IMPORT_HATCH[0].asStack()), NEVER_INPUT_HATCH.asStack(), 2.0F);
        VanillaRecipeHelper.addSmeltingRecipe(provider, NEVER_OUTPUT_BUS.getId(),
                Ingredient.of(GTMachines.ITEM_EXPORT_BUS[0].asStack()), NEVER_OUTPUT_BUS.asStack(), 2.0F);
        VanillaRecipeHelper.addSmeltingRecipe(provider, NEVER_OUTPUT_HATCH.getId(),
                Ingredient.of(GTMachines.FLUID_EXPORT_HATCH[0].asStack()), NEVER_OUTPUT_HATCH.asStack(), 2.0F);
    }

    static class NeverItemBusPartMachine extends ItemBusPartMachine {

        public NeverItemBusPartMachine(IMachineBlockEntity holder, int tier, IO io, Object... args) {
            super(holder, tier, io, args);
        }

        @Override
        protected int getInventorySize() {
            return 0;
        }

        @Override
        public @NotNull Widget createUIWidget() {
            return new WidgetGroup();
        }
    }

    static class NeverFluidHatchPartMachine extends FluidHatchPartMachine {

        public NeverFluidHatchPartMachine(IMachineBlockEntity holder, int tier, IO io, int initialCapacity, int slots,
                                          Object... args) {
            super(holder, tier, io, initialCapacity, slots, args);
        }

        @Override
        public @NotNull Widget createUIWidget() {
            return new WidgetGroup();
        }
    }
}
