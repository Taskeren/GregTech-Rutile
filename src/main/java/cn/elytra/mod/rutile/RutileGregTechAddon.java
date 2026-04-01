package cn.elytra.mod.rutile;

import cn.elytra.mod.rutile.common.RutileRegistration;
import cn.elytra.mod.rutile.common.drumcoating.CoatedDrum;
import com.gregtechceu.gtceu.api.addon.GTAddon;
import com.gregtechceu.gtceu.api.addon.IGTAddon;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.lowdragmc.lowdraglib.syncdata.managed.MultiManagedStorage;
import net.minecraft.data.recipes.FinishedRecipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.ASSEMBLER_RECIPES;

@GTAddon
public class RutileGregTechAddon implements IGTAddon {

    private static final Logger log = LoggerFactory.getLogger(RutileGregTechAddon.class);
    private static RutileGregTechAddon instance;
    private static final GTRegistrate REGISTRATE = GTRegistrate.create(Rutile.MOD_ID);

    public RutileGregTechAddon() {
        instance = this;
    }

    public static RutileGregTechAddon instance() {
        return instance;
    }

    public static GTRegistrate registrate() {
        return REGISTRATE;
    }

    @Override
    public GTRegistrate getRegistrate() {
        return registrate();
    }

    @Override
    public void initializeAddon() {
    }

    @Override
    public String addonModId() {
        return Rutile.MOD_ID;
    }

    @Override
    public void addRecipes(Consumer<FinishedRecipe> provider) {
        CoatedDrum.addCoatingRecipes(CoatedDrum.PTFE_COATED_DRUMS, GTMaterials.Polytetrafluoroethylene, provider);
        CoatedDrum.addCoatingRecipes(CoatedDrum.NEUTRONIUM_COATED_DRUMS, GTMaterials.Neutronium, provider);

        ASSEMBLER_RECIPES.recipeBuilder("unit_fluid_cell")
            .inputItems(GTItems.FLUID_CELL)
            .inputFluids(GTMaterials.TinAlloy.getFluid(144))
            .outputItems(RutileRegistration.FLUID_CELL)
            .duration(25)
            .inputEU(16)
            .save(provider);
    }

    public static class FakeBlockEntity implements IMachineBlockEntity {
        @Override
        public MetaMachine getMetaMachine() {
            return null;
        }

        @Override
        public long getOffset() {
            return 0;
        }

        @Override
        public MultiManagedStorage getRootStorage() {
            return null;
        }
    }
}
