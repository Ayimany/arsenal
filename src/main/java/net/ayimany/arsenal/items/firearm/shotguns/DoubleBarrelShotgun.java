package net.ayimany.arsenal.items.firearm.shotguns;

import net.ayimany.arsenal.util.DispersionMethods;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class DoubleBarrelShotgun extends Shotgun {

    public DoubleBarrelShotgun() {
        super(
                DispersionMethods.NEGATIVE_GAUSSIAN,
                20,
                80,
                2,
                1f,
                1f,
                7f
        );
    }

    @Override
    protected void applyShotCooldown(NbtCompound nbt, PlayerEntity user) {
        if (nbt.getInt(KEY_CURRENT_AMMO) % 2 == 0) super.applyShotCooldown(nbt, user);
    }
}
