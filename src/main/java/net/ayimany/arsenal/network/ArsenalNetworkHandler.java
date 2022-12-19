package net.ayimany.arsenal.network;

import net.ayimany.arsenal.Main;
import net.ayimany.arsenal.items.bases.FirearmBase;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ArsenalNetworkHandler {
    /*
    * HANDLER TABLE
    * RELOAD: 1
    */
    public static final Identifier KEY_HANDLER = new Identifier("arsenal", "key_handler");

    public static void registerReceivers() {

        ServerPlayNetworking.registerGlobalReceiver(KEY_HANDLER, ArsenalNetworkHandler::receive);

    }

    private static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        // 1 for now. Will most likely change when attachments are introduced
        ItemStack stack = player.getMainHandStack();
        Item item = stack.getItem();

        if (!(item instanceof FirearmBase firearm)) return;
        if (firearm.canReload() && !player.getItemCooldownManager().isCoolingDown(firearm)) firearm.reload(player);
    }
}
