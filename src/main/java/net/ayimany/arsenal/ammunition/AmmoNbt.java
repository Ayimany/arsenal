package net.ayimany.arsenal.ammunition;

import net.minecraft.nbt.NbtCompound;

public class AmmoNbt {
    NbtCompound nbt;

    public AmmoNbt() {
        this.nbt = new NbtCompound();
    }

    public AmmoNbt(NbtCompound nbt) {
        this.nbt = nbt;
    }

    public String getName() {
        return this.nbt.getString("Name");
    }

    public void putString(String key, String value) {
        this.nbt.putString(key, value);
    }

}
