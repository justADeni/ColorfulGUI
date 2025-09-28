package me.xflyiwnl.colorfulgui.object.event;

import me.xflyiwnl.colorfulgui.object.GuiItem;

/**
 * Event class containing information about a GUI item update.
 * This event is triggered when dynamic items are updated during GUI refresh cycles.
 *
 * @param <T> The type of GuiItem being updated
 */
public class UpdateItemEvent<T extends GuiItem> {

    private T item;

    /**
     * Creates a new UpdateItemEvent for the specified item.
     *
     * @param item The GUI item being updated
     */
    public UpdateItemEvent(T item) {
        this.item = item;
    }

    /**
     * Gets the GUI item being updated.
     *
     * @return The GUI item
     */
    public T getItem() {
        return item;
    }

}
