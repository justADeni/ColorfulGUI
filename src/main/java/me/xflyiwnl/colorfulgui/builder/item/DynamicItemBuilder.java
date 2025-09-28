package me.xflyiwnl.colorfulgui.builder.item;

import me.xflyiwnl.colorfulgui.object.StaticItem;
import me.xflyiwnl.colorfulgui.object.action.click.ClickDynamicAction;
import me.xflyiwnl.colorfulgui.ColorfulGUI;
import me.xflyiwnl.colorfulgui.builder.ItemBuilder;
import me.xflyiwnl.colorfulgui.object.DynamicItem;
import me.xflyiwnl.colorfulgui.object.action.MetaChange;
import me.xflyiwnl.colorfulgui.object.action.UpdateItem;
import me.xflyiwnl.colorfulgui.object.event.UpdateItemEvent;
import me.xflyiwnl.colorfulgui.util.ColorUtils;
import org.bukkit.*;
import org.bukkit.block.banner.Pattern;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionData;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.function.Supplier;

/**
 * Builder class for creating DynamicItem instances with a fluent API.
 * This builder provides methods to configure various properties of dynamic items
 * including material, name, lore, enchantments, click actions, and update handlers.
 * 
 * Dynamic items can change their properties during GUI updates through update handlers.
 * This builder supports creating items with complex configurations including
 * custom skulls, potions, banners, and metadata modifications.
 */
public class DynamicItemBuilder implements ItemBuilder<DynamicItem> {

    private DynamicItem guiItem;
    private ItemStack itemStack;
    private ItemMeta itemMeta;
    private Material material;

    private String name;
    private List<String> lore = Arrays.asList();

    private int amount = 1;
    private ItemFlag[] itemFlags;

    private boolean unbreakable = false;
    private int model = 0;

    private boolean isBanner = false;
    private boolean isPotion = false;
    private boolean isSkull = false;
    private OfflinePlayer player;

    private Map<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>();
    private ClickDynamicAction action;
    private UpdateItem<UpdateItemEvent<DynamicItem>> onUpdate;

    private PotionData potionData;
    private Color color;

    private Map<Integer, Pattern> patternMap = new HashMap<Integer, Pattern>();
    private List<Pattern> patterns;

    private MetaChange<ItemMeta> metaChange;

    /**
     * Creates a new DynamicItemBuilder instance.
     */
    public DynamicItemBuilder() {
    }

    /**
     * Creates a builder from an existing DynamicItem.
     * This allows for modifying an existing dynamic item.
     *
     * @param guiItem The existing DynamicItem to copy properties from
     * @return This builder instance for method chaining
     */
    public DynamicItemBuilder from(DynamicItem guiItem) {
        this.guiItem = guiItem;
        return this;
    }

    /**
     * Creates a builder from an existing ItemStack.
     * This allows for modifying an existing item stack.
     *
     * @param itemStack The existing ItemStack to copy properties from
     * @return This builder instance for method chaining
     */
    public DynamicItemBuilder from(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }

    /**
     * Creates a builder from a generic supplier.
     * The supplier can return either an ItemStack or DynamicItem.
     *
     * @param genericSupplier The supplier that provides the item
     * @param <T> The type of item the supplier provides
     * @return This builder instance for method chaining
     * @throws IllegalStateException if the supplier returns an unexpected type
     */
    public <T> DynamicItemBuilder from(Supplier<T> genericSupplier) {
        T value = genericSupplier.get();
        return switch(value) {
            case ItemStack item -> from(item);
            case DynamicItem staticItem -> from(staticItem);
            default -> throw new IllegalStateException("Unexpected value: " + value);
        };
    }

    /**
     * Sets the material for the item.
     *
     * @param material The material to use for the item
     * @return This builder instance for method chaining
     */
    public DynamicItemBuilder material(Material material) {
        this.material = material;
        return this;
    }

    /**
     * Sets the material for the item using a supplier.
     *
     * @param materialSupplier The supplier that provides the material
     * @return This builder instance for method chaining
     */
    public DynamicItemBuilder material(Supplier<Material> materialSupplier) {
        return material(materialSupplier.get());
    }

    /**
     * Sets the display name for the item.
     * The name will be colorized using ColorUtils.
     *
     * @param name The display name to set
     * @return This builder instance for method chaining
     */
    public DynamicItemBuilder name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the display name for the item using a supplier.
     *
     * @param nameSupplier The supplier that provides the name
     * @return This builder instance for method chaining
     */
    public DynamicItemBuilder name(Supplier<String> nameSupplier) {
        return name(nameSupplier.get());
    }

    /**
     * Sets the lore (description) for the item using a list.
     * The lore will be colorized using ColorUtils.
     *
     * @param lore The lore as a list of strings
     * @return This builder instance for method chaining
     */
    public DynamicItemBuilder lore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    /**
     * Sets the lore (description) for the item using varargs.
     * The lore will be colorized using ColorUtils.
     *
     * @param lore The lore as individual strings
     * @return This builder instance for method chaining
     */
    public DynamicItemBuilder lore(String... lore) {
        if (lore.length > 0)
            this.lore = Arrays.asList(lore);
        return this;
    }

    /**
     * Sets the lore for the item using a generic supplier.
     * The supplier can return a String, String array, or List of Strings.
     *
     * @param genericSupplier The supplier that provides the lore
     * @param <T> The type of lore the supplier provides
     * @return This builder instance for method chaining
     * @throws IllegalStateException if the supplier returns an unexpected type
     */
    public <T> DynamicItemBuilder lore(Supplier<T> genericSupplier) {
        T value = genericSupplier.get();
        return switch (value) {
            case String oneLineLore -> lore(oneLineLore);
            case String[] varargLore -> lore(varargLore);
            case List<?> multipleLineLore -> lore((List<String>) multipleLineLore);
            default -> throw new IllegalStateException("Unexpected value: " + value);
        };
    }

    /**
     * Sets the amount (stack size) for the item.
     *
     * @param amount The amount to set (1-64)
     * @return This builder instance for method chaining
     */
    public DynamicItemBuilder amount(int amount) {
        this.amount = amount;
        return this;
    }

    /**
     * Sets the item flags for the item.
     * Item flags hide certain information from the item tooltip.
     *
     * @param flags The item flags to apply
     * @return This builder instance for method chaining
     */
    public DynamicItemBuilder flags(ItemFlag... flags) {
        this.itemFlags = flags;
        return this;
    }

    /**
     * Sets the amount for the item using a supplier.
     *
     * @param amountSupplier The supplier that provides the amount
     * @return This builder instance for method chaining
     */
    public DynamicItemBuilder amount(Supplier<Integer> amountSupplier) {
        return amount(amountSupplier.get());
    }

    /**
     * Sets whether the item should be unbreakable.
     *
     * @param unbreakable true to make the item unbreakable
     * @return This builder instance for method chaining
     */
    public DynamicItemBuilder unbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    /**
     * Sets the custom model data for the item.
     * This is used for resource pack custom models.
     *
     * @param model The custom model data value
     * @return This builder instance for method chaining
     */
    public DynamicItemBuilder model(int model) {
        this.model = model;
        return this;
    }

    /**
     * Adds an enchantment to the item.
     *
     * @param enchantment The enchantment to add
     * @param level The level of the enchantment
     * @return This builder instance for method chaining
     */
    public DynamicItemBuilder enchant(Enchantment enchantment, int level) {
        enchantments.put(enchantment, level);
        return this;
    }

    /**
     * Sets the click action for the item.
     * This action will be executed when the item is clicked in the GUI.
     *
     * @param action The action to execute when the item is clicked
     * @return This builder instance for method chaining
     */
    public DynamicItemBuilder action(ClickDynamicAction action) {
        this.action = action;
        return this;
    }

    /**
     * Configures the item as a player skull with the specified player.
     *
     * @param player The player whose head to use
     * @return This builder instance for method chaining
     */
    public DynamicItemBuilder skull(OfflinePlayer player) {
        if (player != null) {
            this.player = player;
            this.isSkull = true;
        }
        return this;
    }

    /**
     * Configures the item as a custom skull with the specified texture URL.
     *
     * @param url The texture URL for the skull
     * @return This builder instance for method chaining
     */
    public DynamicItemBuilder skull(String url) {
        UUID uuid = UUID.randomUUID();
        PlayerProfile profile = Bukkit.createPlayerProfile(uuid);

        PlayerTextures textures = profile.getTextures();
        try {
            textures.setSkin(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return this;
        }
        profile.setTextures(textures);

        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        if (skullMeta != null) {
            skullMeta.setOwnerProfile(profile);
            head.setItemMeta(skullMeta);
            from(head);
        }
        return this;
    }

    /**
     * Configures the item as a skull using a generic supplier.
     * The supplier can return either a String URL or OfflinePlayer.
     *
     * @param genericSupplier The supplier that provides the skull data
     * @param <T> The type of skull data the supplier provides
     * @return This builder instance for method chaining
     * @throws IllegalStateException if the supplier returns an unexpected type
     */
    public <T> DynamicItemBuilder skull(Supplier<T> genericSupplier) {
        T value = genericSupplier.get();
        return switch (value) {
            case String url -> skull(url);
            case OfflinePlayer offlinePlayer -> skull(offlinePlayer);
            default -> throw new IllegalStateException("Unexpected value: " + value);
        };
    }

    /**
     * Sets the update handler for the dynamic item.
     * This handler will be called periodically to update the item's properties.
     * This is the key difference between static and dynamic items.
     *
     * @param onUpdate The update handler to execute during GUI updates
     * @return This builder instance for method chaining
     */
    public DynamicItemBuilder update(UpdateItem<UpdateItemEvent<DynamicItem>> onUpdate) {
        this.onUpdate = onUpdate;
        return this;
    }

    /**
     * Sets the potion data for the item.
     *
     * @param potionData The potion data to set
     * @return This builder instance for method chaining
     */
    public DynamicItemBuilder potionData(PotionData potionData) {
        this.isPotion = true;
        this.potionData = potionData;
        return this;
    }

    /**
     * Sets the color for the potion item.
     *
     * @param color The color to set for the potion
     * @return This builder instance for method chaining
     */
    public DynamicItemBuilder color(Color color) {
        this.isPotion = true;
        this.color = color;
        return this;
    }

    /**
     * Adds a pattern to the banner at the specified index.
     *
     * @param i The index where to add the pattern
     * @param pattern The pattern to add
     * @return This builder instance for method chaining
     */
    public DynamicItemBuilder pattern(int i, Pattern pattern) {
        this.isBanner = true;
        this.patternMap.put(i, pattern);
        return this;
    }

    /**
     * Sets the patterns for the banner using a list.
     *
     * @param patterns The list of patterns to set
     * @return This builder instance for method chaining
     */
    public DynamicItemBuilder patterns(List<Pattern> patterns) {
        if (patterns != null && !patterns.isEmpty()) {
            this.isBanner = true;
            this.patterns = patterns;
        }
        return this;
    }

    /**
     * Sets the patterns for the banner using varargs.
     *
     * @param patterns The patterns to set
     * @return This builder instance for method chaining
     */
    public DynamicItemBuilder patterns(Pattern... patterns) {
        return patterns(Arrays.asList(patterns));
    }

    /**
     * Sets the item meta directly for the item.
     *
     * @param itemMeta The ItemMeta to set
     * @return This builder instance for method chaining
     */
    public DynamicItemBuilder meta(ItemMeta itemMeta) {
        this.itemMeta = itemMeta;
        return this;
    }

    /**
     * Sets a custom meta change handler for the item.
     * This allows for advanced customization of the item's metadata.
     *
     * @param meta The meta change handler
     * @return This builder instance for method chaining
     */
    public DynamicItemBuilder meta(MetaChange<ItemMeta> meta) {
        this.metaChange = meta;
        return this;
    }

    /**
     * Sets the item meta using a supplier.
     *
     * @param metaSupplier The supplier that provides the ItemMeta
     * @return This builder instance for method chaining
     */
    public DynamicItemBuilder meta(Supplier<ItemMeta> metaSupplier) {
        return meta(metaSupplier.get());
    }

    /**
     * Builds and returns the configured DynamicItem.
     * This method applies all the configured properties and creates the final item.
     * The resulting item will have update capabilities if an update handler was set.
     *
     * @return The built DynamicItem instance
     */
    @Override
    public DynamicItem build() {

        UUID uuid = UUID.randomUUID();

        ItemStack itemStack = guiItem != null ? guiItem.getItemStack() : this.itemStack;
        if (guiItem != null && guiItem.getItemStack() == null && itemStack == null ||
                guiItem == null && itemStack == null) {
            itemStack = new ItemStack(material, amount);
        }

        if (material != null) {
            itemStack.setType(material);
        }

        if (amount != 1) {
            itemStack.setAmount(amount);
        }

        ItemMeta itemMeta = this.itemMeta;
        if (itemMeta == null) itemMeta = itemStack.getItemMeta();

        if (name != null) {
            itemMeta.setDisplayName(ColorUtils.colorize(name));
        }

        itemMeta.setLore(ColorUtils.colorize(lore));

        if (!enchantments.isEmpty()) {
            for (Enchantment enchantment : enchantments.keySet()) {
                int i = enchantments.get(enchantment);
                itemMeta.addEnchant(enchantment, i, true);
            }
        }

        itemMeta.setUnbreakable(unbreakable);
        itemMeta.setCustomModelData(model);

        if (itemFlags != null) {
            itemMeta.addItemFlags(itemFlags);
        }

        if (guiItem != null) {
            itemMeta.getPersistentDataContainer().set(new NamespacedKey(ColorfulGUI.getInstance(), "colorfulgui"), PersistentDataType.STRING, guiItem.getUniqueId().toString());
        } else {
            itemMeta.getPersistentDataContainer().set(new NamespacedKey(ColorfulGUI.getInstance(), "colorfulgui"), PersistentDataType.STRING, uuid.toString());
        }

        if (metaChange != null) {
            metaChange.execute(itemMeta);
        }
        itemStack.setItemMeta(itemMeta);

        if (isSkull) {
            itemStack.setType(Material.PLAYER_HEAD);
            SkullMeta skullMeta = (SkullMeta) itemMeta;
            PlayerProfile profile = player.getPlayerProfile();
            if (!profile.isComplete())
                profile = profile.update().join();
            skullMeta.setOwnerProfile(profile);
            itemStack.setItemMeta(skullMeta);
        }

        if (isPotion) {
            PotionMeta potionMeta = (PotionMeta) itemMeta;
            if (potionData != null) potionMeta.setBasePotionData(potionData);
            if (color != null) potionMeta.setColor(color);
            itemStack.setItemMeta(potionMeta);
        }

        if (isBanner) {
            BannerMeta bannerMeta = (BannerMeta) itemMeta;
            if (patterns != null) bannerMeta.setPatterns(patterns);
            if (!patternMap.isEmpty()) {
                patternMap.forEach((integer, pattern) -> {
                    bannerMeta.setPattern(integer, pattern);
                });
            }
            itemStack.setItemMeta(bannerMeta);
        }

        if (guiItem != null) {
            guiItem.setItemStack(itemStack);
            if (action != null)
                guiItem.setAction(action);
            if (onUpdate != null)
                guiItem.setOnUpdate(onUpdate);
            return guiItem;
        } else {
            return new DynamicItem(uuid, itemStack, action, onUpdate);
        }

    }
}
