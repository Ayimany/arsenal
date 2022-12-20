package net.ayimany.arsenal.network;

import net.ayimany.arsenal.items.bases.FirearmBase;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

/**
 * Contains: Network event keys.<p>
 * Handles: Events, Packet Reception.
 **/
public class ArsenalNetworkHandler {

    /**
    * Identifier for arsenal keystroke packets <p>
    * Reload: 1
    **/
    public static final Identifier KEY_HANDLER = new Identifier("arsenal", "key_handler");

    /**
     * Registers all known receivers that belong to Arsenal events.
     * Things such as key presses and attachment events get REGISTERED here.
     * **/
    public static void registerReceivers() {
        ServerPlayNetworking.registerGlobalReceiver(KEY_HANDLER, ArsenalNetworkHandler::receiveKeystroke);
    }

    /**
     * Handles a key press event. {@code buf} must carry a written byte value to identify the keystroke type. <p>
     * Reference {@link ArsenalNetworkHandler#KEY_HANDLER} to see how each byte value is treated.
     * **/
    private static void receiveKeystroke(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        ItemStack stack = player.getMainHandStack();
        Item item = stack.getItem();

        if (!(item instanceof FirearmBase firearm)) return;
        if (firearm.canReload(stack) && !player.getItemCooldownManager().isCoolingDown(firearm)) firearm.reload(stack, player);
    }
}
