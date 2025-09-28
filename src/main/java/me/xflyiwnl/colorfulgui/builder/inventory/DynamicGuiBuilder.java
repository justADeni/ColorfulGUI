package me.xflyiwnl.colorfulgui.builder.inventory;

import me.xflyiwnl.colorfulgui.provider.ColorfulProvider;
import me.xflyiwnl.colorfulgui.builder.GuiBuilder;
import me.xflyiwnl.colorfulgui.object.GuiMask;
import me.xflyiwnl.colorfulgui.object.PaginatedGui;

import java.util.Arrays;
import java.util.List;

public class DynamicGuiBuilder implements GuiBuilder<PaginatedGui, DynamicGuiBuilder> {

    private String title;
    private int rows;
    private GuiMask mask = new GuiMask();
    private ColorfulProvider<PaginatedGui> holder;

    public DynamicGuiBuilder() {
    }

    @Override
    public DynamicGuiBuilder holder(ColorfulProvider<PaginatedGui> holder) {
        this.holder = holder;
        return this;
    }

    @Override
    public DynamicGuiBuilder title(String title) {
        this.title = title;
        return this;
    }

    @Override
    public DynamicGuiBuilder rows(int rows) {
        this.rows = rows;
        return this;
    }

    @Override
    public DynamicGuiBuilder mask(List<String> mask) {
        this.mask.setMask(mask);
        return this;
    }

    @Override
    public DynamicGuiBuilder mask(String... mask) {
        this.mask.setMask(Arrays.asList(mask));
        return this;
    }

    @Override
    public PaginatedGui build() {
        PaginatedGui gui = new PaginatedGui(getHolder(), getTitle(), getRows(), getMask());
        gui.setHolder(holder);
        getMask().setGui(gui);
        getHolder().setGui(gui);
        getHolder().init();
        getHolder().show();
        return gui;
    }

    /**
     * Gets the current title set for the GUI.
     *
     * @return The GUI title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the current number of rows set for the GUI.
     *
     * @return The number of rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * Gets the current mask set for the GUI.
     *
     * @return The GuiMask instance
     */
    public GuiMask getMask() {
        return mask;
    }

    /**
     * Gets the current holder set for the GUI.
     *
     * @return The ColorfulProvider holder for PaginatedGui
     */
    public ColorfulProvider<PaginatedGui> getHolder() {
        return holder;
    }
}
