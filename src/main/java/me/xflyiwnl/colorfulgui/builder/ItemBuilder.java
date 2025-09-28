package me.xflyiwnl.colorfulgui.builder;

import me.xflyiwnl.colorfulgui.object.GuiItem;

/**
 * Generic interface for building GUI items.
 * This interface provides a common contract for all item builders.
 *
 * @param <T> The type of GuiItem being built
 */
public interface ItemBuilder<T extends GuiItem> {

    /**
     * Builds and returns the configured GUI item.
     *
     * @return The built GuiItem instance
     */
    T build();

}
