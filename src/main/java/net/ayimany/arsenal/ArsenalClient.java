package net.ayimany.arsenal;

import net.ayimany.arsenal.network.ArsenalNetworkHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.server.integrated.IntegratedServer;
import org.lwjgl.glfw.GLFW;

@SuppressWarnings("unused")
public class ArsenalClient implements ClientModInitializer {

    public static final KeyBinding RELOAD_BIND = KeyBindingHelper.registerKeyBinding(
            new KeyBinding(
                    "key.arsenal.reload",
                    InputUtil.Type.KEYSYM,
                    GLFW.GLFW_KEY_R,
                    "category.arsenal.weapons"
            )
    );

    private static void onEndTick(MinecraftClient client) {
        if (RELOAD_BIND.wasPressed()) {

            PacketByteBuf buffer = PacketByteBufs.create();
            buffer.writeByte(1);

            IntegratedServer server = client.getServer();
            if (client.player == null || server == null) return;

            var packet = new CustomPayloadC2SPacket(ArsenalNetworkHandler.KEY_HANDLER, buffer);
            client.player.networkHandler.sendPacket(packet);

        }
    }

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register((ArsenalClient::onEndTick));

        EntityRendererRegistry.register(
                ArsenalRegistry.BULLET_ENTITY,
                FlyingItemEntityRenderer::new
        );

    }

}
