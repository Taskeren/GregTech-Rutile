package cn.elytra.mod.rutile;

import cn.elytra.mod.rutile.common.drumcoating.CoatedDrum;
import cn.elytra.mod.rutile.common.unitcell.UnitCell;
import com.gregtechceu.gtceu.api.addon.GTAddon;
import com.gregtechceu.gtceu.api.addon.IGTAddon;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

@GTAddon
public class RutileGregTechAddon implements IGTAddon {

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
        CoatedDrum.addRecipes(provider);
        UnitCell.addRecipes(provider);
    }

}
