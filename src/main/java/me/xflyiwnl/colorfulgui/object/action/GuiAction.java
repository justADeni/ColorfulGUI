package me.xflyiwnl.colorfulgui.object.action;

import org.bukkit.event.Event;

/**
 * Interface for defining actions that occur in response to GUI events.
 * This is a generic interface that can handle different types of Bukkit events.
 *
 * @param <T> The type of Event this action handles
 */
public interface GuiAction<T extends Event> {

    /**
     * Executes the action when a GUI event occurs.
     *
     * @param event The event that triggered this action
     */
    void execute(T event);

}
