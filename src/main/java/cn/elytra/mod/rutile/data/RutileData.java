package cn.elytra.mod.rutile.data;

import cn.elytra.mod.rutile.RutileGregTechAddon;
import com.tterrag.registrate.providers.ProviderType;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RutileData {

    public static void init() {
        RutileGregTechAddon.registrate().addDataGenerator(ProviderType.LANG, RutileLang::init);
    }

}
