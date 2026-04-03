package cn.elytra.mod.rutile.common.unitcell;

import cn.elytra.mod.rutile.common.RutileConfig;
import cn.elytra.mod.rutile.common.RutileRegistration;
import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.FluidProperty;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.chemical.material.registry.MaterialRegistry;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKey;
import com.gregtechceu.gtceu.api.item.component.ICustomDescriptionId;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import it.unimi.dsi.fastutil.objects.Reference2IntMap;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import it.unimi.dsi.fastutil.objects.ReferenceList;
import net.minecraft.ChatFormatting;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;
import java.util.function.Predicate;

import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.ASSEMBLER_RECIPES;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class UnitCell {

    private static final Logger log = LoggerFactory.getLogger(UnitCell.class);

    public static final ReferenceList<Material> MARKED_MATERIALS = new ReferenceArrayList<>();
    public static final Reference2IntMap<FluidType> FLUID_UNIT_AMOUNT = new Reference2IntOpenHashMap<>();

    public static int getFluidUnitAmount(FluidType fluidType) {
        return FLUID_UNIT_AMOUNT.getOrDefault(fluidType, FluidType.BUCKET_VOLUME);
    }

    public static int getFluidUnitAmount(Fluid fluid) {
        return getFluidUnitAmount(fluid.getFluidType());
    }

    public static void setFluidUnitAmount(FluidType fluidType, int amount) {
        FLUID_UNIT_AMOUNT.put(fluidType, amount);
    }

    public static int getFluidUnitAmount(FluidStack fluidStack) {
        return getFluidUnitAmount(fluidStack.getFluid());
    }

    @ApiStatus.Internal
    public static void bake() {
        for (MaterialRegistry registry : GTCEuAPI.materialManager.getRegistries()) {
            for (Material material : registry.getAllMaterials()) {
                // polymers
                if (material.hasProperty(PropertyKey.POLYMER)
                    || material.hasProperty(PropertyKey.INGOT)
                    || material.hasProperty(PropertyKey.DUST)) {
                    if (material.hasProperty(PropertyKey.FLUID)) {
                        log.debug("Registering default unit amount for {}", material.getName());
                        MARKED_MATERIALS.add(material);
                        FluidProperty prop = material.getProperty(PropertyKey.FLUID);
                        for (FluidStorageKey key : FluidStorageKey.allKeys()) {
                            Fluid fluid = prop.getStorage().get(key);
                            if (fluid != null) {
                                setFluidUnitAmount(fluid.getFluidType(), 144);
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        bake();
        log.info("Discovered {} 144-unit-amount materials", MARKED_MATERIALS.size());
    }

    @ApiStatus.Internal
    public static ICustomDescriptionId cellName() {
        return new ICustomDescriptionId() {
            @Override
            public Component getItemName(ItemStack stack) {
                if (stack.getItem() == RutileRegistration.FLUID_CELL.asItem()) {
                    Component prefix = FluidUtil.getFluidHandler(stack).<UnitCellFluidHandlerItemStack>cast()
                        .map(FluidHandlerItemStackSimple::getFluid)
                        .filter(Predicate.not(FluidStack::isEmpty))
                        .map(FluidStack::getDisplayName)
                        .orElse(Component.translatable("gtceu.fluid.empty"));
                    return Component.translatable(stack.getDescriptionId(), prefix);
                }
                return Component.literal("Glitched item, report this to the author!")
                    .withStyle(ChatFormatting.RED, ChatFormatting.ITALIC);
            }
        };
    }

    public static void addRecipes(Consumer<FinishedRecipe> provider) {
        if(!RutileConfig.get().unitCell.enable) return;

        ASSEMBLER_RECIPES.recipeBuilder("unit_fluid_cell_tin_alloy")
            .inputItems(GTItems.FLUID_CELL)
            .inputFluids(GTMaterials.TinAlloy.getFluid(144))
            .outputItems(RutileRegistration.FLUID_CELL)
            .duration(25)
            .inputEU(16)
            .save(provider);

        ASSEMBLER_RECIPES.recipeBuilder("unit_fluid_cell_pvc")
            .inputItems(GTItems.FLUID_CELL, 8)
            .inputFluids(GTMaterials.PolyvinylChloride.getFluid(144))
            .outputItems(RutileRegistration.FLUID_CELL, 8)
            .duration(25)
            .inputEU(16)
            .save(provider);
    }
}
