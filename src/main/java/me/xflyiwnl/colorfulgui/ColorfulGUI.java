package me.xflyiwnl.colorfulgui;

import me.xflyiwnl.colorfulgui.builder.inventory.DynamicGuiBuilder;
import me.xflyiwnl.colorfulgui.builder.inventory.StaticGuiBuilder;
import me.xflyiwnl.colorfulgui.builder.item.DynamicItemBuilder;
import me.xflyiwnl.colorfulgui.builder.item.StaticItemBuilder;
import me.xflyiwnl.colorfulgui.listener.GuiListener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main ColorfulGUI class that serves as the entry point for creating custom GUIs.
 * This class provides factory methods for creating GUI builders and item builders.
 */
public class ColorfulGUI extends JavaPlugin {

    private static JavaPlugin instance;

    public ColorfulGUI() {
        super();
    }

    @Override
    public void onEnable() {
        instance = this;
        registerListeners();
    }

    /**
     * Constructs a new ColorfulGUI instance and initializes the GUI system.
     * 
     * @param plugin The JavaPlugin instance that will host this GUI system
     */
    public ColorfulGUI(JavaPlugin plugin) {
        if (instance == null) {
            instance = plugin;
            registerListeners();
        }
    }

    private void registerListeners() {
        instance.getServer().getPluginManager().registerEvents(new GuiListener(), instance);
    }

    /**
     * Creates a new StaticItemBuilder for building static items.
     * Static items don't change their properties during GUI updates.
     * 
     * @return A new StaticItemBuilder instance
     */
    public StaticItemBuilder staticItem() {
        return new StaticItemBuilder();
    }

    /**
     * Creates a new DynamicItemBuilder for building dynamic items.
     * Dynamic items can change their properties during GUI updates.
     * 
     * @return A new DynamicItemBuilder instance
     */
    public DynamicItemBuilder dynamicItem() {
        return new DynamicItemBuilder();
    }

    /**
     * Creates a new StaticGuiBuilder for building static GUIs.
     * Static GUIs have a fixed number of items and pages.
     * 
     * @return A new StaticGuiBuilder instance
     */
    public StaticGuiBuilder gui() {
        return new StaticGuiBuilder();
    }

    /**
     * Creates a new DynamicGuiBuilder for building paginated GUIs.
     * Paginated GUIs can have multiple pages and dynamic content.
     * 
     * @return A new DynamicGuiBuilder instance for creating paginated GUIs
     */
    public DynamicGuiBuilder paginated() {
        return new DynamicGuiBuilder();
    }

    /**
     * Gets the JavaPlugin instance associated with this ColorfulGUI.
     * 
     * @return The JavaPlugin instance
     */
    public static JavaPlugin getInstance() {
        return instance;
    }

}
