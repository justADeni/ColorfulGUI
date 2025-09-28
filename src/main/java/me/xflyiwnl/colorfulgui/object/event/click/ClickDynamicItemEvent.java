package me.xflyiwnl.colorfulgui.object.event.click;

import me.xflyiwnl.colorfulgui.object.DynamicItem;
import me.xflyiwnl.colorfulgui.object.event.ClickItemEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Event class specifically for dynamic item clicks.
 * This extends ClickItemEvent with dynamic item specific functionality.
 */
public class ClickDynamicItemEvent extends ClickItemEvent {

    private DynamicItem currentItem;

    /**
     * Creates a new ClickDynamicItemEvent with the specified parameters.
     *
     * @param currentItem The dynamic item that was clicked
     * @param action The inventory action that was performed
     * @param click The type of click that occurred
     * @param clickedInventory The inventory that was clicked
     * @param cursor The item stack on the cursor
     * @param slot The slot that was clicked
     * @param slotType The type of slot that was clicked
     */
    public ClickDynamicItemEvent(DynamicItem currentItem, InventoryAction action, ClickType click, Inventory clickedInventory, ItemStack cursor, Integer slot, InventoryType.SlotType slotType) {
        super(currentItem, action, click, clickedInventory, cursor, slot, slotType);
        this.currentItem = currentItem;
    }

    /**
     * Gets the dynamic item that was clicked.
     *
     * @return The clicked dynamic item
     */
    public DynamicItem getCurrentItem() {
        return currentItem;
    }

}
