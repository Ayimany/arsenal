package net.ayimany.arsenal.itemdata;

import net.ayimany.arsenal.ammunition.AmmoType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

public class FirearmNbt {
    NbtCompound nbt;

    public FirearmNbt() {
        this.nbt = new NbtCompound();
    }

    public FirearmNbt(NbtCompound nbt) {
        this.nbt = nbt;
    }

    public int getMaxAmmo() {
        return nbt.getInt("MaxAmmo");
    }

    public int getCurrentAmmo() {
        return nbt.getInt("CurrentAmmo");
    }

    public void setCurrentAmmo(int value) {
        nbt.putInt("CurrentAmmo", value);
    }

    public AmmoType getAmmoType() {
        return AmmoType.valueOf(this.nbt.getString("AmmoType"));
    }

    public int getFireRate() {
        return nbt.getInt("FireRate");
    }

    public int getReloadDelay() {
        return nbt.getInt("ReloadDelay");
    }

    public int getInactiveTicks() {
        return nbt.getInt("InactiveTicks");
    }

    public void setInactiveTicks(int value) {
        nbt.putInt("InactiveTicks", value);
    }

    public void put(String key, NbtElement value) {
        this.nbt.put(key, value);
    }

    public void putInt(String key, int value) {
        this.nbt.putInt(key, value);
    }

    public void putString(String key, String value) {
        this.nbt.putString(key, value);
    }

    public NbtCompound getNbt() {
        return nbt;
    }
}
