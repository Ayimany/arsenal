package net.ayimany.arsenal;

import net.ayimany.arsenal.entities.bases.BulletEntity;
import net.ayimany.arsenal.items.ammo.PistolAmmo;
import net.ayimany.arsenal.items.ammo.RifleAmmo;
import net.ayimany.arsenal.items.ammo.ShotgunAmmo;
import net.ayimany.arsenal.items.firearms.Pistol;
import net.ayimany.arsenal.items.firearms.Shotgun;
import net.ayimany.arsenal.items.firearms.SniperRifle;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ArsenalRegistry {
    public static final String MOD_ID = "arsenal";

    public static final EntityType<BulletEntity> BULLET_ENTITY_TYPE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(MOD_ID, "pistol_bullet"),
            FabricEntityTypeBuilder.<BulletEntity> create(SpawnGroup.MISC, BulletEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25F, 0.25F))
                    .trackRangeBlocks(4).trackedUpdateRate(10)
                    .build()
    );

    public static final Item PISTOL = new Pistol();
    public static final Item PISTOL_AMMO = new PistolAmmo();

    public static final Item RIFLE = new SniperRifle();
    public static final Item RIFLE_AMMO = new RifleAmmo();

    public static final Item SHOTGUN = new Shotgun();
    public static final Item SHOTGUN_AMMO = new ShotgunAmmo();

    public static void registerAll() {
        registerItem(PISTOL, "pistol");
        registerItem(PISTOL_AMMO, "pistol_ammo");

        registerItem(RIFLE, "rifle");
        registerItem(RIFLE_AMMO, "rifle_ammo");

        registerItem(SHOTGUN, "shotgun");
        registerItem(SHOTGUN_AMMO, "shotgun_ammo");
    }

    public static void registerItem(Item item, String name) {
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, name), item);

    }

}
