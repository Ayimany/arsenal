package net.ayimany.arsenal.item;

import net.ayimany.arsenal.itemdata.FirearmNbt;
import net.ayimany.arsenal.itemstack.FirearmStack;
import net.minecraft.entity.player.PlayerEntity;

public interface IFirearmItem {

    void shoot(FirearmStack stack, PlayerEntity shooter);
    void reload(FirearmStack stack, PlayerEntity carrier);
    FirearmNbt createWeaponProperties();

}
