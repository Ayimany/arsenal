package net.ayimany.arsenal.items.bases;

import net.ayimany.arsenal.entities.bases.BulletEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public abstract class AmmoUnit extends Item {
    Context context;
    String name;

    protected float damage;
    protected float speed;
    protected   int bulletCount;
    protected   int pierce;
    protected float stability = 1;

    public AmmoUnit(String name, Context context, int damage, int speed, int bulletCount) {
        super(new FabricItemSettings());
        this.name        = name;
        this.context     = context;
        this.damage      = damage;
        this.speed       = speed;
        this.bulletCount = bulletCount;
    }

    public enum Context {
        PISTOL,
        RIFLE,
        SHOTGUN,
        ROCKET
    }

    public BulletEntity produceBulletFor(LivingEntity owner, World world) {
        BulletEntity bullet = new BulletEntity(owner, world);

        bullet.loadAmmoUnitProperties(this);

        return bullet;
    }

    public float getDamage() {
        return damage;
    }

    public float getSpeed() {
        return speed;
    }

    public int getBulletCount() {
        return bulletCount;
    }

    public int getPierce() {
        return pierce;
    }

    public float getStability() {
        return stability;
    }

    @Override
    public String toString() {
        return name;
    }

}
