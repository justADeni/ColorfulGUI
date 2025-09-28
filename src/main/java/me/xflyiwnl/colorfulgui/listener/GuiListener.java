package me.xflyiwnl.colorfulgui.listener;

import me.xflyiwnl.colorfulgui.object.event.click.ClickDynamicItemEvent;
import me.xflyiwnl.colorfulgui.object.event.click.ClickStaticItemEvent;
import me.xflyiwnl.colorfulgui.provider.ColorfulProvider;
import me.xflyiwnl.colorfulgui.ColorfulGUI;
import me.xflyiwnl.colorfulgui.object.DynamicItem;
import me.xflyiwnl.colorfulgui.object.Gui;
import me.xflyiwnl.colorfulgui.object.GuiItem;
import me.xflyiwnl.colorfulgui.object.StaticItem;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

/**
 * Event listener for handling ColorfulGUI inventory events.
 * This listener processes clicks, opens, closes, and drags for ColorfulGUI inventories.
 */
public class GuiListener implements Listener {

    /**
     * Handles inventory click events for ColorfulGUI inventories.
     * This method processes item clicks and executes appropriate actions.
     *
     * @param event The inventory click event
     */
    @EventHandler
    public void onClick(InventoryClickEvent event) {

        Inventory inventory = event.getInventory();
        ItemStack itemStack = event.getCurrentItem();

        if (inventory == null) {
            return;
        }


        InventoryHolder holder = inventory.getHolder();
        if (holder == null) {
            return;
        }

        if (!(holder instanceof ColorfulProvider)) {
            return;
        }

        ColorfulProvider<Gui> provider = (ColorfulProvider<Gui>) holder;
        provider.onClick(event);

        if (itemStack != null) {

            PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey(ColorfulGUI.getInstance(), "colorfulgui");
            if (!container.has(key, PersistentDataType.STRING)) {
                return;
            }
            UUID uuid = UUID.fromString(container.get(key, PersistentDataType.STRING).toString());
            GuiItem item = provider.getGui().getItem(uuid);
            if (item == null) {
                return;
            }

            if (item.getAction() != null) {
                if (item instanceof StaticItem) {
                    StaticItem staticItem = (StaticItem) item;
                    ClickStaticItemEvent clickEvent = new ClickStaticItemEvent(
                            staticItem,
                            event.getAction(),
                            event.getClick(),
                            event.getClickedInventory(),
                            event.getCursor(),
                            event.getSlot(),
                            event.getSlotType());
                    staticItem.getAction().execute(clickEvent);
                }
                if (item instanceof DynamicItem) {
                    DynamicItem dynamicItem = (DynamicItem) item;
                    ClickDynamicItemEvent clickEvent = new ClickDynamicItemEvent(
                            dynamicItem,
                            event.getAction(),
                            event.getClick(),
                            event.getClickedInventory(),
                            event.getCursor(),
                            event.getSlot(),
                            event.getSlotType());
                    dynamicItem.getAction().execute(clickEvent);
                }
            }
        }


    }

    /**
     * Handles inventory open events for ColorfulGUI inventories.
     * This method starts update tasks and calls provider open handlers.
     *
     * @param event The inventory open event
     */
    @EventHandler
    public void onOpen(InventoryOpenEvent event) {

        Inventory inventory = event.getInventory();
        if (inventory == null) {
            return;
        }

        InventoryHolder holder = inventory.getHolder();
        if (holder == null) {
            return;
        }

        if (!(holder instanceof ColorfulProvider)) {
            return;
        }

        ColorfulProvider<Gui> provider = (ColorfulProvider<Gui>) holder;

        if (provider.getTask() != null) {
            provider.getTask().startTask();
        }

        provider.onOpen(event);

    }

    /**
     * Handles inventory close events for ColorfulGUI inventories.
     * This method stops update tasks and calls provider close handlers.
     *
     * @param event The inventory close event
     */
    @EventHandler
    public void onClose(InventoryCloseEvent event) {

        Inventory inventory = event.getInventory();
        if (inventory == null) {
            return;
        }

        InventoryHolder holder = inventory.getHolder();
        if (holder == null) {
            return;
        }

        if (!(holder instanceof ColorfulProvider)) {
            return;
        }

        ColorfulProvider<Gui> provider = (ColorfulProvider<Gui>) holder;

        if (provider.getTask() != null) {
            provider.getTask().cancel();
        }
        provider.onClose(event);

    }

    /**
     * Handles inventory drag events for ColorfulGUI inventories.
     * This method processes item dragging and calls provider drag handlers.
     *
     * @param event The inventory drag event
     */
    @EventHandler
    public void onDrag(InventoryDragEvent event) {

        Inventory inventory = event.getInventory();
        if (inventory == null) {
            return;
        }

        InventoryHolder holder = inventory.getHolder();
        if (holder == null) {
            return;
        }

        if (!(holder instanceof ColorfulProvider)) {
            return;
        }

        ColorfulProvider<Gui> provider = (ColorfulProvider<Gui>) holder;

        provider.onDrag(event);

    }

}
