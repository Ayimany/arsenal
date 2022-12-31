package net.ayimany.arsenal.entities;

import net.ayimany.arsenal.ArsenalRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.TntBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BulletEntity extends ThrownItemEntity {

    private static final float COMMON_WEAK_BLOCK_RESISTANCE = 0.4f;

    public BulletEntity(EntityType<? extends BulletEntity> entityType, World world) {
        super(entityType, world);
    }

    public BulletEntity(LivingEntity owner, World world) {
        super(ArsenalRegistry.BULLET_ENTITY, owner, world);
    }


    @Override
    protected Item getDefaultItem() {
        return Items.FIREWORK_STAR;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();

        entity.damage(DamageSource.thrownProjectile(this, getOwner()), 6);
        entity.timeUntilRegen = 0;

        kill();
    }

    @Override
    protected void onBlockHit(BlockHitResult result) {

        if (!world.isClient()) {
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

}