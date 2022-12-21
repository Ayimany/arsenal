package net.ayimany.arsenal.entities.bullets;

import net.ayimany.arsenal.ArsenalRegistry;
import net.ayimany.arsenal.entities.bases.BulletEntity;
import net.ayimany.arsenal.items.ammo.shotguns.ShotgunShell;
import net.ayimany.arsenal.items.bases.AmmoUnit;
import net.ayimany.arsenal.items.bases.FirearmBase;
import net.ayimany.arsenal.items.firearm.shotguns.Shotgun;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ShotgunPellet extends BulletEntity {

    public ShotgunPellet(EntityType<? extends BulletEntity> entityType, World world) {
        super(entityType, world);
    }

    public ShotgunPellet(LivingEntity owner, World world) {
        super(ArsenalRegistry.SHOTGUN_PELLET, owner, world);
    }

    @Override
    public void loadAmmoUnitProperties(AmmoUnit unit) {
        super.loadAmmoUnitProperties(unit);
        this._yawDispersion = _pitchDispersion = ((ShotgunShell) unit).getSpreadMultiplier() * (random.nextBoolean() ? 1 : -1);
    }

    @Override
    public void loadWeaponProperties(FirearmBase base, ItemStack weapon) {
        super.loadWeaponProperties(base, weapon);
        this._yawDispersion   *= ((Shotgun) base).calculateSpread(weapon);
        this._pitchDispersion *= ((Shotgun) base).calculateSpread(weapon);
    }

}
