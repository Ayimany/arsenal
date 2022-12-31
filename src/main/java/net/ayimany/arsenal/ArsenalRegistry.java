package net.ayimany.arsenal;

import net.ayimany.arsenal.ammunition.AmmoNbtBuilder;
import net.ayimany.arsenal.ammunition.AmmoType;
import net.ayimany.arsenal.ammunition.Ammunition;
import net.ayimany.arsenal.entities.BulletEntity;
import net.ayimany.arsenal.item.FirearmItem;
import net.ayimany.arsenal.itemdata.FirearmNbtBuilder;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ArsenalRegistry {
    public static final String MOD_ID = "arsenal";

    public static final EntityType<BulletEntity> BULLET_ENTITY = Registry.register(
            Registries.ENTITY_TYPE,

            new Identifier(MOD_ID, "shotgun_pellet"),

            FabricEntityTypeBuilder.<BulletEntity> create(SpawnGroup.MISC, BulletEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25F, 0.25F))
                    .trackRangeBlocks(4).trackedUpdateRate(10)
                    .build()
    );

    public static final ItemGroup ARSENAL_ITEM_GROUP = FabricItemGroup.builder(new Identifier(MOD_ID, "items"))
            .icon(() -> new ItemStack(Items.FIREWORK_STAR))
            .build();

    public static final Item PISTOL = new FirearmItem(() ->
            new FirearmNbtBuilder()
                    .maxAmmo(12)
                    .fireRate(20)
                    .reloadDelay(80)
                    .ammoType(AmmoType.PISTOL)
                    .build()
    );

    public static final Ammunition PISTOL_AMMO = new Ammunition(AmmoType.PISTOL, () ->
            new AmmoNbtBuilder()
                    .name("PistolAmmo")
                    .build()
    );

    public static void registerAll() {
        registerItem(PISTOL, "pistol");
        registerItem(PISTOL_AMMO, "pistol_ammo");
    }

    public static void registerItem(Item item, String name) {
        Registry.register(Registries.ITEM, new Identifier(MOD_ID, name), item);
        ItemGroupEvents.modifyEntriesEvent(ARSENAL_ITEM_GROUP).register((content) -> content.add(item));
    }

}
