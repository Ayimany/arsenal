package net.ayimany.arsenal.item;

import net.ayimany.arsenal.entities.BulletEntity;
import net.ayimany.arsenal.itemdata.FirearmNbt;
import net.ayimany.arsenal.itemstack.FirearmStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.function.Supplier;

import static net.ayimany.arsenal.InventoryUtils.getNextBulletStack;

public class FirearmItem extends Item implements IFirearmItem {

    private final Supplier<FirearmNbt> initialConditions;

    public FirearmItem(Supplier<FirearmNbt> nbtSupplier) {
        super(new Settings().maxCount(1));
        this.initialConditions = nbtSupplier;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        FirearmStack firearmStack = new FirearmStack(this, stack);
//        firearmStack.tick();

        if (selected && entity instanceof PlayerEntity player) {
            player.sendMessage(Text.of(firearmStack.getCurrentAmmo() + " / " + firearmStack.getMaxAmmo()), true);
        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack userStack = user.getStackInHand(hand);
        FirearmStack stack = new FirearmStack(this, userStack);

        if (!stack.canBeUsed()) return TypedActionResult.fail(userStack);

        if (stack.canShoot()) shoot(stack, user);
        else if (stack.mustReload()) reload(stack, user);

        return super.use(world, user, hand);
    }

    @Override
    public void shoot(FirearmStack stack, PlayerEntity shooter) {

        if (!shooter.world.isClient) {
            BulletEntity entity = new BulletEntity(shooter, shooter.world);

            entity.setVelocity(
                    shooter,
                    shooter.getPitch(),
                    shooter.getYaw(),
                    0.0f,
                    2.0f,
                    0.0f
            );

            shooter.world.spawnEntity(entity);

            stack.consumeBullet();

            shooter.getItemCooldownManager().set(this, stack.getShotDelayTicks());
        }

        shooter.playSound(SoundEvents.ENTITY_IRON_GOLEM_REPAIR, 1, 0.5f);
    }

    @Override
    public void reload(FirearmStack stack, PlayerEntity carrier) {

        if (!carrier.world.isClient) {
            while (stack.canReload()) {
                ItemStack bulletStack = getNextBulletStack(stack.getAmmoType(), carrier.getInventory());
                if (bulletStack == null) break;

                int requiredAmmo = stack.getMaxAmmo() - stack.getCurrentAmmo();
                int ammoToDecrease = Math.min(bulletStack.getCount(), requiredAmmo);

                bulletStack.decrement(ammoToDecrease);
                stack.setCurrentAmmo(stack.getCurrentAmmo() + ammoToDecrease);
            }

            carrier.getItemCooldownManager().set(this, stack.getReloadDelayTicks());
        }

    }

    @Override
    public FirearmNbt createWeaponProperties() {
        return initialConditions.get();
    }
}
