package net.ayimany.arsenal;

import net.ayimany.arsenal.entities.bases.BulletEntity;
import net.ayimany.arsenal.items.ammo.shotguns.Birdshot;
import net.ayimany.arsenal.items.ammo.shotguns.Buckshot;
import net.ayimany.arsenal.items.ammo.shotguns.Slugger;
import net.ayimany.arsenal.items.firearm.shotguns.*;
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

    // SHOTGUN
    public static final Item LEVER_ACTION_SHOTGUN = new LeverActionShotgun();
    public static final Item DOUBLE_BARREL_SHOTUGN = new DoubleBarrelShotgun();
    public static final Item PUMP_ACTION_SHOTGUN = new PumpActionShotgun();
    public static final Item SEMI_AUTO_SHOTGUN = new SemiAutoShotgun();
    public static final Item SINGLE_SHOT_SHOTGUN = new SingleShotShotgun();

    public static final Item BIRDSHOT = new Birdshot();
    public static final Item BUCKSHOT = new Buckshot();
    public static final Item SLUG = new Slugger();


    public static void registerAll() {

        registerItem(LEVER_ACTION_SHOTGUN, "lever_action_shotgun");
        registerItem(DOUBLE_BARREL_SHOTUGN, "double_barrel_shotgun");
        registerItem(PUMP_ACTION_SHOTGUN, "pump_action_shotgun");
        registerItem(SEMI_AUTO_SHOTGUN, "semi_auto_shotgun");
        registerItem(SINGLE_SHOT_SHOTGUN, "single_shot_shotgun");

        registerItem(BIRDSHOT, "birdshot");
        registerItem(BUCKSHOT, "buckshot");
        registerItem(SLUG, "slugger");
    }

    public static void registerItem(Item item, String name) {
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, name), item);

    }

}
