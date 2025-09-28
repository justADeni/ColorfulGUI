package me.xflyiwnl.colorfulgui.object.action.click;

import me.xflyiwnl.colorfulgui.object.event.click.ClickStaticItemEvent;
import me.xflyiwnl.colorfulgui.object.action.ClickAction;

/**
 * Interface for defining click actions specifically for static items.
 * Static items have fixed properties and don't change during GUI updates.
 */
public interface ClickStaticAction extends ClickAction<ClickStaticItemEvent> {

    /**
     * Executes the action when a static item is clicked.
     *
     * @param event The click event containing information about the static item click
     */
    @Override
    void execute(ClickStaticItemEvent event);
}
