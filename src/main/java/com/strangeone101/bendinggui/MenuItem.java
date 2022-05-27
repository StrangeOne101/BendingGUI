package com.strangeone101.bendinggui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class MenuItem{

	protected List<String> lore = new ArrayList<String>();
	protected MenuBase menu;
	protected int number;
	protected Material icon;
	protected String text;
	protected boolean isShiftClicked = false;
	protected boolean isEnchanted = false;
	
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
        meta.setLore(lore);
        meta.setDisplayName(getText());
        slot.setItemMeta(meta);
        //net.minecraft.server.v1_8_R3.ItemStack stack1 = CraftItemStack.asNMSCopy(slot);
        //stack1.setTag(this.getNBTData());
        return slot;//CraftItemStack.asCraftMirror(stack1);
	}

	/***DO NOT USE PLAYER VARIABLE IF USING MenuBendingOptions! Use the class' player variable instead! This causes
	 * problems when looking at other player's menus. Called when a player clicks on the item.
	 * @param player The player clicking*/
	public abstract void onClick(Player player);
	
	public void setDescriptions(List<String> lines) {
		this.lore = lines;
	}
	
	public void addDescription(String line) {
		this.lore.addAll(List.of(line.split("\\n")));
	}
	
	public void setMenu(MenuBase menu) {
		this.menu = menu;
	}
	
	public boolean isShiftClicked() {
		return this.isShiftClicked;
	}
}
