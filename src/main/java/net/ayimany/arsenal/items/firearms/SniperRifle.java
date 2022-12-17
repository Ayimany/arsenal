package net.ayimany.arsenal.items.firearms;

import net.ayimany.arsenal.items.bases.AmmoUnit;
import net.ayimany.arsenal.items.bases.FirearmBase;
import net.ayimany.arsenal.util.SoundUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;

public class SniperRifle extends FirearmBase {

    public SniperRifle() {
        super(4, AmmoUnit.Context.RIFLE);
        this.reloadDelayTicks = 60;
        this.shotDelayTicks   = 30;
    }

    @Override
    protected void playShootingSound(PlayerEntity user) {
        SoundUtils.playSoundFromPlayer(user, SoundEvents.ITEM_TRIDENT_HIT, 2f, 2f);
        SoundUtils.playSoundFromPlayer(user, SoundEvents.BLOCK_SAND_PLACE, 0.5f, 0.5f);
        SoundUtils.playSoundFromPlayer(user, SoundEvents.BLOCK_GRASS_PLACE, 0.1f, 0.5f);
        SoundUtils.playSoundFromPlayer(user, SoundEvents.ENTITY_FIREWORK_ROCKET_BLAST, 1f, 2f);
        SoundUtils.playSoundFromPlayer(user, SoundEvents.BLOCK_CHAIN_PLACE, 1.2f, 2f);
    }
}
