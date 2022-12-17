package net.ayimany.arsenal.items.bases;

import net.ayimany.arsenal.entities.bases.BulletEntity;
import net.ayimany.arsenal.util.SoundUtils;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public abstract class FirearmBase extends Item  {
    protected AmmoUnit currentUnitType;
    protected AmmoUnit.Context context;

    protected int shotDelayTicks;
    protected int reloadDelayTicks;

    protected int maxAmmo;
    protected int currentAmmo;

    protected float damageMultiplier = 1;
    protected float shotStrength = 1;

    protected float spreadFactor = 0;

    public FirearmBase(int maxAmmo, AmmoUnit.Context context) {
        super(new FabricItemSettings()
                .maxDamage(maxAmmo)
        );

        this.maxAmmo = maxAmmo;
        this.context = context;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (selected && entity instanceof PlayerEntity p) displayAmmoOnHUD(p);

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        if (world.isClient) return TypedActionResult.pass(user.getStackInHand(hand));

        // Further code will do NO client checks.

        if (canShoot())
            shoot(world, user);

        if (mustReload())
            reload(user);

        return TypedActionResult.success(user.getStackInHand(hand), false);
    }

    final void shoot(World world, PlayerEntity user) {
        playShootingSound(user);

        summonBullets(world, user);
        lowerCurrentAmmo();

        user.getItemCooldownManager().set(this, shotDelayTicks);
    }

    protected void reload(PlayerEntity user) {
        playReloadingSound(user);

        loadBulletStackFrom(user.getInventory());

        user.getItemCooldownManager().set(this, reloadDelayTicks);
    }


    protected void summonBullets(World world, PlayerEntity user) {

        for (int i = 0; i < currentUnitType.getBulletCount(); i++) {
            BulletEntity bullet = currentUnitType.produceBulletFor(user, world);

            bullet.loadWeaponProperties(this);
            bullet.loadProjectileProperties(user);

            world.spawnEntity(bullet);
        }

    }

    protected void loadBulletStackFrom(Inventory inventory) {
        if (inventory == null) return;

        ItemStack nextStack = getNextAdequateStack(inventory);

        while (currentAmmo < maxAmmo) {
            if (nextStack ==  null) return;

            loadBulletStack(nextStack);

            nextStack = getNextAdequateStack(inventory);
        }

    }

    protected void loadBulletStack(ItemStack stack) {
        if (stack == null) return;

        int ammoToDecrease = maxAmmo - currentAmmo;
        int ammoToRefill = Math.min(stack.getCount(), ammoToDecrease);

        stack.decrement(ammoToRefill);
        currentAmmo += ammoToRefill;

    }

    protected ItemStack getNextAdequateStack(Inventory inventory) {

        for (int i = 0; i < inventory.size(); i++) {
            ItemStack current = inventory.getStack(i);

            if (!(current.getItem() instanceof AmmoUnit unit) || unit.context != this.context) continue;

            currentUnitType = unit;

            return current;
        }

        return null;

    }

    protected boolean canShoot() {
        return currentAmmo > 0 && currentUnitType != null;
    }

    protected boolean mustReload() {
        return currentAmmo <= 0;
    }

    protected void lowerCurrentAmmo() {
        this.currentAmmo--;
    }

    protected void playShootingSound(PlayerEntity user) {
    }

    protected void playReloadingSound(PlayerEntity user) {
        SoundUtils.playSoundFromPlayer(user, SoundEvents.BLOCK_WOODEN_BUTTON_CLICK_OFF, 1, 0);
    }

    private void displayAmmoOnHUD(PlayerEntity entity) {
        entity.sendMessage(
                Text.of(currentUnitType + " | " + currentAmmo + " / " + maxAmmo),
                true
        );
    }

    public float getDamageMultiplier() {
        return damageMultiplier;
    }

    public float getShotStrength() {
        return shotStrength;
    }

    public float getSpreadFactor() {
        return spreadFactor;
    }
}
