package cn.elytra.mod.rutile.utils;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.lowdragmc.lowdraglib.syncdata.managed.MultiManagedStorage;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class FakeMachineBlockEntity implements IMachineBlockEntity {
    public static final FakeMachineBlockEntity INSTANCE = new FakeMachineBlockEntity();

    private FakeMachineBlockEntity() {
    }

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

    @Override
    public String toString() {
        return "FakeMachineBlockEntity";
    }
}
