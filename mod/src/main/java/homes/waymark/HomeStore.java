package homes.waymark;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Per-world storage for player homes, kept in {@code <world>/waymark_homes.json}.
 * Every mutation writes the file immediately, so a crash never loses more than
 * the in-flight change.
 */
public final class HomeStore {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    private static final Type FILE_TYPE = new TypeToken<Map<UUID, Home[]>>() {}.getType();

    private final Path file;
    private final Map<UUID, Home[]> homes = new HashMap<>();

    private HomeStore(Path file) {
        this.file = file;
    }

    public static HomeStore load(MinecraftServer server) {
        Path file = server.getWorldPath(LevelResource.ROOT).resolve("waymark_homes.json");
        HomeStore store = new HomeStore(file);
        if (Files.exists(file)) {
            try (Reader reader = Files.newBufferedReader(file)) {
                Map<UUID, Home[]> loaded = GSON.fromJson(reader, FILE_TYPE);
                if (loaded != null) {
                    loaded.forEach((uuid, arr) ->
                            store.homes.put(uuid, Arrays.copyOf(arr, Waymark.MAX_HOMES)));
                }
            } catch (Exception e) {
                Waymark.LOGGER.error("Failed to read {}, starting with empty home list", file, e);
            }
        }
        return store;
    }

    /** Returns the player's home slots; entries may be null. Do not mutate. */
    public Home[] getHomes(UUID player) {
        return homes.computeIfAbsent(player, u -> new Home[Waymark.MAX_HOMES]);
    }

    public Home getHome(UUID player, int slot) {
        return getHomes(player)[slot];
    }

    public void setHome(UUID player, int slot, Home home) {
        getHomes(player)[slot] = home;
        save();
    }

    public void renameHome(UUID player, int slot, String newName) {
        Home old = getHome(player, slot);
        if (old != null) {
            getHomes(player)[slot] = new Home(newName, old.dimension(), old.x(), old.y(), old.z(), old.yaw(), old.pitch());
            save();
        }
    }

    public void deleteHome(UUID player, int slot) {
        getHomes(player)[slot] = null;
        save();
    }

    public synchronized void save() {
        try {
            Path tmp = file.resolveSibling(file.getFileName() + ".tmp");
            try (Writer writer = Files.newBufferedWriter(tmp)) {
                GSON.toJson(homes, FILE_TYPE, writer);
            }
            Files.move(tmp, file, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            Waymark.LOGGER.error("Failed to save homes to {}", file, e);
        }
    }
}
