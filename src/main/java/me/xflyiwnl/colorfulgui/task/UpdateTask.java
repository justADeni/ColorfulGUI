package me.xflyiwnl.colorfulgui.task;

import me.xflyiwnl.colorfulgui.object.DynamicItem;
import me.xflyiwnl.colorfulgui.object.GuiItem;
import me.xflyiwnl.colorfulgui.object.event.UpdateItemEvent;
import me.xflyiwnl.colorfulgui.provider.ColorfulProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Task responsible for updating dynamic GUI items at regular intervals.
 * This task runs periodically to update dynamic items and refresh the GUI.
 */
public class UpdateTask extends BukkitRunnable {

    private JavaPlugin plugin;
    private int updateTime;
    private ColorfulProvider<?> provider;
    private boolean started = false;

    /**
     * Creates a new UpdateTask with the specified parameters.
     *
     * @param plugin The plugin instance to schedule the task with
     * @param updateTime The interval between updates in ticks
     * @param provider The ColorfulProvider that owns the GUI being updated
     */
    public UpdateTask(JavaPlugin plugin, int updateTime, ColorfulProvider<?> provider) {
        this.plugin = plugin;
        this.updateTime = updateTime;
        this.provider = provider;
    }

    /**
     * Starts the update task if it hasn't been started already.
     * The task will run at the specified interval.
     */
    public void startTask() {
        if (started) return;
        started = true;
        this.runTaskTimer(plugin, 0, updateTime);
    }

    @Override
    public void run() {
        provider.update();

        Map<Integer, GuiItem> slotItems = new HashMap<Integer, GuiItem>(provider.getGui().getSetItems());
        for (Integer id : slotItems.keySet()) {
            GuiItem item = slotItems.get(id);
            updateItem(item);
        }
        LinkedList<GuiItem> addItems = new LinkedList<GuiItem>(provider.getGui().getAddItems());
        for (GuiItem item : addItems) {
            updateItem(item);
        }
        provider.getGui().render();
    }

    /**
     * Updates a single GUI item if it's a dynamic item with an update handler.
     *
     * @param item The GUI item to update
     */
    public void updateItem(GuiItem item) {
        if (item instanceof DynamicItem) {
            DynamicItem dynamicItem = (DynamicItem) item;
            if (dynamicItem.getOnUpdate() == null) return;
            UpdateItemEvent<DynamicItem> event = new UpdateItemEvent<DynamicItem>(dynamicItem);
            dynamicItem.getOnUpdate().execute(event);
        }
    }

    /**
     * Checks if the task has been started.
     *
     * @return true if the task is started, false otherwise
     */
    public boolean isStarted() {
        return started;
    }

    /**
     * Sets the started state of the task.
     *
     * @param started The new started state
     */
    public void setStarted(boolean started) {
        this.started = started;
    }
}
