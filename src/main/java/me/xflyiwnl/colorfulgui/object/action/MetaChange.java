package me.xflyiwnl.colorfulgui.object.action;

import org.bukkit.inventory.meta.ItemMeta;

/**
 * Interface for defining custom modifications to ItemMeta.
 * This allows for custom item metadata changes during item creation.
 *
 * @param <T> The type of ItemMeta to modify
 */
public interface MetaChange<T extends ItemMeta> {
    
    /**
     * Executes custom modifications on the provided ItemMeta.
     *
     * @param t The ItemMeta to modify
     */
    void execute(T t);
}
