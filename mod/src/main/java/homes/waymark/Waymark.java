package homes.waymark;

import homes.waymark.ui.HomeListMenu;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Waymark implements ModInitializer {
    public static final String MOD_ID = "waymark";
    public static final int MAX_HOMES = 3;
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static HomeStore store;

    public static HomeStore store() {
        return store;
    }

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> store = HomeStore.load(server));
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            if (store != null) {
                store.save();
            }
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(Commands.literal("home").executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayerOrException();
                    HomeListMenu.open(player);
                    return 1;
                })));

        LOGGER.info("Waymark initialized — /home is ready.");
    }
}
