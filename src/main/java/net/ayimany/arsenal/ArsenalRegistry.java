package net.ayimany.arsenal;

import net.ayimany.arsenal.entities.bases.BulletEntity;
import net.ayimany.arsenal.entities.bullets.ShotgunPellet;
import net.ayimany.arsenal.items.ammo.shotguns.Birdshot;
import net.ayimany.arsenal.items.ammo.shotguns.Buckshot;
import net.ayimany.arsenal.items.ammo.shotguns.Slugger;
import net.ayimany.arsenal.items.firearm.shotguns.*;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * Full-static class to register Arsenal items, entities and more.
 **/
public class ArsenalRegistry {
    public static final String MOD_ID = "arsenal";

    public static final EntityType<ShotgunPellet> SHOTGUN_PELLET = Registry.register(
            Registries.ENTITY_TYPE,

            new Identifier(MOD_ID, "shotgun_pellet"),

            FabricEntityTypeBuilder.<ShotgunPellet> create(SpawnGroup.MISC, ShotgunPellet::new)
                    .dimensions(EntityDimensions.fixed(0.25F, 0.25F))
                    .trackRangeBlocks(4).trackedUpdateRate(10)
                    .build()
    );

    // SHOTGUNS
    public static final Item BOLT_ACTION_SHOTGUN = new BoltActionShotgun();
    public static final Item DOUBLE_BARREL_SHOTGUN = new DoubleBarrelShotgun();
    public static final Item PUMP_ACTION_SHOTGUN = new PumpActionShotgun();
    public static final Item SEMI_AUTO_SHOTGUN = new SemiAutoShotgun();
    public static final Item SINGLE_SHOT_SHOTGUN = new SingleShotShotgun();

    public static final Item BIRDSHOT = new Birdshot();
    public static final Item BUCKSHOT = new Buckshot();
    public static final Item SLUG = new Slugger();


    public static final ItemGroup ARSENAL_ITEM_GROUP = FabricItemGroup.builder(new Identifier(MOD_ID, "items"))
            .icon(() -> new ItemStack(BIRDSHOT))
            .build();

    /**
     * Registers all known items meant to be in the Arsenal mod.
     **/
    public static void registerAll() {

        registerItem(BOLT_ACTION_SHOTGUN, "bolt_action_shotgun");
        registerItem(DOUBLE_BARREL_SHOTGUN, "double_barrel_shotgun");
        registerItem(PUMP_ACTION_SHOTGUN, "pump_action_shotgun");
        registerItem(SEMI_AUTO_SHOTGUN, "semi_auto_shotgun");
        registerItem(SINGLE_SHOT_SHOTGUN, "single_shot_shotgun");

        registerItem(BIRDSHOT, "birdshot");
        registerItem(BUCKSHOT, "buckshot");
        registerItem(SLUG, "slugger");
    }

    /**
     * Takes an item and puts it in the minecraft registry under the name: arsenal:NAME
     **/
    public static void registerItem(Item item, String name) {
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, name), item);
        ItemGroupEvents.modifyEntriesEvent(ARSENAL_ITEM_GROUP).register((content) -> content.add(item));
    }

}
