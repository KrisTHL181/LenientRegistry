package kris.lenientregistry;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LenientRegistry implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("lenient-registry");
    public static Config CONFIG;

    @Override
    public void onInitializeClient() {
        CONFIG = Config.load();
        LOGGER.info("Lenient Registry Sync loaded! Config: enabled={}, IgnoreMods={}", CONFIG.enabled, CONFIG.ignored_mods);
    }
}
