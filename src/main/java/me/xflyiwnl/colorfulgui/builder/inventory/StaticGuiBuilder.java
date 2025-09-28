package me.xflyiwnl.colorfulgui.builder.inventory;

import me.xflyiwnl.colorfulgui.provider.ColorfulProvider;
import me.xflyiwnl.colorfulgui.builder.GuiBuilder;
import me.xflyiwnl.colorfulgui.object.Gui;
import me.xflyiwnl.colorfulgui.object.GuiMask;

import java.util.Arrays;
import java.util.List;

public class StaticGuiBuilder implements GuiBuilder<Gui, StaticGuiBuilder> {

    private String title;
    private int rows;
    private GuiMask mask = new GuiMask();
    private ColorfulProvider<Gui> holder;

    public StaticGuiBuilder() {
    }

    @Override
    public StaticGuiBuilder holder(ColorfulProvider<Gui> holder) {
        this.holder = holder;
        return this;
    }

    @Override
    public StaticGuiBuilder title(String title) {
        this.title = title;
        return this;
    }

    @Override
    public StaticGuiBuilder rows(int rows) {
        this.rows = rows;
        return this;
    }

    @Override
    public StaticGuiBuilder mask(List<String> mask) {
        this.mask.setMask(mask);
        return this;
    }

    @Override
    public StaticGuiBuilder mask(String... mask) {
        this.mask.setMask(Arrays.asList(mask));
        return this;
    }

    @Override
    public Gui build() {
        Gui gui = new Gui(getHolder(), getTitle(), getRows(), getMask());
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
     * @return The ColorfulProvider holder
     */
    public ColorfulProvider<Gui> getHolder() {
        return holder;
    }

}
