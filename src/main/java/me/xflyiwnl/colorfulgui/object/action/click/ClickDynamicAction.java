package me.xflyiwnl.colorfulgui.object.action.click;

import me.xflyiwnl.colorfulgui.object.event.click.ClickDynamicItemEvent;
import me.xflyiwnl.colorfulgui.object.action.ClickAction;

/**
 * Interface for defining click actions specifically for dynamic items.
 * Dynamic items can change their properties during GUI updates.
 */
public interface ClickDynamicAction extends ClickAction<ClickDynamicItemEvent> {
    
    /**
     * Executes the action when a dynamic item is clicked.
     *
     * @param event The click event containing information about the dynamic item click
     */
    @Override
    void execute(ClickDynamicItemEvent event);
}
