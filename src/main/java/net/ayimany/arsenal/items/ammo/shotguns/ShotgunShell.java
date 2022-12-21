package net.ayimany.arsenal.items.ammo.shotguns;

import net.ayimany.arsenal.entities.bullets.ShotgunPellet;
import net.ayimany.arsenal.items.bases.AmmoUnit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public abstract class ShotgunShell extends AmmoUnit {

    int gauge;
    float spreadMultiplier;

    public ShotgunShell(String name, int damage, int initialSpeed, int bulletCount, int gauge, float spreadMultiplier) {
        super(name, damage, initialSpeed, bulletCount);
        this.gauge = gauge;
        this.spreadMultiplier = spreadMultiplier;
    }

    @Override
    public ShotgunPellet[] toEntity(World world, PlayerEntity user) {
        ShotgunPellet[] entities = new ShotgunPellet[bulletCount];

        for (int i = 0; i < entities.length; i++)
            entities[i] = new ShotgunPellet(user, world);

        return entities;
    }

    public int getGauge() {
        return gauge;
    }

    public float getSpreadMultiplier() {
        return spreadMultiplier;
    }

}
