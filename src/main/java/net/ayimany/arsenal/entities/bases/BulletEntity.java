package net.ayimany.arsenal.entities.bases;

import net.ayimany.arsenal.items.bases.AmmoUnit;
import net.ayimany.arsenal.items.bases.FirearmBase;
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

public abstract class BulletEntity extends ThrownItemEntity {
    public static final float COMMON_WEAK_BLOCK_RESISTANCE = 0.4f;

    protected float _damage;
    protected float _speed;
    protected float _pierce;
    protected float _yawDispersion;
    protected float _pitchDispersion;

    public BulletEntity(EntityType<? extends BulletEntity> entityType, World world) {
        super(entityType, world);
    }

    public BulletEntity(EntityType<? extends BulletEntity> entityType, LivingEntity owner, World world) {
        super(entityType, owner, world);
    }

    public void loadAmmoUnitProperties(AmmoUnit unit) {
        this._damage = unit.getDamage();
        this._speed  = unit.getSpeed();
        this._pierce = unit.getPierce();
    }

    public void loadWeaponProperties(FirearmBase base, ItemStack weapon) {
        NbtCompound nbt = weapon.getSubNbt("Arsenal");
        if (nbt == null) return;

        this._damage        *= nbt.getFloat(KEY_DAMAGE_MULTIPLIER);
    }

    public void loadProjectileProperties(Entity shooter) {
        this.setVelocity(shooter,
                shooter.getPitch() + _yawDispersion,
                shooter.getYaw() + _pitchDispersion,
                0.0f, _speed, 0.0f
        );
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
