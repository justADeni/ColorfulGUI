package me.xflyiwnl.colorfulgui.object.action;

import me.xflyiwnl.colorfulgui.object.event.ClickItemEvent;

/**
 * Interface for defining actions that occur when GUI items are clicked.
 * This is a generic interface that can handle different types of click events.
 *
 * @param <T> The type of ClickItemEvent this action handles
 */
public interface ClickAction<T extends ClickItemEvent> {

    /**
     * Executes the action when an item is clicked.
     *
     * @param execute The click event containing information about the click
     */
    void execute(T execute);

}
