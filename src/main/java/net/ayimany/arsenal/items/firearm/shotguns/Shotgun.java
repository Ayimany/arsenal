package net.ayimany.arsenal.items.firearm.shotguns;

import net.ayimany.arsenal.items.ammo.shotguns.ShotgunShell;
import net.ayimany.arsenal.items.bases.FirearmBase;
import net.ayimany.arsenal.util.DispersionMethod;
import net.ayimany.arsenal.util.FirearmUtils;
import net.ayimany.arsenal.util.SoundUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;

public abstract class Shotgun extends FirearmBase {

    public static final String KEY_SPREAD_FACTOR = "SpreadFactor";

    protected final DispersionMethod method;
    protected final float spreadFactor;

    public Shotgun(DispersionMethod method, int shotDelayTicks, int reloadDelayTicks, int maxAmmo, float damageMultiplier, float shotStrength, float spreadFactor) {
        super(ShotgunShell.class, shotDelayTicks, reloadDelayTicks, maxAmmo, damageMultiplier, shotStrength);
        this.spreadFactor = spreadFactor;
        this.method = method;
    }

    @Override
    public void loadWeaponNbt(NbtCompound nbt) {
        super.loadWeaponNbt(nbt);
        nbt.putFloat(KEY_SPREAD_FACTOR, spreadFactor);
    }

    @Override
    protected void playShootingSound(PlayerEntity user) {
        SoundUtils.playSoundFromPlayer(user, SoundEvents.ENTITY_IRON_GOLEM_DAMAGE, 0.25f, 0f);
        SoundUtils.playSoundFromPlayer(user, SoundEvents.ENTITY_FIREWORK_ROCKET_BLAST_FAR, 1f, 0.f);
        SoundUtils.playSoundFromPlayer(user, SoundEvents.ENTITY_GENERIC_EXPLODE, 0.5f, 2f);
        SoundUtils.playSoundFromPlayer(user, SoundEvents.BLOCK_CHAIN_PLACE, 1.2f, 2f);
    }

    public float calculateSpread(ItemStack weapon) {
        NbtCompound nbt = weapon.getSubNbt("Arsenal");
        if (nbt == null) return 0;

        float factor = nbt.getFloat(KEY_SPREAD_FACTOR);
        int count = FirearmUtils.getLoadedUnit(weapon).getBulletCount();

        return method.calculateDispersion(factor, count);

    }
}
