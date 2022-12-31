package net.ayimany.arsenal.ammunition;

import net.minecraft.item.Item;

import java.util.function.Supplier;

public class Ammunition extends Item {
    Supplier<AmmoNbt> initialConditions;
    AmmoType type;

    public Ammunition(AmmoType type, Supplier<AmmoNbt> nbtSupplier) {
        super(new Settings());
        this.type = type;
        this.initialConditions = nbtSupplier;
    }

    public AmmoType getType() {
        return type;
    }

    public AmmoNbt createAmmoProperties() {
        return initialConditions.get();
    }
}
