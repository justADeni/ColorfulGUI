package me.xflyiwnl.colorfulgui.builder;

import me.xflyiwnl.colorfulgui.provider.ColorfulProvider;
import me.xflyiwnl.colorfulgui.object.Gui;

import java.util.List;

public interface GuiBuilder<T extends Gui, B extends GuiBuilder<T, B>> {

    B holder(ColorfulProvider<T> holder);

    B title(String title);

    B rows(int rows);

    B mask(List<String> mask);

    B mask(String... mask);

    T build();

}
