package cn.elytra.mod.rutile.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import net.minecraft.ChatFormatting;

/**
 * @see ChatFormatting
 */
public class RutileLang {

    public static void init(RegistrateLangProvider provider) {
        provider.add("rutile.universal.tooltip.fluid_stored", "§2Fluid Stored: §f%s, %s mB %s");
        provider.add("rutile.never_hatches.import.tooltip", "§7Unable to import anything, just a structural marker.");
        provider.add("rutile.never_hatches.export.tooltip", "§7Unable to export anything, just a structural marker.");
    }

}
