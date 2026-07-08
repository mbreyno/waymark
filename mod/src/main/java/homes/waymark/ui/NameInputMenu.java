package homes.waymark.ui;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

/**
 * Anvil-based text input. The player edits the name in the anvil's text field
 * and clicks the result item to confirm. Costs nothing and never touches real
 * items.
 */
public class NameInputMenu extends AnvilMenu {
    private final ServerPlayer player;
    private final Consumer<String> onConfirm;
    private String currentName;
    private boolean confirmed;

    private NameInputMenu(int syncId, Inventory playerInventory, ServerPlayer player,
                          String initialName, Consumer<String> onConfirm) {
        super(syncId, playerInventory, ContainerLevelAccess.NULL);
        this.player = player;
        this.onConfirm = onConfirm;
        this.currentName = initialName;

        ItemStack input = new ItemStack(Items.PAPER);
        input.set(DataComponents.CUSTOM_NAME, Component.literal(initialName));
        this.inputSlots.setItem(0, input);
        createResult();
    }

    public static void open(ServerPlayer player, String initialName, Consumer<String> onConfirm) {
        player.openMenu(new SimpleMenuProvider(
                (syncId, inventory, p) -> new NameInputMenu(syncId, inventory, player, initialName, onConfirm),
                Component.literal("Name this home")));
    }

    @Override
    public boolean setItemName(String name) {
        String cleaned = name == null ? "" : name.strip();
        this.currentName = cleaned.isEmpty() ? "Home" : cleaned;
        createResult();
        return true;
    }

    @Override
    public void createResult() {
        ItemStack result = new ItemStack(Items.NAME_TAG);
        result.set(DataComponents.ITEM_NAME,
                Component.literal("Save as \"" + currentName + "\"").withStyle(ChatFormatting.GREEN));
        this.resultSlots.setItem(0, result);
        broadcastChanges();
    }

    @Override
    protected boolean mayPickup(Player clicker, boolean hasStack) {
        return true;
    }

    @Override
    protected void onTake(Player clicker, ItemStack stack) {
        setCarried(ItemStack.EMPTY);
        if (!confirmed) {
            confirmed = true;
            onConfirm.accept(currentName);
        }
    }

    @Override
    public boolean stillValid(Player clicker) {
        return true;
    }

    @Override
    public void removed(Player clicker) {
        // Nothing to give back: the input paper is virtual.
    }
}
