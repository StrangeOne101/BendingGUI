package com.strangeone101.bendinggui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

public abstract class MenuItem{

	protected List<String> lore = new ArrayList<String>();
	protected MenuBase menu;
	protected int number;
	protected MaterialData icon;
	protected String text;
	protected boolean isShiftClicked = false;
	protected boolean isEnchanted = false;
	
	public MenuItem(String text, MaterialData icon, int number) 
	{
		this.text = text;
        this.icon = icon;
        this.number = number;
	}
	
	public MenuItem(String string, MaterialData icon)
	{
		this(string, icon, 1);
	}
	
	public void setEnchanted(boolean bool)
	{
		this.isEnchanted = bool;
	}
	
	public MenuBase getMenu() {
        return menu;
    }

    public int getNumber() {
        return number;
    }

    public MaterialData getIcon() {
        return icon;
    }

    public String getText() {
        return text;
    }
	
	@SuppressWarnings("deprecation")
	public ItemStack getItemStack() 
	{
		ItemStack slot = new ItemStack(getIcon().getItemType(), getNumber(), getIcon().getData());
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
	
	public void setDescriptions(List<String> lines) 
	{
		this.lore = lines;
	}
	
	public void addDescription(String line)
	{
		this.lore.add(line);
	}
	
	public void setMenu(MenuBase menu)
	{
		this.menu = menu;
	}
	
	/*public NBTTagCompound getNBTData()
	{
		NBTTagCompound tag = new NBTTagCompound();
		if (this.data != null)
		{
			tag = this.data;
		}
		NBTTagCompound display = new NBTTagCompound();
        display.setString("Name", this.getText());
        NBTTagList lore = new NBTTagList();
        for (String s : this.lore)
        {
        	lore.add(new NBTTagString(s));
        }
        display.set("Lore", lore);
        tag.set("display", display);
        return tag;
	}
	
	public void setNBTData(NBTTagCompound tag)
	{
		this.data = tag;
	}*/
	
	/*/**Adds an enchanted glow to the item*/
	/*public void addEnchantedGlow()
	{
		NBTTagCompound tag = this.getNBTData();
		NBTTagList ench = new NBTTagList();
        tag.set("ench", ench);
        this.setNBTData(tag);
	}*/
	
	public boolean isShiftClicked()
	{
		return this.isShiftClicked;
	}

}