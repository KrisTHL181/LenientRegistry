package kris.lenientregistrysync;

import com.google.gson.*;
import java.io.*;
import java.nio.file.*;

public class Config {
    public boolean enabled = true;
    public String[] ignored_mods = {};

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = Path.of("config", "lenient-registry-sync.json");

    public static Config load() {
        Config config = new Config();

        if (Files.notExists(CONFIG_PATH)) {
            try {
                Files.createDirectories(CONFIG_PATH.getParent());
                Files.writeString(CONFIG_PATH, GSON.toJson(config));
                LenientRegistrySync.LOGGER.info("Created default config: lenient-registry-sync.json");
            } catch (IOException e) {
                LenientRegistrySync.LOGGER.warn("Failed to create config file!", e);
            }
            return config;
        }

        try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            config.enabled = json.get("enabled").getAsBoolean();
            JsonArray mods = json.getAsJsonArray("ignored_mods");
            config.ignored_mods = new String[mods.size()];
            for (int i = 0; i < mods.size(); i++) {
                config.ignored_mods[i] = mods.get(i).getAsString();
            }
        } catch (Exception e) {
            LenientRegistrySync.LOGGER.warn("Failed to load config, using defaults. Error: " + e.getMessage());
            return config;
        }

        return config;
    }

    public void save() {
        try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
            writer.write(GSON.toJson(this));
        } catch (IOException e) {
            LenientRegistrySync.LOGGER.warn("Failed to save config!", e);
        }
    }
}
