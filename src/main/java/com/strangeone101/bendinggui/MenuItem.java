package com.strangeone101.bendinggui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.strangeone101.bendinggui.config.ConfigStandard;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

public abstract class MenuItem {

	protected List<String> lore = new ArrayList<String>();
	protected MenuBase menu;
	protected int number;
	protected Material icon;
	protected String text;
	protected boolean isShiftClicked = false;
	protected boolean isEnchanted = false;
	protected int modelData = 0;
	
	public MenuItem(String text, Material icon, int number) {
		this.text = text;
        this.icon = icon;
        this.number = number;
	}
	
	public MenuItem(String string, Material icon) {
		this(string, icon, 1);
	}
	
	public void setEnchanted(boolean bool) {
		this.isEnchanted = bool;
	}
	
	public MenuBase getMenu() {
        return menu;
    }

    public int getNumber() {
        return number;
    }

    public Material getIcon() {
        return icon;
    }

    public String getText() {
        return text;
    }

	public ItemStack getItemStack() {
		ItemStack slot = new ItemStack(getIcon(), getNumber());
        ItemMeta meta = slot.getItemMeta();

		List<String> clonedLore = new ArrayList<>(lore);

		if (ConfigStandard.getInstance().isAdminModelData()) {
			clonedLore.add(ChatColor.GRAY + "Model Data: " + modelData);
		}

        meta.setLore(clonedLore);
        meta.setDisplayName(getText());
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES); //So weapons don't show damage, etc. Kinda pointless for UIs
		meta.setCustomModelData(modelData);
		if (meta instanceof PotionMeta) {
			((PotionMeta) meta).setBasePotionType(PotionType.WATER);
		}
        slot.setItemMeta(meta);

        return slot;
	}

	/***DO NOT USE PLAYER VARIABLE IF USING MenuBendingOptions! Use the class' player variable instead! This causes
	 * problems when looking at other player's menus. Called when a player clicks on the item.
	 * @param player The player clicking*/
	public abstract void onClick(Player player);
	
	public void setDescriptions(List<String> lines) {
		this.lore = lines;
	}
	
	public void addDescription(String line) {
		Collections.addAll(this.lore, line.split("\\n"));
	}
	
	public void setMenu(MenuBase menu) {
		this.menu = menu;
	}
	
	public boolean isShiftClicked() {
		return this.isShiftClicked;
	}

	public int getModelData() {
		return modelData;
	}

	public void setModelData(int modelData) {
		this.modelData = modelData;
	}
}
