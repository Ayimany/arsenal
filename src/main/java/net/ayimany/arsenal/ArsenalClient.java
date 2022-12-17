package net.ayimany.arsenal;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

public class ArsenalClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(
                ArsenalRegistry.BULLET_ENTITY_TYPE,
                FlyingItemEntityRenderer::new
        );
    }

}
