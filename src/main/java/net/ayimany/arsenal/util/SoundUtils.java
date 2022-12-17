package net.ayimany.arsenal.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

public class SoundUtils {

    public static void playSoundFromPlayer(PlayerEntity entity, SoundEvent event, float volume, float pitch) {
        entity.world
                .playSoundFromEntity(null, entity, event, SoundCategory.PLAYERS, volume, pitch);
    }

    public static void playSoundFromEntity(Entity entity, SoundEvent event, float volume, float pitch) {
        entity.world
                .playSoundFromEntity(null, entity, event, SoundCategory.AMBIENT, volume, pitch);
    }
}
