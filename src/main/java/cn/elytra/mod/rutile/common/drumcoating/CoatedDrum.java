package cn.elytra.mod.rutile.common.drumcoating;

import cn.elytra.mod.rutile.RutileGregTechAddon;
import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.block.MetaMachineBlock;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.IPropertyFluidFilter;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.FluidPipeProperties;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.client.renderer.machine.MachineRenderer;
import com.gregtechceu.gtceu.common.data.machines.GTMachineUtils;
import com.gregtechceu.gtceu.common.machine.storage.DrumMachine;
import com.gregtechceu.gtceu.common.registry.GTRegistration;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.ASSEMBLER_RECIPES;

public class CoatedDrum {

    private static final Logger log = LoggerFactory.getLogger(CoatedDrum.class);

    public static final Map<Material, MachineDefinition> PTFE_COATED_DRUMS = new HashMap<>();
    public static final Map<Material, MachineDefinition> NEUTRONIUM_COATED_DRUMS = new HashMap<>();

    public record CoatingOptions(String prefixId, String prefixName, OverridePropertyFluidFilter withCoat) {
        public static final CoatingOptions PTFE = new CoatingOptions("ptfe_coated",
            "PTFE Coated",
            OverridePropertyFluidFilter.PTFE);
        public static final CoatingOptions NEUTRONIUM = new CoatingOptions("neutronium_coated",
            "Neutronium Coated",
            OverridePropertyFluidFilter.NEUTRONIUM);
    }

    public static MachineDefinition registerCoatedDrum(Material material, int capacity, String lang,
                                                       CoatingOptions options) {
        FluidPipeProperties pipeProperties;
        if (material.hasProperty(PropertyKey.FLUID_PIPE)) {
            pipeProperties = material.getProperty(PropertyKey.FLUID_PIPE);
        } else {
            // ignore drums whose material doesn't even have a pipe property.
            return null;
        }

        IPropertyFluidFilter filter = options.withCoat().build(pipeProperties);
        MachineDefinition definition = RutileGregTechAddon.registrate().machine(
                options.prefixId() + "_" + material.getName() + "_drum",
                MachineDefinition::createDefinition,
                (holder) -> new CoatedDrumMachine(holder, material, capacity, filter),
                MetaMachineBlock::new,
                (holder, prop) -> new CoatedDrumMachineItem(holder, prop, material, filter),
                MetaMachineBlockEntity::createBlockEntity)
            .langValue(options.prefixName() + " " + lang)
            .rotationState(RotationState.NONE)
            .renderer(() -> new MachineRenderer(GTCEu.id("block/machine/metal_drum")))
            .tooltipBuilder((stack, list) -> {
                GTMachineUtils.TANK_TOOLTIPS.accept(stack, list);
                filter.appendTooltips(list, false, true);
            })
            .tooltips(
                Component.translatable("gtceu.machine.quantum_tank.tooltip"),
                Component.translatable(
                    "gtceu.universal.tooltip.fluid_storage_capacity",
                    FormattingUtil.formatNumbers(capacity)))
            .paintingColor(material.getMaterialRGB())
            .itemColor((s, i) -> material.getMaterialRGB())
            .register();
        // set drum capacity to the map so that the item can get the correct capacity.
        GTMachineUtils.DRUM_CAPACITY.put(definition, capacity);
        return definition;
    }

    @ApiStatus.Internal
    public static void register(Material material, int capacity, String lang) {
        MachineDefinition ptfe = registerCoatedDrum(material, capacity, lang, CoatingOptions.PTFE);
        PTFE_COATED_DRUMS.put(material, ptfe);
        MachineDefinition neutronium = registerCoatedDrum(material, capacity, lang, CoatingOptions.NEUTRONIUM);
        NEUTRONIUM_COATED_DRUMS.put(material, neutronium);
    }

    @ApiStatus.Internal
    public static void addCoatingRecipes(Map<Material, MachineDefinition> map, Material coatingMaterial,
                                         Consumer<FinishedRecipe> provider) {
        map.forEach((mat, def) -> {
            Optional<MachineDefinition> uncoatedOptional = GTMachineUtils.DRUM_CAPACITY.keySet()
                .stream()
                .filter(d -> {
                    DrumMachine dm = (DrumMachine) d.createMetaMachine(new RutileGregTechAddon.FakeBlockEntity());
                    return dm.getMaterial() == mat;
                }).findFirst();

            if (uncoatedOptional.isEmpty()) {
                log.info("Unable to generate coating recipe for {}, because it failed to find the uncoated version",
                    def.getName());
                return;
            }
            MachineDefinition uncoated = uncoatedOptional.get();

            ASSEMBLER_RECIPES.recipeBuilder("coating_" + uncoated.getName() + "_with_" + coatingMaterial.getName())
                .inputItems(uncoated.asStack())
                .inputFluids(coatingMaterial.getFluid(8 * 144))
                .outputItems(def.asStack())
                .duration(50)
                .inputEU(16)
                .save(provider);
        });
    }

}
