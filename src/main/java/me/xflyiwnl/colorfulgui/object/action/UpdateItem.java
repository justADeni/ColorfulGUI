package me.xflyiwnl.colorfulgui.object.action;

import me.xflyiwnl.colorfulgui.object.event.UpdateItemEvent;

/**
 * Interface for defining actions that occur when GUI items are updated.
 * This is typically used with dynamic items that change over time.
 *
 * @param <T> The type of UpdateItemEvent this action handles
 */
public interface UpdateItem<T extends UpdateItemEvent> {

    /**
     * Executes the action when an item is updated.
     *
     * @param event The update event containing information about the update
     */
    void execute(T event);

}
