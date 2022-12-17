package net.ayimany.arsenal.entities.bases;

import net.ayimany.arsenal.ArsenalRegistry;
import net.ayimany.arsenal.items.bases.AmmoUnit;
import net.ayimany.arsenal.items.bases.FirearmBase;
import net.ayimany.arsenal.util.SoundUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BulletEntity extends ThrownItemEntity {
    public static final float WEAK_BLOCK_COMMON_MAX_RESISTANCE = 0.4f;
    float _damage;
    float _speed;
    float _breakingPower;
      int _pierce;
    float _yawMod;
    float _pitchMod;

    public BulletEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    @SuppressWarnings("unused")
    public BulletEntity(double d, double e, double f, World world) {
        super(ArsenalRegistry.BULLET_ENTITY_TYPE, d, e, f, world);
    }

    public BulletEntity(LivingEntity owner, World world) {
        super(ArsenalRegistry.BULLET_ENTITY_TYPE, owner, world);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.FIREWORK_STAR;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        entity.damage(DamageSource.thrownProjectile(this, getOwner()), _damage);
        entity.timeUntilRegen = 0;

        if (_pierce > 0) _pierce--;
        else kill();
    }

    @Override
    protected void onBlockHit(BlockHitResult result) {

        if (!world.isClient()) {
            playBlockCollisionSound();
            kill();

            destroyBlockOnCollision(result);
        }

        //+ Required
        super.onBlockHit(result);
    }

    public void loadAmmoUnitProperties(AmmoUnit unit) {
        this._damage = unit.getDamage();
        this._speed = unit.getSpeed();
    }

    public void loadWeaponProperties(
            FirearmBase weapon
    ) {
        this._damage        *= weapon.getDamageMultiplier();
        this._speed         *= weapon.getShotStrength();
        this._breakingPower  = WEAK_BLOCK_COMMON_MAX_RESISTANCE * weapon.getShotStrength();
        this._pitchMod       = (this.random.nextFloat() * (random.nextBoolean() ? -1 : 1)) * weapon.getSpreadFactor();
        this._yawMod         = (this.random.nextFloat() * (random.nextBoolean() ? -1 : 1)) * weapon.getSpreadFactor();
    }

    public void loadProjectileProperties(Entity shooter) {
        this.setVelocity(shooter,
                shooter.getPitch() + _pitchMod,
                shooter.getYaw() + _yawMod,
                0.0f, _speed, 0.0f
        );
    }

    private void destroyBlockOnCollision(BlockHitResult result) {
        BlockPos pos = result.getBlockPos();
        if (world.getBlockState(pos).getBlock().getBlastResistance() < _breakingPower) {
            world.breakBlock(pos, false);
        }
    }

    private void playBlockCollisionSound() {
        SoundUtils.playSoundFromEntity(this, SoundEvents.BLOCK_CHAIN_STEP, 1, 2);
    }

}
