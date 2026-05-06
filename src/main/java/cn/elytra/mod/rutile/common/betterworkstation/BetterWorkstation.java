package cn.elytra.mod.rutile.common.betterworkstation;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.category.GTRecipeCategory;
import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.machinezoo.noexception.Exceptions;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import static com.gregtechceu.gtceu.integration.emi.recipe.GTRecipeEMICategory.machineCategory;

@ApiStatus.Internal
public final class BetterWorkstation {

    public static void registerWorkStations(EmiRegistry registry) {
        // collect the relationship about recipe types and the machines that can work the recipe types.
        Multimap<GTRecipeType, MachineDefinition> recipeTypeToMachineDefinitions = ArrayListMultimap.create();
        for (MachineDefinition machine : GTRegistries.MACHINES) {
            @Nullable GTRecipeType[] recipeTypes = machine.getRecipeTypes();
            if (recipeTypes != null) {
                for (@Nullable GTRecipeType recipeType : recipeTypes) {
                    if (recipeType != null) recipeTypeToMachineDefinitions.put(recipeType, machine);
                }
            }
        }

        // do registration for all recipe types.
        for (GTRecipeType recipeType : recipeTypeToMachineDefinitions.keys()) {
            // make the list of workstations.
            // categories from the same recipe type has the exactly same workstations.
            Collection<MachineDefinition> workstationsUngrouped = recipeTypeToMachineDefinitions.get(recipeType);
            Collection<EmiIngredient> workstations = groupByClass(workstationsUngrouped);

            // register workstations for the categories.
            for (GTRecipeCategory category : recipeType.getCategories()) {
                // skip for invisible categories
                if (!category.isXEIVisible() && !GTCEu.isDev()) continue;
                // get EMI category from GT ones
                EmiRecipeCategory emiCategory = machineCategory(category);
                workstations.forEach(w -> registry.addWorkstation(emiCategory, w));
            }
        }
    }

    private static Collection<EmiIngredient> groupByClass(Collection<MachineDefinition> input) {
        Multimap<Object, MachineDefinition> groupingMap = ArrayListMultimap.create();

        // grouping by the machineSupplier.
        // when their machineSupplier is same, the MetaMachine instance should belong to the same class,
        // so that the behavior should be same, and should be grouped together.
        // P.S., the lambda expression will be singleton, if there's nothing captured, otherwise, they will be instantiated for each.
        // so we're comparing the class of the lambdas instead of the lambdas themselves.
        for (MachineDefinition definition : input) {
            Object machineSupplierClass = Exceptions.sneak().supplier(() -> FIELD_MACHINE_SUPPLIER.get(definition).getClass()).get();
            groupingMap.put(machineSupplierClass, definition);
        }

        // from the grouping result, generate the emi ingredient.
        List<EmiIngredient> ret = Lists.newArrayList();
        for (Object machineSupplierClass : groupingMap.keySet()) {
            Collection<MachineDefinition> machines = groupingMap.get(machineSupplierClass);
            // only 1 possible candidate
            if (machines.size() == 1) {
                ret.add(EmiStack.of(machines.iterator().next().asStack()));
            }
            // more than 1
            else {
                // FIXME: EMI complains about missing an ID for serialization. I'm not sure if it matters, but it didn't crash tho.
                List<EmiStack> list = machines.stream().map(MachineDefinition::asStack).map(EmiStack::of).toList();
                ret.add(EmiIngredient.of(list));
            }
        }
        return ret;
    }

    private static final Field FIELD_MACHINE_SUPPLIER;

    static {
        try {
            FIELD_MACHINE_SUPPLIER = MachineDefinition.class.getDeclaredField("machineSupplier");
            FIELD_MACHINE_SUPPLIER.setAccessible(true);
        } catch (NoSuchFieldException e) {
            ExceptionUtils.rethrow(e);
            throw new Error("unreachable!");
        }
    }
}
