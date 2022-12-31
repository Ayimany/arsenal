package net.ayimany.arsenal;

import net.ayimany.arsenal.ammunition.AmmoType;
import net.ayimany.arsenal.ammunition.Ammunition;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class InventoryUtils {

    public static ItemStack getNextBulletStack(AmmoType type, Inventory inventory) {
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);

            if (!(stack.getItem() instanceof Ammunition ammo)) continue;
            if(ammo.getType() != type) continue;

            return stack;
        }

        return null;
    }

}
