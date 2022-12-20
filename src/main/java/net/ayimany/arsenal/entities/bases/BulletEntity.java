package net.ayimany.arsenal.entities.bases;

import net.ayimany.arsenal.ArsenalRegistry;
import net.ayimany.arsenal.items.bases.AmmoUnit;
import net.ayimany.arsenal.util.SoundUtils;
import net.minecraft.block.Block;
import net.minecraft.block.TntBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static net.ayimany.arsenal.items.bases.FirearmBase.*;

/**
 * The entity summoned by every single weapon in Arsenal. <p>
 * It's Bullet -> Gun -> Projectile build allows inclusion of all necessary parts to produce a unique projectile. <p>
 * Third and last part of the Arsenal equation
**/
public class BulletEntity extends ThrownItemEntity {
    public static final float COMMON_WEAK_BLOCK_RESISTANCE = 0.4f;
    float _damage;
    float _speed;
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
        this._damage        = unit.getDamage();
        this._speed         = unit.getSpeed();
        this._pierce        = unit.getPierce();
        this._pitchMod      = unit.getStability();
        this._yawMod        = unit.getStability();
    }

    public void loadWeaponProperties(ItemStack weapon) {
        NbtCompound nbt = weapon.getSubNbt("Arsenal");
        if (nbt == null) return;

        this._damage        *= nbt.getFloat(KEY_DAMAGE_MULTIPLIER);
        this._speed         *= nbt.getFloat(KEY_SHOT_STRENGTH);
        this._pitchMod      *= (this.random.nextFloat() * (random.nextBoolean() ? -1 : 1)) * nbt.getFloat(KEY_SPREAD_FACTOR);
        this._yawMod        *= (this.random.nextFloat() * (random.nextBoolean() ? -1 : 1)) * nbt.getFloat(KEY_SPREAD_FACTOR);
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
        Block block = world.getBlockState(pos).getBlock();
        if (block.getBlastResistance() <= COMMON_WEAK_BLOCK_RESISTANCE) {
            world.breakBlock(pos, false);
        }

        if (!world.isClient && block instanceof TntBlock && getOwner() != null) {
            TntEntity tntEntity = new TntEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, (LivingEntity) getOwner());
            tntEntity.setFuse(0);
            world.spawnEntity(tntEntity);
        }
    }

    private void playBlockCollisionSound() {
        SoundUtils.playSoundFromEntity(this, SoundEvents.BLOCK_NETHERITE_BLOCK_PLACE, 0.75f, 2);
    }

}
