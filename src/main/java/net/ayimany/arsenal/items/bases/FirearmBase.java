package net.ayimany.arsenal.items.bases;

import net.ayimany.arsenal.entities.bases.BulletEntity;
import net.ayimany.arsenal.util.SoundUtils;
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

import java.util.Random;

public abstract class FirearmBase extends Item  {

    public static final String KEY_SHOT_DELAY_TICKS      = "ShotDelay";
    public static final String KEY_RELOAD_DELAY_TICKS    = "ReloadDelay";

    public static final String KEY_MAX_AMMO              = "MaxAmmo";
    public static final String KEY_CURRENT_AMMO          = "CurrentAmmo";

    public static final String KEY_DAMAGE_MULTIPLIER     = "DamageMultiplier";
    public static final String KEY_SHOT_STRENGTH         = "ShotStrength";

    public static final String KEY_LOADED_AMMO_NAME      = "LoadedAmmoName";

    Class<? extends AmmoUnit> unitType;
    protected Random random;

    protected final   int shotDelayTicks;
    protected final   int reloadDelayTicks;

    protected final   int maxAmmo;
    protected final float damageMultiplier;

    protected final float shotStrength;

    public static boolean isAvailable(FirearmBase firearm, PlayerEntity player) {
        return !player.getItemCooldownManager().isCoolingDown(firearm);
    }

    public static boolean mustReload(ItemStack weapon) {
        NbtCompound nbt = weapon.getOrCreateSubNbt("Arsenal");

        return nbt.getInt(KEY_CURRENT_AMMO) <= 0;
    }

    public static boolean canReload(ItemStack weapon) {
        NbtCompound nbt = weapon.getOrCreateSubNbt("Arsenal");

        return nbt.getInt(KEY_CURRENT_AMMO) < nbt.getInt(KEY_MAX_AMMO);
    }

    protected static boolean canShoot(ItemStack weapon) {
        NbtCompound nbt = weapon.getOrCreateSubNbt("Arsenal");

        return !(nbt.getString(KEY_LOADED_AMMO_NAME).equals("None"));
    }

    public FirearmBase(Class<? extends AmmoUnit> unitType, int shotDelayTicks, int reloadDelayTicks, int maxAmmo, float damageMultiplier, float shotStrength) {
        super(new Item.Settings().maxDamage((int)(3600 * maxAmmo * (shotStrength / 2f))));

        this.unitType = unitType;
        this.random = new Random();

        this.shotDelayTicks   = shotDelayTicks;
        this.reloadDelayTicks = reloadDelayTicks;
        this.maxAmmo          = maxAmmo;
        this.damageMultiplier = damageMultiplier;
        this.shotStrength     = shotStrength;

    }

    public void loadWeaponNbt(NbtCompound nbt) {

        nbt.putInt(KEY_SHOT_DELAY_TICKS, shotDelayTicks);
        nbt.putInt(KEY_RELOAD_DELAY_TICKS, reloadDelayTicks);

        nbt.putInt(KEY_MAX_AMMO, maxAmmo);
        nbt.putInt(KEY_CURRENT_AMMO, 0);

        nbt.putFloat(KEY_DAMAGE_MULTIPLIER, damageMultiplier);
        nbt.putFloat(KEY_SHOT_STRENGTH, shotStrength);

        nbt.putString(KEY_LOADED_AMMO_NAME, "None");

    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        ItemStack weapon = user.getStackInHand(hand);

        // Further code will do NO client checks.
        if (world.isClient) return TypedActionResult.pass(user.getStackInHand(hand));

        NbtCompound nbt = weapon.getSubNbt("Arsenal");
        if (nbt == null) loadWeaponNbt(nbt = weapon.getOrCreateSubNbt("Arsenal"));

        if (canShoot(weapon)) shoot(weapon, world, user);
        else if (mustReload(weapon)) reload(weapon, user);

        displayDetailsOnHUD(nbt, user);

        return TypedActionResult.success(user.getStackInHand(hand), false);
    }

    public final void shoot(ItemStack stack, World world, PlayerEntity user) {
        NbtCompound nbt = stack.getOrCreateSubNbt("Arsenal");

        playShootingSound(user);

        summonBullets(stack, world, user);
        applyRecoil(stack, user);
        lowerCurrentAmmo(stack);

        if (nbt.getInt(KEY_CURRENT_AMMO) == 0) nbt.putString(KEY_LOADED_AMMO_NAME, "None");
        applyShotCooldown(nbt, user);

        stack.damage(1, user, (entity -> {}));
    }

    protected void summonBullets(ItemStack weapon, World world, PlayerEntity user) {
        NbtCompound nbt = weapon.getOrCreateSubNbt("Arsenal");

        AmmoUnit currentUnit = AmmoUnit.getType(nbt.getString(KEY_LOADED_AMMO_NAME));

        for (BulletEntity entity: currentUnit.toEntity(world, user)) {

            entity.loadAmmoUnitProperties(currentUnit);
            entity.loadWeaponProperties(this, weapon);
            entity.loadProjectileProperties(user);

            world.spawnEntity(entity);
        }
    }

    protected void applyRecoil(ItemStack stack, PlayerEntity user) {
        NbtCompound nbt = stack.getOrCreateSubNbt("Arsenal");
        user.setPitch(user.getPitch() + nbt.getFloat(KEY_SHOT_STRENGTH));
    }

    protected void applyShotCooldown(NbtCompound nbt, PlayerEntity user) {
        user.getItemCooldownManager().set(this, nbt.getInt(KEY_SHOT_DELAY_TICKS));
    }

    protected void lowerCurrentAmmo(ItemStack weapon) {
        NbtCompound nbt = weapon.getOrCreateSubNbt("Arsenal");
        nbt.putInt(KEY_CURRENT_AMMO, nbt.getInt(KEY_CURRENT_AMMO) - 1);
    }

    public void reload(ItemStack weapon, PlayerEntity user) {
        NbtCompound nbt = weapon.getOrCreateSubNbt("Arsenal");

        boolean foundAmmo = findAndLoadBulletStackFrom(weapon, user.getInventory());

        if (!foundAmmo) return;

        playReloadingSound(user);
        applyReloadCooldown(nbt, user);
    }

    protected boolean findAndLoadBulletStackFrom(ItemStack weapon, Inventory inventory) {
        NbtCompound nbt = weapon.getOrCreateSubNbt("Arsenal");

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
        NbtCompound nbt = weapon.getOrCreateSubNbt("Arsenal");

        int currentAmmo = nbt.getInt(KEY_CURRENT_AMMO);
        int maxAmmo = nbt.getInt(KEY_MAX_AMMO);

        int ammoToDecrease = maxAmmo - currentAmmo;
        int ammoToRefill = Math.min(stack.getCount(), ammoToDecrease);

        stack.decrement(ammoToRefill);
        nbt.putInt(KEY_CURRENT_AMMO, currentAmmo + ammoToRefill);
        nbt.putString(KEY_LOADED_AMMO_NAME, unit.name);
    }

    protected Pair<AmmoUnit, ItemStack> getNextAdequateStack(ItemStack weapon, Inventory inventory) {
        NbtCompound nbt = weapon.getOrCreateSubNbt("Arsenal");

        for (int i = 0; i < inventory.size(); i++) {
            ItemStack current = inventory.getStack(i);

            if (!(unitType.isInstance(current.getItem()))) continue;
            AmmoUnit unit = (AmmoUnit) current.getItem();

            if (unit.name.equals(nbt.getString(KEY_LOADED_AMMO_NAME)) || nbt.getInt(KEY_CURRENT_AMMO) == 0)
                return new Pair<>(unit, current);
        }

        return null;

    }

    protected void applyReloadCooldown(NbtCompound nbt, PlayerEntity user) {
        user.getItemCooldownManager().set(this, nbt.getInt(KEY_RELOAD_DELAY_TICKS));
    }

    private void displayDetailsOnHUD(NbtCompound nbt, PlayerEntity entity) {
        entity.sendMessage(Text.of(
                nbt.getString(KEY_LOADED_AMMO_NAME) + ": " +
                        nbt.getInt(KEY_CURRENT_AMMO) + " / " +
                        nbt.getInt(KEY_MAX_AMMO)),
                true
        );
    }

    protected void playShootingSound(PlayerEntity user) {
    }

    protected void playReloadingSound(PlayerEntity user) {
        SoundUtils.playSoundFromPlayer(user, SoundEvents.ENTITY_FOX_SPIT, 0.5f, 0.5f);
        SoundUtils.playSoundFromPlayer(user, SoundEvents.ENTITY_TURTLE_EGG_CRACK, 0.25f, 2f);
        SoundUtils.playSoundFromPlayer(user, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 1f, 0.75f);
    }

}
