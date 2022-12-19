package net.ayimany.arsenal.items.firearm.shotguns;

import net.ayimany.arsenal.items.bases.AmmoUnit;
import net.ayimany.arsenal.items.bases.FirearmBase;
import net.ayimany.arsenal.util.SoundUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;

public class LeverActionShotgun extends FirearmBase {

    public LeverActionShotgun() {
        super(6, AmmoUnit.Context.SHOTGUN);
        this.reloadDelayTicks = 50;
        this.shotDelayTicks   = 10;
        this.spreadFactor     = 4;
        this.shotStrength     = 0.8f;
    }

    @Override
    protected void playShootingSound(PlayerEntity user) {
        SoundUtils.playSoundFromPlayer(user, SoundEvents.ENTITY_IRON_GOLEM_DAMAGE, 0.25f, 0f);
        SoundUtils.playSoundFromPlayer(user, SoundEvents.ENTITY_FIREWORK_ROCKET_BLAST_FAR, 1f, 0.f);
        SoundUtils.playSoundFromPlayer(user, SoundEvents.ENTITY_GENERIC_EXPLODE, 0.5f, 2f);
        SoundUtils.playSoundFromPlayer(user, SoundEvents.BLOCK_CHAIN_PLACE, 1.2f, 2f);
    }
}
