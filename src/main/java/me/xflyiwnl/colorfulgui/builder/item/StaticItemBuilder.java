package me.xflyiwnl.colorfulgui.builder.item;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import me.xflyiwnl.colorfulgui.builder.ItemBuilder;
import me.xflyiwnl.colorfulgui.object.action.click.ClickStaticAction;
import me.xflyiwnl.colorfulgui.ColorfulGUI;
import me.xflyiwnl.colorfulgui.object.StaticItem;
import me.xflyiwnl.colorfulgui.object.action.MetaChange;
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
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Builder class for creating StaticItem instances with a fluent API.
 * This builder provides methods to configure various properties of static items
 * including material, name, lore, enchantments, click actions, and special item types.
 * 
 * Static items have fixed properties that don't change during GUI updates.
 * This builder supports creating items with complex configurations including
 * custom skulls, potions, banners, and tooltip modifications.
 */
public class StaticItemBuilder implements ItemBuilder<StaticItem> {

    private StaticItem guiItem;
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
    private ClickStaticAction action;

    private PotionData potionData;
    private Color color;

    private Map<Integer, Pattern> patternMap = new HashMap<Integer, Pattern>();
    private List<Pattern> patterns;

    private MetaChange<ItemMeta> metaChange;

    private TooltipDisplay tooltipDisplay;

    /**
     * Creates a new StaticItemBuilder instance.
     */
    public StaticItemBuilder() {
    }

    /**
     * Creates a builder from an existing StaticItem.
     * This allows for modifying an existing static item.
     *
     * @param guiItem The existing StaticItem to copy properties from
     * @return This builder instance for method chaining
     */
    public StaticItemBuilder from(StaticItem guiItem) {
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
    public StaticItemBuilder from(ItemStack itemStack) {
        if (itemStack != null)
            this.itemStack = itemStack;
        return this;
    }

    /**
     * Creates a builder from a generic supplier.
     * The supplier can return either an ItemStack or StaticItem.
     *
     * @param genericSupplier The supplier that provides the item
     * @param <T> The type of item the supplier provides
     * @return This builder instance for method chaining
     * @throws IllegalStateException if the supplier returns an unexpected type
     */
    public <T> StaticItemBuilder from(Supplier<T> genericSupplier) {
        T value = genericSupplier.get();
        return switch(value) {
            case ItemStack item -> from(item);
            case StaticItem staticItem -> from(staticItem);
            default -> throw new IllegalStateException("Unexpected value: " + value);
        };
    }

    /**
     * Sets the material for the item.
     *
     * @param material The material to use for the item
     * @return This builder instance for method chaining
     */
    public StaticItemBuilder material(Material material) {
        if (material != null)
            this.material = material;
        return this;
    }

    /**
     * Sets the material for the item using a supplier.
     *
     * @param materialSupplier The supplier that provides the material
     * @return This builder instance for method chaining
     */
    public StaticItemBuilder material(Supplier<Material> materialSupplier) {
        return material(materialSupplier.get());
    }

    /**
     * Sets the display name for the item.
     * The name will be colorized using ColorUtils.
     *
     * @param name The display name to set
     * @return This builder instance for method chaining
     */
    public StaticItemBuilder name(String name) {
        if (name != null)
            this.name = name;
        return this;
    }

    /**
     * Sets the display name for the item using a supplier.
     *
     * @param nameSupplier The supplier that provides the name
     * @return This builder instance for method chaining
     */
    public StaticItemBuilder name(Supplier<String> nameSupplier) {
        return name(nameSupplier.get());
    }

    /**
     * Sets the lore (description) for the item using a list.
     * The lore will be colorized using ColorUtils.
     *
     * @param multipleLineLore The lore as a list of strings
     * @return This builder instance for method chaining
     */
    public StaticItemBuilder lore(List<String> multipleLineLore) {
        if (multipleLineLore != null)
            this.lore = multipleLineLore;
        return this;
    }

    /**
     * Sets the lore (description) for the item using varargs.
     * The lore will be colorized using ColorUtils.
     *
     * @param lore The lore as individual strings
     * @return This builder instance for method chaining
     */
    public StaticItemBuilder lore(String... lore) {
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
    public <T> StaticItemBuilder lore(Supplier<T> genericSupplier) {
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
    public StaticItemBuilder amount(int amount) {
        this.amount = amount;
        return this;
    }

    /**
     * Sets the amount for the item using a supplier.
     *
     * @param amountSupplier The supplier that provides the amount
     * @return This builder instance for method chaining
     */
    public StaticItemBuilder amount(Supplier<Integer> amountSupplier) {
        return amount(amountSupplier.get());
    }

    /**
     * Sets the item flags for the item.
     * Item flags hide certain information from the item tooltip.
     *
     * @param flags The item flags to apply
     * @return This builder instance for method chaining
     */
    public StaticItemBuilder flags(ItemFlag... flags) {
        this.itemFlags = flags;
        return this;
    }

    /**
     * Sets the item flags for the item using a supplier.
     *
     * @param flagsSupplier The supplier that provides the item flags
     * @return This builder instance for method chaining
     */
    public StaticItemBuilder flags(Supplier<ItemFlag[]> flagsSupplier) {
        this.itemFlags = flagsSupplier.get();
        return this;
    }

    /**
     * Sets whether the item should be unbreakable.
     *
     * @param unbreakable true to make the item unbreakable
     * @return This builder instance for method chaining
     */
    public StaticItemBuilder unbreakable(boolean unbreakable) {
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
    public StaticItemBuilder model(int model) {
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
    public StaticItemBuilder enchant(Enchantment enchantment, int level) {
        if (enchantment != null)
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
    public StaticItemBuilder action(ClickStaticAction action) {
        this.action = action;
        return this;
    }

    /**
     * Configures the item as a player skull with the specified player.
     *
     * @param player The player whose head to use
     * @return This builder instance for method chaining
     */
    public StaticItemBuilder skull(OfflinePlayer player) {
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
    public StaticItemBuilder skull(String url) {
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
    public <T> StaticItemBuilder skull(Supplier<T> genericSupplier) {
        T value = genericSupplier.get();
        return switch (value) {
            case String url -> skull(url);
            case OfflinePlayer offlinePlayer -> skull(offlinePlayer);
            default -> throw new IllegalStateException("Unexpected value: " + value);
        };
    }

    /**
     * Sets the potion data for the item.
     * This method is deprecated. Use the newer potion API instead.
     *
     * @param potionData The potion data to set
     * @return This builder instance for method chaining
     * @deprecated Use newer potion API methods
     */
    @Deprecated
    public StaticItemBuilder potionData(PotionData potionData) {
        if (potionData != null) {
            this.isPotion = true;
            this.potionData = potionData;
        }
        return this;
    }

    /**
     * Sets the color for the potion item.
     * This method is deprecated. Use the newer potion API instead.
     *
     * @param color The color to set for the potion
     * @return This builder instance for method chaining
     * @deprecated Use newer potion API methods
     */
    @Deprecated
    public StaticItemBuilder color(Color color) {
        if (color != null) {
            this.isPotion = true;
            this.color = color;
        }
        return this;
    }

    /**
     * Adds a pattern to the banner at the specified index.
     *
     * @param i The index where to add the pattern
     * @param pattern The pattern to add
     * @return This builder instance for method chaining
     */
    public StaticItemBuilder pattern(int i, Pattern pattern) {
        if (pattern != null) {
            this.isBanner = true;
            this.patternMap.put(i, pattern);
        }
        return this;
    }

    /**
     * Sets the patterns for the banner using a list.
     *
     * @param patterns The list of patterns to set
     * @return This builder instance for method chaining
     */
    public StaticItemBuilder patterns(List<Pattern> patterns) {
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
    public StaticItemBuilder patterns(Pattern... patterns) {
        return patterns(Arrays.asList(patterns));
    }

    /**
     * Sets a custom meta change handler for the item.
     * This allows for advanced customization of the item's metadata.
     *
     * @param meta The meta change handler
     * @return This builder instance for method chaining
     */
    public StaticItemBuilder meta(MetaChange<ItemMeta> meta) {
        if (meta != null) {
            this.metaChange = meta;
        }
        return this;
    }

    /**
     * Sets the tooltip display configuration for the item.
     * This allows for customizing which tooltip components are shown.
     *
     * @param display The function to configure the tooltip display
     * @return This builder instance for method chaining
     */
    public StaticItemBuilder toolTipDisplay(Function<TooltipDisplay.Builder, TooltipDisplay.Builder> display) {
        tooltipDisplay = display.apply(TooltipDisplay.tooltipDisplay()).build();
        return this;
    }

    /**
     * Hides all tooltip components for the item.
     *
     * @return This builder instance for method chaining
     */
    public StaticItemBuilder hideAllToolTips() {
        toolTipDisplay(t -> t.hideTooltip(true));
        return this;
    }

    /**
     * Hides all tooltip components except the name for the item.
     *
     * @return This builder instance for method chaining
     */
    public StaticItemBuilder hideAllToolTipsButName() {
        toolTipDisplay(t -> t.addHiddenComponents(
                DataComponentTypes.ENCHANTMENTS,
                DataComponentTypes.LORE,
                DataComponentTypes.UNBREAKABLE,
                DataComponentTypes.CUSTOM_MODEL_DATA,
                DataComponentTypes.ITEM_MODEL,
                DataComponentTypes.BLOCKS_ATTACKS,
                DataComponentTypes.CAN_BREAK,
                DataComponentTypes.ATTRIBUTE_MODIFIERS,
                DataComponentTypes.BANNER_PATTERNS,
                DataComponentTypes.CONSUMABLE,
                DataComponentTypes.BUNDLE_CONTENTS,
                DataComponentTypes.DAMAGE,
                DataComponentTypes.DAMAGE_RESISTANT,
                DataComponentTypes.CONSUMABLE,
                DataComponentTypes.POTION_CONTENTS,
                DataComponentTypes.WRITABLE_BOOK_CONTENT,
                DataComponentTypes.CAN_PLACE_ON,
                DataComponentTypes.DEATH_PROTECTION,
                DataComponentTypes.EQUIPPABLE,
                DataComponentTypes.ENCHANTABLE,
                DataComponentTypes.DYED_COLOR
        ));
        return this;
    }

    /**
     * Hides all tooltip components except the name and lore for the item.
     *
     * @return This builder instance for method chaining
     */
    public StaticItemBuilder hideAllToolTipsButNameLore() {
        toolTipDisplay(t -> t.addHiddenComponents(
                DataComponentTypes.ENCHANTMENTS,
                DataComponentTypes.UNBREAKABLE,
                DataComponentTypes.CUSTOM_MODEL_DATA,
                DataComponentTypes.ITEM_MODEL,
                DataComponentTypes.BLOCKS_ATTACKS,
                DataComponentTypes.CAN_BREAK,
                DataComponentTypes.ATTRIBUTE_MODIFIERS,
                DataComponentTypes.BANNER_PATTERNS,
                DataComponentTypes.CONSUMABLE,
                DataComponentTypes.BUNDLE_CONTENTS,
                DataComponentTypes.DAMAGE,
                DataComponentTypes.DAMAGE_RESISTANT,
                DataComponentTypes.CONSUMABLE,
                DataComponentTypes.POTION_CONTENTS,
                DataComponentTypes.WRITABLE_BOOK_CONTENT,
                DataComponentTypes.CAN_PLACE_ON,
                DataComponentTypes.DEATH_PROTECTION,
                DataComponentTypes.EQUIPPABLE,
                DataComponentTypes.ENCHANTABLE,
                DataComponentTypes.DYED_COLOR
        ));
        return this;
    }

    /**
     * Builds and returns the configured StaticItem.
     * This method applies all the configured properties and creates the final item.
     *
     * @return The built StaticItem instance
     */
    @Override
    public StaticItem build() {

        UUID uuid = UUID.randomUUID();

        ItemStack itemStack = guiItem != null ? guiItem.getItemStack() : this.itemStack;
        if (guiItem != null && itemStack == null ||
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

        if (tooltipDisplay != null) {
            itemStack.setData(DataComponentTypes.TOOLTIP_DISPLAY, tooltipDisplay);
        }

        if (guiItem != null) {
            guiItem.setItemStack(itemStack);
            if (action != null)
                guiItem.setAction(action);
            return guiItem;
        } else {
            return new StaticItem(uuid, itemStack, action);
        }

    }
}
