package homes.waymark.ui;

import homes.waymark.Home;
import homes.waymark.Waymark;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Locale;

/**
 * The 3-row chest menu shown by /home. One button per home slot: empty slots
 * show "New Home" and create a home where the player stands; occupied slots
 * open the per-home options menu.
 */
public class HomeListMenu extends ChestMenu {
    static final int[] HOME_SLOTS = {11, 13, 15};
    private static final int ROWS = 3;

    private final ServerPlayer player;

    private HomeListMenu(int syncId, Inventory playerInventory, ServerPlayer player) {
        super(MenuType.GENERIC_9x3, syncId, playerInventory, new SimpleContainer(ROWS * 9), ROWS);
        this.player = player;
        refresh();
    }

    public static void open(ServerPlayer player) {
        player.openMenu(new SimpleMenuProvider(
                (syncId, inventory, p) -> new HomeListMenu(syncId, inventory, player),
                Component.literal("Waymark — Your Homes")));
    }

    private void refresh() {
        ItemStack filler = Ui.button(Ui.item("gray_stained_glass_pane"), Component.empty());
        for (int i = 0; i < ROWS * 9; i++) {
            getContainer().setItem(i, filler.copy());
        }
        Home[] homes = Waymark.store().getHomes(player.getUUID());
        for (int i = 0; i < HOME_SLOTS.length; i++) {
            Home home = homes[i];
            ItemStack button;
            if (home == null) {
                button = Ui.button(Ui.item("light_gray_bed"),
                        Ui.name("New Home", ChatFormatting.GRAY),
                        Ui.loreLine("Click to set a home"),
                        Ui.loreLine("right where you stand."));
            } else {
                button = Ui.button(Ui.item("red_bed"),
                        Ui.name(home.name(), ChatFormatting.GREEN, ChatFormatting.BOLD),
                        Ui.loreLine(String.format(Locale.ROOT, "%.0f, %.0f, %.0f", home.x(), home.y(), home.z())),
                        Ui.loreLine(prettyDimension(home.dimension())),
                        Component.empty(),
                        Ui.loreLine("Click for options."));
            }
            getContainer().setItem(HOME_SLOTS[i], button);
        }
    }

    private static String prettyDimension(String dimension) {
        String path = dimension.contains(":") ? dimension.substring(dimension.indexOf(':') + 1) : dimension;
        return switch (path) {
            case "overworld" -> "Overworld";
            case "the_nether" -> "The Nether";
            case "the_end" -> "The End";
            default -> path;
        };
    }

    @Override
    public void clicked(int slotId, int button, ContainerInput input, Player clicker) {
        for (int i = 0; i < HOME_SLOTS.length; i++) {
            if (slotId == HOME_SLOTS[i]) {
                handleHomeClick(i);
                return;
            }
        }
        // Swallow every other interaction: this menu is buttons only.
    }

    private void handleHomeClick(int slot) {
        Home home = Waymark.store().getHome(player.getUUID(), slot);
        if (home == null) {
            NameInputMenu.open(player, "Home " + (slot + 1), name -> {
                Home created = new Home(
                        name,
                        player.level().dimension().identifier().toString(),
                        player.getX(), player.getY(), player.getZ(),
                        player.getYRot(), player.getXRot());
                Waymark.store().setHome(player.getUUID(), slot, created);
                player.sendSystemMessage(
                        Component.literal("Home \"" + name + "\" set.").withStyle(ChatFormatting.GREEN));
                HomeListMenu.open(player);
            });
        } else {
            HomeActionsMenu.open(player, slot);
        }
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
