package me.xflyiwnl.colorfulgui.builder;

import me.xflyiwnl.colorfulgui.provider.ColorfulProvider;
import me.xflyiwnl.colorfulgui.object.Gui;

import java.util.List;

/**
 * Generic interface for building GUIs with customizable properties.
 * This interface provides a fluent API for configuring GUI properties.
 *
 * @param <T> The type of GUI being built
 * @param <B> The type of builder (for method chaining)
 */
public interface GuiBuilder<T extends Gui, B extends GuiBuilder<T, B>> {

    /**
     * Sets the holder for the GUI.
     * The holder handles events and provides GUI logic.
     *
     * @param holder The ColorfulProvider that will handle this GUI
     * @return This builder instance for method chaining
     */
    B holder(ColorfulProvider<T> holder);

    /**
     * Sets the title for the GUI.
     *
     * @param title The title to display at the top of the GUI
     * @return This builder instance for method chaining
     */
    B title(String title);

    /**
     * Sets the number of rows for the GUI.
     *
     * @param rows The number of rows (each row contains 9 slots)
     * @return This builder instance for method chaining
     */
    B rows(int rows);

    /**
     * Sets the mask pattern for the GUI using a list of strings.
     * Each string represents a row, and each character represents a slot.
     *
     * @param mask The mask pattern as a list of strings
     * @return This builder instance for method chaining
     */
    B mask(List<String> mask);

    /**
     * Sets the mask pattern for the GUI using varargs.
     * Each string represents a row, and each character represents a slot.
     *
     * @param mask The mask pattern as varargs strings
     * @return This builder instance for method chaining
     */
    B mask(String... mask);

    /**
     * Builds and returns the configured GUI.
     *
     * @return The built GUI instance
     */
    T build();

}
