package net.ayimany.arsenal.items.bases;

import net.ayimany.arsenal.entities.bases.BulletEntity;
import net.ayimany.arsenal.util.SoundUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

/**
 * Every firearm in Arsenal inherits from this class.<p>
 * This contains all the logic to shoot, reload and more. Overrideable if necessary,
 *     although, few weapons will do this. <p>
 * First part of the Arsenal equation.
 **/
public abstract class FirearmBase extends Item  {
    protected AmmoUnit.Context context;

    public static final String KEY_SHOT_DELAY_TICKS   = "ShotDelay";
    public static final String KEY_RELOAD_DELAY_TICKS = "ReloadDelay";
    public static final String KEY_MAX_AMMO           = "MaxAmmo";
    public static final String KEY_CURRENT_AMMO       = "CurrentAmmo";
    public static final String KEY_SHOTS_PER_ACTION   = "ShotsPerTriggerAction";
    public static final String KEY_DAMAGE_MULTIPLIER  = "DamageMultiplier";
    public static final String KEY_SHOT_STRENGTH      = "ShotStrength";
    public static final String KEY_SPREAD_FACTOR      = "SpreadFactor";
    public static final String KEY_LOADED_AMMO_NAME   = "LoadedAmmoName";

    protected final   int SHOT_DELAY_TICKS;
    protected final   int RELOAD_DELAY_TICKS;
    protected final   int MAX_AMMO;
    protected final float DAMAGE_MULTIPLIER;
    protected final float SHOT_STRENGTH;
    protected final float SPREAD_FACTOR;
    protected final   int SHOTS_PER_ACTION;

    public FirearmBase(AmmoUnit.Context context, int SHOT_DELAY_TICKS, int RELOAD_DELAY_TICKS, int MAX_AMMO, float DAMAGE_MULTIPLIER, float SHOT_STRENGTH, float SPREAD_FACTOR, int SHOTS_PER_ACTION) {
        super(new Item.Settings().maxDamage((int)(3600 * MAX_AMMO * (SHOT_STRENGTH / 2f))));

        this.context = context;

        this.SHOT_DELAY_TICKS = SHOT_DELAY_TICKS;
        this.RELOAD_DELAY_TICKS = RELOAD_DELAY_TICKS;
        this.MAX_AMMO = MAX_AMMO;
        this.DAMAGE_MULTIPLIER = DAMAGE_MULTIPLIER;
        this.SHOT_STRENGTH = SHOT_STRENGTH;
        this.SPREAD_FACTOR = SPREAD_FACTOR;
        this.SHOTS_PER_ACTION = SHOTS_PER_ACTION;

    }

    public void loadWeaponNbt(NbtCompound nbt) {

        nbt.putInt(KEY_SHOT_DELAY_TICKS, SHOT_DELAY_TICKS);
        nbt.putInt(KEY_RELOAD_DELAY_TICKS, RELOAD_DELAY_TICKS);
        nbt.putInt(KEY_MAX_AMMO, MAX_AMMO);
        nbt.putInt(KEY_SHOTS_PER_ACTION, SHOTS_PER_ACTION);

        nbt.putFloat(KEY_DAMAGE_MULTIPLIER, DAMAGE_MULTIPLIER);
        nbt.putFloat(KEY_SHOT_STRENGTH, SHOT_STRENGTH);
        nbt.putFloat(KEY_SPREAD_FACTOR, SPREAD_FACTOR);

        nbt.putString(KEY_LOADED_AMMO_NAME, "None");

    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (selected && entity instanceof PlayerEntity p) {
            displayAmmoOnHUD(stack, p);
        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        // Further code will do NO client checks.
        if (world.isClient) return TypedActionResult.pass(user.getStackInHand(hand));

        ItemStack weapon = user.getStackInHand(hand);
        NbtCompound nbt = weapon.getSubNbt("Arsenal");

        if (nbt == null) loadWeaponNbt(weapon.getOrCreateSubNbt("Arsenal"));

        if (canShoot(weapon)) shoot(weapon, world, user);
        else if (mustReload(weapon)) reload(weapon, user);

        return TypedActionResult.success(user.getStackInHand(hand), false);
    }

    public final void shoot(ItemStack stack, World world, PlayerEntity user) {
        NbtCompound nbt = stack.getSubNbt("Arsenal");
        if (nbt == null) return;

        playShootingSound(user);

        for (int i = 0; i < nbt.getInt(KEY_SHOTS_PER_ACTION); i++) {
            summonBullets(stack, world, user);
            lowerCurrentAmmo(stack);
        }

        if (nbt.getInt(KEY_CURRENT_AMMO) == 0) nbt.putString(KEY_LOADED_AMMO_NAME, "None");

        stack.damage(1, user, (entity -> {}));
        user.getItemCooldownManager().set(this, nbt.getInt(KEY_SHOT_DELAY_TICKS));
    }

    protected void summonBullets(ItemStack weapon, World world, PlayerEntity user) {
        NbtCompound nbt = weapon.getSubNbt("Arsenal");
        if (nbt == null) return;

        AmmoUnit currentUnit = AmmoUnit.getType(nbt.getString(KEY_LOADED_AMMO_NAME));

        for (int i = 0; i < currentUnit.getBulletCount(); i++) {
            BulletEntity bullet = currentUnit.produceBulletFor(user, world);

            bullet.loadWeaponProperties(weapon);
            bullet.loadProjectileProperties(user);

            world.spawnEntity(bullet);
        }

    }

    protected boolean canShoot(ItemStack weapon) {
        NbtCompound nbt = weapon.getSubNbt("Arsenal");
        if (nbt == null) return false;

        return nbt.getInt(KEY_CURRENT_AMMO) > 0 && !(nbt.getString(KEY_LOADED_AMMO_NAME).equals("None"));
    }

    public void reload(ItemStack weapon, PlayerEntity user) {
        NbtCompound nbt = weapon.getSubNbt("Arsenal");
        if (nbt == null) return;

        boolean foundAmmo = findAndLoadBulletStackFrom(weapon, user.getInventory());

        if (!foundAmmo) return;

        playReloadingSound(user);
        user.getItemCooldownManager().set(this, nbt.getInt(KEY_RELOAD_DELAY_TICKS));
    }

    protected boolean findAndLoadBulletStackFrom(ItemStack weapon, Inventory inventory) {
        NbtCompound nbt = weapon.getSubNbt("Arsenal");
        if (nbt == null) return false;

        boolean state = false;

        Pair<AmmoUnit, ItemStack> nextStack = getNextAdequateStack(weapon, inventory);
        int currentAmmo = nbt.getInt(KEY_CURRENT_AMMO);
        int maxAmmo = nbt.getInt(KEY_MAX_AMMO);

        while (currentAmmo < maxAmmo) {
            if (nextStack ==  null) break;

            loadBulletStack(weapon, nextStack.getLeft(), nextStack.getRight());
            state = true;

            currentAmmo = nbt.getInt(KEY_CURRENT_AMMO);
            nextStack = getNextAdequateStack(weapon, inventory);
        }

        return state;
    }

    protected void loadBulletStack(ItemStack weapon, AmmoUnit unit, ItemStack stack) {
        if (stack == null) return;

        NbtCompound nbt = weapon.getSubNbt("Arsenal");
        if (nbt == null) return;

        int currentAmmo = nbt.getInt(KEY_CURRENT_AMMO);
        int maxAmmo = nbt.getInt(KEY_MAX_AMMO);

        int ammoToDecrease = maxAmmo - currentAmmo;
        int ammoToRefill = Math.min(stack.getCount(), ammoToDecrease);

        stack.decrement(ammoToRefill);
        nbt.putInt(KEY_CURRENT_AMMO, currentAmmo + ammoToRefill);
        nbt.putString(KEY_LOADED_AMMO_NAME, unit.name);
    }

    protected Pair<AmmoUnit, ItemStack> getNextAdequateStack(ItemStack weapon, Inventory inventory) {
        NbtCompound nbt = weapon.getSubNbt("Arsenal");
        if (nbt == null) return null;

        for (int i = 0; i < inventory.size(); i++) {
            ItemStack current = inventory.getStack(i);

            if (!(current.getItem() instanceof AmmoUnit unit) || unit.context != this.context) continue;

            if (unit.name.equals(nbt.getString(KEY_LOADED_AMMO_NAME)) || nbt.getInt(KEY_CURRENT_AMMO) == 0)
                return new Pair<>(unit, current);
        }

        return null;

    }

    protected boolean mustReload(ItemStack weapon) {
        NbtCompound nbt = weapon.getSubNbt("Arsenal");
        if (nbt == null) return false;

        return nbt.getInt(KEY_CURRENT_AMMO) <= 0;
    }

    public boolean canReload(ItemStack weapon) {
        NbtCompound nbt = weapon.getSubNbt("Arsenal");
        if (nbt == null) return false;

        return nbt.getInt(KEY_CURRENT_AMMO) < nbt.getInt(KEY_MAX_AMMO);
    }

    protected void lowerCurrentAmmo(ItemStack weapon) {
        NbtCompound nbt = weapon.getSubNbt("Arsenal");

        if (nbt != null)
            nbt.putInt(KEY_CURRENT_AMMO, nbt.getInt(KEY_CURRENT_AMMO) - 1);
    }

    protected void playShootingSound(PlayerEntity user) {
    }

    protected void playReloadingSound(PlayerEntity user) {
        SoundUtils.playSoundFromPlayer(user, SoundEvents.ENTITY_FOX_SPIT, 0.5f, 0.5f);
        SoundUtils.playSoundFromPlayer(user, SoundEvents.ENTITY_TURTLE_EGG_CRACK, 0.25f, 2f);
        SoundUtils.playSoundFromPlayer(user, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 1f, 0.75f);
    }

    private void displayAmmoOnHUD(ItemStack stack, PlayerEntity entity) {
        NbtCompound nbt = stack.getSubNbt("Arsenal");
        if (nbt == null) return;

        entity.sendMessage(
                Text.of(nbt.getString(KEY_LOADED_AMMO_NAME)+ ": " + nbt.getInt(KEY_CURRENT_AMMO) + " / " + nbt.getInt(KEY_MAX_AMMO)),
                true
        );
    }

}
