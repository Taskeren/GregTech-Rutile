package cn.elytra.mod.rutile;

import cn.elytra.mod.rutile.common.RutileRegistration;
import cn.elytra.mod.rutile.data.RutileData;
import net.minecraftforge.fml.common.Mod;

@Mod(Rutile.MOD_ID)
public class Rutile {

    public static final String MOD_ID = "rutile";

    public Rutile() {
        RutileGregTechAddon.registrate().registerRegistrate();
        RutileRegistration.init();
        RutileData.init();
    }
}
