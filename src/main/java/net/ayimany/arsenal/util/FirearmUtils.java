package net.ayimany.arsenal.util;

import net.ayimany.arsenal.items.bases.AmmoUnit;
import net.ayimany.arsenal.items.bases.FirearmBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class FirearmUtils {

    public static AmmoUnit getLoadedUnit(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateSubNbt("Arsenal");
        return AmmoUnit.getType(nbt.getString(FirearmBase.KEY_LOADED_AMMO_NAME));
    }
}
