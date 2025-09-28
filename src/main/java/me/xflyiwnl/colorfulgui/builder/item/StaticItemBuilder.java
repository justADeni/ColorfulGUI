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

    public StaticItemBuilder() {
    }

    public StaticItemBuilder from(StaticItem guiItem) {
        this.guiItem = guiItem;
        return this;
    }

    public StaticItemBuilder from(ItemStack itemStack) {
        if (itemStack != null)
            this.itemStack = itemStack;
        return this;
    }

    public <T> StaticItemBuilder from(Supplier<T> genericSupplier) {
        T value = genericSupplier.get();
        return switch(value) {
            case ItemStack item -> from(item);
            case StaticItem staticItem -> from(staticItem);
            default -> throw new IllegalStateException("Unexpected value: " + value);
        };
    }

    public StaticItemBuilder material(Material material) {
        if (material != null)
            this.material = material;
        return this;
    }

    public StaticItemBuilder material(Supplier<Material> materialSupplier) {
        return material(materialSupplier.get());
    }

    public StaticItemBuilder name(String name) {
        if (name != null)
            this.name = name;
        return this;
    }

    public StaticItemBuilder name(Supplier<String> nameSupplier) {
        return name(nameSupplier.get());
    }

    public StaticItemBuilder lore(List<String> multipleLineLore) {
        if (multipleLineLore != null)
            this.lore = multipleLineLore;
        return this;
    }

    public <T> StaticItemBuilder lore(Supplier<T> genericSupplier) {
        T value = genericSupplier.get();
        return switch (value) {
            case String oneLineLore -> lore(oneLineLore);
            case List<?> multipleLineLore -> lore((List<String>) multipleLineLore);
            default -> throw new IllegalStateException("Unexpected value: " + value);
        };
    }

    public StaticItemBuilder lore(String... lore) {
        if (lore.length > 0)
            this.lore = Arrays.asList(lore);

        return this;
    }

    public StaticItemBuilder amount(int amount) {
        this.amount = amount;
        return this;
    }

    public StaticItemBuilder amount(Supplier<Integer> amountSupplier) {
        return amount(amountSupplier.get());
    }

    public StaticItemBuilder flags(ItemFlag... flags) {
        this.itemFlags = flags;
        return this;
    }

    public StaticItemBuilder flags(Supplier<ItemFlag[]> flagsSupplier) {
        this.itemFlags = flagsSupplier.get();
        return this;
    }

    public StaticItemBuilder unbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    public StaticItemBuilder model(int model) {
        this.model = model;
        return this;
    }

    public StaticItemBuilder enchant(Enchantment enchantment, int level) {
        if (enchantment != null)
            enchantments.put(enchantment, level);
        return this;
    }

    public StaticItemBuilder action(ClickStaticAction action) {
        this.action = action;
        return this;
    }

    public StaticItemBuilder skull(OfflinePlayer player) {
        if (player != null) {
            this.player = player;
            this.isSkull = true;
        }
        return this;
    }

    public StaticItemBuilder skull(String url) {
        UUID uuid = UUID.randomUUID();
        PlayerProfile profile = Bukkit.createPlayerProfile(uuid);

        PlayerTextures textures = profile.getTextures();
        try {
            textures.setSkin(new URL(url));
        } catch (MalformedURLException e) {
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

    public <T> StaticItemBuilder skull(Supplier<T> genericSupplier) {
        T value = genericSupplier.get();
        return switch (value) {
            case String url -> skull(url);
            case OfflinePlayer offlinePlayer -> skull(offlinePlayer);
            default -> throw new IllegalStateException("Unexpected value: " + value);
        };
    }

    @Deprecated
    public StaticItemBuilder potionData(PotionData potionData) {
        if (potionData != null) {
            this.isPotion = true;
            this.potionData = potionData;
        }
        return this;
    }

    @Deprecated
    public StaticItemBuilder color(Color color) {
        if (color != null) {
            this.isPotion = true;
            this.color = color;
        }
        return this;
    }

    public StaticItemBuilder pattern(int i, Pattern pattern) {
        if (pattern != null) {
            this.isBanner = true;
            this.patternMap.put(i, pattern);
        }
        return this;
    }

    public StaticItemBuilder patterns(List<Pattern> patterns) {
        if (patterns != null && !patterns.isEmpty()) {
            this.isBanner = true;
            this.patterns = patterns;
        }
        return this;
    }

    public StaticItemBuilder meta(MetaChange<ItemMeta> meta) {
        if (meta != null) {
            this.metaChange = meta;
        }
        return this;
    }

    public StaticItemBuilder toolTipDisplay(Function<TooltipDisplay.Builder, TooltipDisplay.Builder> display) {
        tooltipDisplay = display.apply(TooltipDisplay.tooltipDisplay()).build();
        return this;
    }

    public StaticItemBuilder hideAllToolTips() {
        toolTipDisplay(t -> t.hideTooltip(true));
        return this;
    }

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
                DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE,
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
                DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE,
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
