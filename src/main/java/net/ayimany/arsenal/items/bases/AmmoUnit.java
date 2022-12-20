package net.ayimany.arsenal.items.bases;

import net.ayimany.arsenal.entities.bases.BulletEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;

import java.util.HashMap;

/**
 * All bullet types in Arsenal inherit this class. <p>
 * Contains all properties a bullet would have, including weight, pierce, damage, etc. <p>
 * The class also sets up how the physical projectile should spawn. <p>
 * Second part of the Arsenal equation
 * **/
public abstract class AmmoUnit extends Item {

    // BEGIN STATIC

    public static final float DEFAULT_BULLET_SPEED = 12.0f;
    public static final HashMap<String, AmmoUnit> units = new HashMap<>();

    public static AmmoUnit getType(String key) {
        return units.get(key);
    }

    public enum Context {
        PISTOL(1),
        RIFLE(2),
        SHOTGUN(3),
        ROCKET(4);

        final int type;
        Context(int type) {
            this.type = type;
        }

    }

    // BEGIN INSTANCE

    Context context;
    String name;

    protected float damage;
    protected   int bulletCount;
    protected   int pierce;
    protected float weight;
    protected float stability = 1;

    public AmmoUnit(String name, Context context, int damage, int bulletCount, int weight) {
        super(new FabricItemSettings());
        this.name        = name;
        this.context     = context;
        this.damage      = damage;
        this.bulletCount = bulletCount;
        this.weight      = weight;

        units.put(name, this);

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
        return DEFAULT_BULLET_SPEED - weight;
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

    public float getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return name;
    }

}
