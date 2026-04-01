package cn.elytra.mod.rutile.data;

import cn.elytra.mod.rutile.RutileGregTechAddon;
import com.tterrag.registrate.providers.ProviderType;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RutileData {

    public static void onGatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        PackOutput packOut = gen.getPackOutput();
    }

    public static void init() {
        RutileGregTechAddon.registrate().addDataGenerator(ProviderType.LANG, RutileLang::init);
    }

}
