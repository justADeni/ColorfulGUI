package me.xflyiwnl.colorfulgui.object.event;

import me.xflyiwnl.colorfulgui.object.GuiItem;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Event class containing information about a GUI item click.
 * This class encapsulates all the relevant data from an inventory click event.
 */
public class ClickItemEvent {

    private GuiItem currentItem;
    private InventoryAction action;
    private ClickType click;
    private Inventory clickedInventory;
    private ItemStack cursor;
    private Integer slot;
    private InventoryType.SlotType slotType;

    /**
     * Creates a new ClickItemEvent with the specified parameters.
     *
     * @param currentItem The GUI item that was clicked
     * @param action The inventory action that was performed
     * @param click The type of click that occurred
     * @param clickedInventory The inventory that was clicked
     * @param cursor The item stack on the cursor
     * @param slot The slot that was clicked
     * @param slotType The type of slot that was clicked
     */
    public ClickItemEvent(GuiItem currentItem, InventoryAction action, ClickType click, Inventory clickedInventory, ItemStack cursor, Integer slot, InventoryType.SlotType slotType) {
        this.currentItem = currentItem;
        this.action = action;
        this.click = click;
        this.clickedInventory = clickedInventory;
        this.cursor = cursor;
        this.slot = slot;
        this.slotType = slotType;
    }

    /**
     * Gets the GUI item that was clicked.
     *
     * @return The clicked GUI item
     */
    public GuiItem getCurrentItem() {
        return currentItem;
    }

    /**
     * Gets the inventory action that was performed.
     *
     * @return The inventory action
     */
    public InventoryAction getAction() {
        return action;
    }

    /**
     * Gets the type of click that occurred.
     *
     * @return The click type
     */
    public ClickType getClick() {
        return click;
    }

    /**
     * Gets the inventory that was clicked.
     *
     * @return The clicked inventory
     */
    public Inventory getClickedInventory() {
        return clickedInventory;
    }

    /**
     * Gets the item stack on the cursor.
     *
     * @return The cursor item stack
     */
    public ItemStack getCursor() {
        return cursor;
    }

    /**
     * Gets the slot that was clicked.
     *
     * @return The clicked slot number
     */
    public Integer getSlot() {
        return slot;
    }

    /**
     * Gets the type of slot that was clicked.
     *
     * @return The slot type
     */
    public InventoryType.SlotType getSlotType() {
        return slotType;
    }
}
