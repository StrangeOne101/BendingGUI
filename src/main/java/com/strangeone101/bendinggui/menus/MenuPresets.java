package com.strangeone101.bendinggui.menus;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import com.projectkorra.projectkorra.object.Preset;
import com.strangeone101.bendinggui.MenuBase;
import com.strangeone101.bendinggui.MenuItem;

public class MenuPresets extends MenuBase 
{
	protected Player openPlayer;
	private OfflinePlayer thePlayer;
	
	public MenuPresets(OfflinePlayer player) 
	{
		super("Save/Load Binds", 4);
		this.thePlayer = player;
	}

	
	/**Update the gui*/
	public void update()
	{
		
	}
	
	/**Adds the item to the menu normally but because normal MenuItems
	 * don't support NBT data, we have to manually get and set the data.*/
	/*public void placeItemInMenu(MenuItem item, int index, OfflinePlayer player)
	{
		NBTTagCompound tag = item.getNBTData();
		tag.setString("SkullOwner", player.getName());
		item.setNBTData(tag);
		this.addMenuItem(item, index);
	}*/
	
	public MenuItem getArrowItem()
	{
		String s = ChatColor.YELLOW + "Return to menu"; 
		MenuItem item = new MenuItem(ChatColor.YELLOW +  s, new MaterialData(Material.ARROW))
		{
			@Override
			public void onClick(Player player) 
			{
				switchMenu(player, new MenuBendingOptions(thePlayer));
			}
		};
		item.addDescription(ChatColor.DARK_GRAY + "Click to return to the menu!");		
		return item;
	}
	
	public MenuItem getPreset(String preset) {
		
		if (!(thePlayer instanceof Player)) return null;
		Player p = (Player) thePlayer;
		
		if (Preset.getPreset(p, preset) != null) {
			String s = ChatColor.YELLOW + "Return to menu"; 
			MenuItem item = new MenuItem(ChatColor.YELLOW +  s, new MaterialData(Material.ARROW))
			{
				@Override
				public void onClick(Player player) 
				{
					switchMenu(player, new MenuBendingOptions(thePlayer));
				}
			};
			item.addDescription(ChatColor.DARK_GRAY + "Click to return to the menu!");		
			return item;
		}
		return null;
	}
	
	@Override
	public void openMenu(Player player) 
	{
		this.openPlayer = player;
		update();
		super.openMenu(player);
	}
}
