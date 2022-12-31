package net.ayimany.arsenal.itemdata;

import net.ayimany.arsenal.ammunition.AmmoType;
import net.minecraft.nbt.NbtCompound;

public class FirearmNbtBuilder {
    FirearmNbt nbt;

    public FirearmNbtBuilder() {
        this.nbt = new FirearmNbt();

        this.nbt.put("Ammo", new NbtCompound());
        this.nbt.putInt("CurrentAmmo", 0);
    }

    public FirearmNbt build() {
        return this.nbt;
    }

    public FirearmNbtBuilder maxAmmo(int value) {
        this.nbt.putInt("MaxAmmo", value);
        return this;
    }

    public FirearmNbtBuilder ammoType(AmmoType type) {
        this.nbt.putString("AmmoType", type.toString());
        return this;
    }

    public FirearmNbtBuilder fireRate(int ticks) {
        this.nbt.putInt("FireRate", ticks);
        return this;
    }

    public FirearmNbtBuilder reloadDelay(int ticks) {
        this.nbt.putInt("ReloadDelay", ticks);
        return this;
    }

    public FirearmNbtBuilder overheatTolerance(int ticks) {
        this.nbt.putInt("OverheatTolerance", ticks);
        return this;
    }

}
