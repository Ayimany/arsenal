package net.ayimany.arsenal;

import net.ayimany.arsenal.network.ArsenalNetworkHandler;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("arsenal");

	@Override
	public void onInitialize() {
		LOGGER.info("INITIALIZED.");
		ArsenalRegistry.registerAll();
		ArsenalNetworkHandler.registerReceivers();
	}
}
