package net.ayimany.arsenal.itemstack;

import net.ayimany.arsenal.ammunition.AmmoNbt;
import net.ayimany.arsenal.ammunition.AmmoType;
import net.ayimany.arsenal.item.FirearmItem;
import net.ayimany.arsenal.itemdata.FirearmNbt;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class FirearmStack {
    ItemStack stack;
    FirearmNbt weaponNbt;
    AmmoNbt ammoNbt;

    public FirearmStack(FirearmItem item, ItemStack stack) {
        this.stack = stack;

        NbtCompound stackNbt = stack.getOrCreateNbt();

        if (!stackNbt.contains("Firearm")) {
            this.weaponNbt = item.createWeaponProperties();
            stackNbt.put("Firearm", weaponNbt.getNbt());
        } else {
            this.weaponNbt = new FirearmNbt((NbtCompound) stackNbt.get("Firearm"));
            this.ammoNbt = new AmmoNbt();
        }

    }

    public int getCurrentAmmo() {
        return this.weaponNbt.getCurrentAmmo();
    }

    public void setCurrentAmmo(int value) {
        this.weaponNbt.setCurrentAmmo(value);
    }

    public int getMaxAmmo() {
        return this.weaponNbt.getMaxAmmo();
    }

    public void consumeBullet() {
        this.weaponNbt.setCurrentAmmo(weaponNbt.getCurrentAmmo() - 1);
    }

    public int getShotDelayTicks() {
        return this.weaponNbt.getFireRate();
    }

    public boolean canShoot() {
        return !isOutOfRounds();
    }

    public int getReloadDelayTicks() {
        return this.weaponNbt.getReloadDelay();
    }

    public boolean mustReload() {
        return weaponNbt.getCurrentAmmo() <= 0;
    }

    public boolean canReload() {
        return weaponNbt.getCurrentAmmo() < weaponNbt.getMaxAmmo();
    }

    public boolean isOutOfRounds() {
        return weaponNbt.getCurrentAmmo() <= 0;
    }

    public boolean canBeUsed() {
        return weaponNbt.getInactiveTicks() <= 0;
    }

    public int ticksUntilActive() {
        return this.weaponNbt.getInactiveTicks();
    }

    public AmmoType getAmmoType() {
        return this.weaponNbt.getAmmoType();
    }

    public String getAmmoName() {
        return this.ammoNbt.getName();
    }

    public void tick() {
        if (!canBeUsed())
            weaponNbt.setInactiveTicks(weaponNbt.getInactiveTicks() - 1);
    }

}
