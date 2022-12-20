package net.ayimany.arsenal.items.firearm.shotguns;

import net.ayimany.arsenal.items.bases.AmmoUnit;
import net.ayimany.arsenal.items.bases.FirearmBase;
import net.ayimany.arsenal.util.SoundUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;

/**
 * Really quick shotgun type. Weaker than other shotgun types. <p>\
 * It's a game. Balancing is needed.
 **/
public class SemiAutoShotgun extends FirearmBase {

    public SemiAutoShotgun() {
        super(
                AmmoUnit.Context.SHOTGUN,
                5,
                75,
                12,
                0.75f,
                0.75f,
                14,
                1
        );
    }

    @Override
    protected void playShootingSound(PlayerEntity user) {
        SoundUtils.playSoundFromPlayer(user, SoundEvents.ENTITY_IRON_GOLEM_DAMAGE, 0.25f, 0f);
        SoundUtils.playSoundFromPlayer(user, SoundEvents.ENTITY_FIREWORK_ROCKET_BLAST_FAR, 1f, 0.f);
        SoundUtils.playSoundFromPlayer(user, SoundEvents.ENTITY_GENERIC_EXPLODE, 0.5f, 2f);
        SoundUtils.playSoundFromPlayer(user, SoundEvents.BLOCK_CHAIN_PLACE, 1.2f, 2f);
    }
}
