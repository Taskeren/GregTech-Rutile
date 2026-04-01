package cn.elytra.mod.rutile.common;

import cn.elytra.mod.rutile.RutileGregTechAddon;
import cn.elytra.mod.rutile.common.unitcell.UnitCell;
import cn.elytra.mod.rutile.common.unitcell.UnitCellThermalFluidStats;
import com.gregtechceu.gtceu.api.item.ComponentItem;
import com.gregtechceu.gtceu.api.item.IComponentItem;
import com.gregtechceu.gtceu.api.item.component.IItemComponent;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.item.ItemFluidContainer;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullConsumer;

public class RutileRegistration {

    public static ItemEntry<ComponentItem> FLUID_CELL = RutileGregTechAddon.registrate()
        .item("fluid_cell", ComponentItem::create)
        .lang("%s Fluid Cell")
        .setData(ProviderType.ITEM_MODEL, NonNullBiConsumer.noop())
        .color(() -> GTItems::cellColor)
        .onRegister(attach(
            new UnitCellThermalFluidStats(1800, true, true, true, true),
            new ItemFluidContainer(),
            UnitCell.cellName()
        ))
        .register();


    public static <T extends IComponentItem> NonNullConsumer<T> attach(IItemComponent... components) {
        return item -> item.attachComponents(components);
    }

    public static void init() {
    }

}
