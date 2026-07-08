package homes.waymark.ui;

import homes.waymark.Home;
import homes.waymark.Waymark;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Set;

/**
 * Options for one existing home: teleport, rename, or delete, plus a back
 * button that returns to the home list.
 */
public class HomeActionsMenu extends ChestMenu {
    private static final int ROWS = 3;
    private static final int SLOT_TELEPORT = 11;
    private static final int SLOT_RENAME = 13;
    private static final int SLOT_DELETE = 15;
    private static final int SLOT_BACK = 22;

    private final ServerPlayer player;
    private final int homeSlot;

    private HomeActionsMenu(int syncId, Inventory playerInventory, ServerPlayer player, int homeSlot) {
        super(MenuType.GENERIC_9x3, syncId, playerInventory, new SimpleContainer(ROWS * 9), ROWS);
        this.player = player;
        this.homeSlot = homeSlot;
        build();
    }

    public static void open(ServerPlayer player, int homeSlot) {
        Home home = Waymark.store().getHome(player.getUUID(), homeSlot);
        if (home == null) {
            HomeListMenu.open(player);
            return;
        }
        player.openMenu(new SimpleMenuProvider(
                (syncId, inventory, p) -> new HomeActionsMenu(syncId, inventory, player, homeSlot),
                Component.literal(home.name())));
    }

    private void build() {
        ItemStack filler = Ui.button(Ui.item("gray_stained_glass_pane"), Component.empty());
        for (int i = 0; i < ROWS * 9; i++) {
            getContainer().setItem(i, filler.copy());
        }
        getContainer().setItem(SLOT_TELEPORT, Ui.button(Items.ENDER_PEARL,
                Ui.name("Teleport", ChatFormatting.AQUA, ChatFormatting.BOLD),
                Ui.loreLine("Go to this home.")));
        getContainer().setItem(SLOT_RENAME, Ui.button(Items.NAME_TAG,
                Ui.name("Rename", ChatFormatting.YELLOW, ChatFormatting.BOLD),
                Ui.loreLine("Give this home a new name.")));
        getContainer().setItem(SLOT_DELETE, Ui.button(Items.BARRIER,
                Ui.name("Delete", ChatFormatting.RED, ChatFormatting.BOLD),
                Ui.loreLine("Remove this home. The slot"),
                Ui.loreLine("goes back to \"New Home\".")));
        getContainer().setItem(SLOT_BACK, Ui.button(Items.ARROW,
                Ui.name("Back", ChatFormatting.WHITE),
                Ui.loreLine("Return to your homes.")));
    }

    @Override
    public void clicked(int slotId, int button, ContainerInput input, Player clicker) {
        Home home = Waymark.store().getHome(player.getUUID(), homeSlot);
        if (home == null) {
            HomeListMenu.open(player);
            return;
        }
        switch (slotId) {
            case SLOT_TELEPORT -> teleport(home);
            case SLOT_RENAME -> rename(home);
            case SLOT_DELETE -> delete(home);
            case SLOT_BACK -> HomeListMenu.open(player);
            default -> {
                // Buttons only; ignore everything else.
            }
        }
    }

    private void teleport(Home home) {
        ServerLevel level = player.level().getServer().getLevel(
                ResourceKey.create(Registries.DIMENSION, Identifier.parse(home.dimension())));
        if (level == null) {
            player.closeContainer();
            player.sendSystemMessage(
                    Component.literal("That home's dimension no longer exists.").withStyle(ChatFormatting.RED));
            return;
        }
        player.closeContainer();
        player.teleportTo(level, home.x(), home.y(), home.z(), Set.of(), home.yaw(), home.pitch(), false);
        level.playSound(null, BlockPos.containing(home.x(), home.y(), home.z()),
                SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0f, 1.0f);
        player.sendOverlayMessage(
                Component.literal("Welcome to " + home.name() + ".").withStyle(ChatFormatting.AQUA));
    }

    private void rename(Home home) {
        NameInputMenu.open(player, home.name(), newName -> {
            Waymark.store().renameHome(player.getUUID(), homeSlot, newName);
            player.sendSystemMessage(
                    Component.literal("Renamed to \"" + newName + "\".").withStyle(ChatFormatting.YELLOW));
            HomeListMenu.open(player);
        });
    }

    private void delete(Home home) {
        Waymark.store().deleteHome(player.getUUID(), homeSlot);
        player.sendSystemMessage(
                Component.literal("Home \"" + home.name() + "\" deleted.").withStyle(ChatFormatting.RED));
        HomeListMenu.open(player);
    }

    @Override
    public ItemStack quickMoveStack(Player clicker, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, net.minecraft.world.inventory.Slot slot) {
        return false;
    }

    @Override
    public boolean stillValid(Player clicker) {
        return true;
    }
}
