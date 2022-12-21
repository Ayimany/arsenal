package net.ayimany.arsenal.items.bases;

import net.ayimany.arsenal.entities.bases.BulletEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;

import java.util.HashMap;

public abstract class AmmoUnit extends Item {

    public static final HashMap<String, AmmoUnit> units = new HashMap<>();

    String name;

    protected float damage;
    protected   int pierce;
    protected float initialSpeed;
    protected int bulletCount;

    public static AmmoUnit getType(String key) {
        return units.get(key);
    }

    public AmmoUnit(String name, int damage, int initialSpeed, int bulletCount) {
        super(new FabricItemSettings());
        this.name        = name;
        this.damage      = damage;
        this.initialSpeed = initialSpeed;
        this.bulletCount = bulletCount;

        units.put(name, this);
    }

    public abstract BulletEntity[] toEntity(World world, PlayerEntity entity);

    public float getDamage() {
        return damage;
    }

    public float getSpeed() {
        return initialSpeed;
    }

    public int getPierce() {
        return pierce;
    }

    public int getBulletCount() {
        return bulletCount;
    }

    @Override
    public String toString() {
        return name;
    }

}
