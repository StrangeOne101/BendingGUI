package com.strangeone101.bendinggui.menus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.Element;
import com.strangeone101.bendinggui.BendingGUI;
import com.strangeone101.bendinggui.Config;
import com.strangeone101.bendinggui.MenuBase;
import com.strangeone101.bendinggui.MenuItem;
import com.strangeone101.bendinggui.RunnablePlayer;
import com.strangeone101.bendinggui.Util;

public class MenuPlayers extends MenuBase 
{
	public RunnablePlayer runnable;
	public MenuBendingOptions prev;
	public static Collection<? extends OfflinePlayer> players = Bukkit.getOnlinePlayers();
	public int page = 0;
	protected Player openPlayer;
	protected boolean showOffinePlayers;
	
	public MenuPlayers(MenuBendingOptions prevmenu) 
	{
		super("Player Bending", getSize());
		this.prev = prevmenu;
	}
	
	public void setOnClick(RunnablePlayer code)
	{
		this.runnable = code;
	}

	private static int getSize()
	{
		int size = 4;
		size = (players.size() / 7) + 1 + 2;
		if (size > 6) size = 6;
		if (size < 4) size = 4;
		return size;
	}
	
	/**Update the gui*/
	public void update()
	{
		if (this.showOffinePlayers)
		{
			players = Arrays.asList(Bukkit.getOfflinePlayers());
		}
		int p = players.size();
		int extraIndex = p <= 4 * 7 ? 9 : 0;
		int j = players.size();
		if (players.size() >= 5 * 7)
		{
			j = 35;
			this.addMenuItem(this.getArrowItem(true), 6, getSize() - 1);
		}
		this.addMenuItem(this.getArrowItem(false), 0, getSize() - 1);
		if (BendingGUI.enableOfflinePlayers) 
		{
			this.addMenuItem(this.getOfflinePlayerToggle(), 8, getSize() - 1);
		}
		int k = j + this.page * 35;
		k = k > players.size() ? players.size() : k;
		
		for (int i = this.page * 35; i < k; i++)
		{
			OfflinePlayer player = (OfflinePlayer) players.toArray()[i];
			if (player != null)
			{
				int x = (i - (this.page * 35)) % 7 + 1;
				int y = (i - (this.page * 35)) / 7;
				int i1 = y * 9 + x + extraIndex;
				//this.placeItemInMenu(this.getPlayerItem(player), i1, player);
				this.addMenuItem(this.getPlayerItem(player), i1);
				SkullMeta meta = (SkullMeta) this.getInventory().getItem(i1).getItemMeta();
				meta.setOwner(player.getName());
				this.getInventory().getItem(i1).setItemMeta(meta);
			}
		}
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
	
	@SuppressWarnings("deprecation")
	public MenuItem getPlayerItem(final OfflinePlayer player)
	{
		MenuItem item = new MenuItem(ChatColor.YELLOW + player.getName() + (!player.isOnline() ? ChatColor.DARK_GRAY + " (Offline)" : (player.getName() == openPlayer.getName() ? ChatColor.DARK_GRAY + " (You)" : "")), new MaterialData(Material.SKULL_ITEM, (byte)3)) 
		{
			@Override
			public void onClick(Player player1) 
			{	
				if (player1.hasPermission("bendinggui.admin.view"))
				{
					switchMenu(player1, new MenuBendingOptions(player));
				}
			}
		};
		
		if (BendingPlayer.getBendingPlayer(player) == null || BendingPlayer.getBendingPlayer(player).getElements() == null || BendingPlayer.getBendingPlayer(player).getElements().size() == 0)
		{
			item.addDescription(ChatColor.GRAY + "(Non-bender)");
			if (openPlayer.hasPermission("bendinggui.admin.view"))
			{
				item.addDescription("");
				item.addDescription(ChatColor.RED + "" + ChatColor.BOLD + "CLICK TO VIEW BENDING");
			}
			return item;
		}
		if ((player instanceof Player && ((Player)player).hasPermission("bending.avatar")) || 
				BendingPlayer.getBendingPlayer(player).getElements().size() > 1)
		{
			boolean b = BendingPlayer.getBendingPlayer(player).hasElement(Element.EARTH) || BendingPlayer.getBendingPlayer(player).hasElement(Element.AIR);
			item.addDescription((player.getName() != openPlayer.getName() ? ChatColor.GRAY + "Currently the " : ChatColor.GRAY + "You are the " ) + Element.AVATAR.getColor() + "Avatar" );
			item.addDescription(ChatColor.DARK_GRAY + (player.getName() != openPlayer.getName() ? "They" : "You") + " are currently a" + (b ? "n" : "") + ":");
		}
		else
		{
			boolean b = BendingPlayer.getBendingPlayer(player).hasElement(Element.EARTH) || BendingPlayer.getBendingPlayer(player).hasElement(Element.AIR);
			item.addDescription((player.getName() != openPlayer.getName() ? ChatColor.DARK_GRAY + "They" : ChatColor.GRAY + "You" ) + " are currently a" + (b ? "n" : "") + ":");
		}
		BendingPlayer p = BendingPlayer.getBendingPlayer(player);
	
		if (p.getElements().contains(Element.AIR)) 
		{
			String s = ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + "Airbender";
			if (player instanceof Player) 
			{
				if (((Player) player).hasPermission("bending.air.flight") && Config.showSubElementsOnUser) {s = s + ChatColor.DARK_GRAY + " (Can" + ChatColor.GRAY + " Fly" + ChatColor.DARK_GRAY + ")";}
				item.addDescription(s);
			}
		}
		if (p.getElements().contains(Element.EARTH)) 
		{
			String s = ChatColor.DARK_GRAY + "- " + ChatColor.GREEN + "Earthbender";
			if (player instanceof Player) 
			{
				boolean b = false;
				List<String> list = new ArrayList<String>();
				if (((Player)player).hasPermission("bending.earth.metalbending") && Config.showSubElementsOnUser) 
				{
					b = true;
					list.add(Element.METAL.getColor() + "Metalbend");
				}
				if (((Player)player).hasPermission("bending.earth.lavabending") && Config.showSubElementsOnUser) 
				{
					b = true;
					list.add(Element.LAVA.getColor() + "Lavabend");
				}
				if (((Player)player).hasPermission("bending.earth.sandbending") && Config.showSubElementsOnUser) 
				{
					b = true;
					list.add(Element.SAND.getColor() + "Sandbend");
				}
				if (b && Config.showSubElementsOnUser) 
				{
					s = s + ChatColor.DARK_GRAY + " (Can " + (BendingGUI.INSTANCE.makeListFancy(list).replaceAll("\\,", ChatColor.DARK_GRAY + ",")
							.replaceAll(" and ", ChatColor.DARK_GRAY + " and ") + ChatColor.DARK_GRAY + ")");
				}
				List<String> l = BendingGUI.getDescriptions(s, ChatColor.DARK_GRAY, 65);
				for (String s1 : l) {
					item.addDescription(s1);
				}
			}
		}
		if (p.getElements().contains(Element.FIRE)) 
		{
			String s = ChatColor.DARK_GRAY + "- " + ChatColor.RED + "Firebender";
			if (player instanceof Player) 
			{
				boolean b = false;
				List<String> list = new ArrayList<String>();
				if (((Player)player).hasPermission("bending.fire.lightningbending")) 
				{
					b = true;
					list.add("Can use " + Element.LIGHTNING.getColor() + "Lightning");
				}
				if (((Player)player).hasPermission("bending.fire.combustionbending")) 
				{
					b = true;
					list.add("Can " + Element.COMBUSTION.getColor() + "Combust");
				}
				if (b && Config.showSubElementsOnUser) 
				{
					s = s + ChatColor.DARK_GRAY + " (" + (BendingGUI.INSTANCE.makeListFancy(list).replaceAll("\\,", ChatColor.DARK_GRAY + ",")
							.replaceAll(" and ", ChatColor.DARK_GRAY + " and ") + ChatColor.DARK_GRAY + ")");
				}
				List<String> l = BendingGUI.getDescriptions(s, ChatColor.DARK_GRAY, 65);
				for (String s1 : l) {
					item.addDescription(s1);
				}
			}
			
		}
		if (p.getElements().contains(Element.WATER)) 
		{
			String s = ChatColor.DARK_GRAY + "- " + ChatColor.BLUE + "Waterbender";
			if (player instanceof Player) 
			{
				boolean b = false;
				List<String> list = new ArrayList<String>();
				if (((Player)player).hasPermission("bending.water.bloodbending")) 
				{
					b = true;
					list.add(Element.BLOOD.getColor() + "Bloodbend");
				}
				if (((Player)player).hasPermission("bending.water.plantbending")) 
				{
					b = true;
					list.add(Element.PLANT.getColor() + "Plantbend");
				}
				if (((Player)player).hasPermission("bending.water.healing")) 
				{
					b = true;
					list.add(Element.HEALING.getColor() + "Heal");
				}
				if (b && Config.showSubElementsOnUser) 
				{
					s = s + ChatColor.DARK_GRAY + " (Can " + (BendingGUI.INSTANCE.makeListFancy(list).replaceAll("\\,", ChatColor.DARK_GRAY + ",")
							.replaceAll(" and ", ChatColor.DARK_GRAY + " and ") + ChatColor.DARK_GRAY + ")");
				}
				List<String> l = BendingGUI.getDescriptions(s, ChatColor.DARK_GRAY, 65);
				for (String s1 : l) {
					item.addDescription(s1);
				}
			}
			
		}
		if (p.getElements().contains(Element.CHI)) 
		{
			String s = ChatColor.DARK_GRAY + "- " + ChatColor.GOLD + "Chiblocker";
			item.addDescription(s);
		}
		if (!Util.getStaff(player.getUniqueId()).equals("")) {
			item.addDescription(Util.getStaff(player.getUniqueId()));
		}
		
		if (openPlayer.hasPermission("bendinggui.admin.view"))
		{
			item.addDescription("");
			item.addDescription(ChatColor.RED + "" + ChatColor.BOLD + "CLICK TO VIEW BENDING");
		}
		return item;
	}
	
	public MenuItem getArrowItem(final boolean isRight)
	{
		int maxPage = players.size() / (5 * 7) + 1;
		String s = ChatColor.YELLOW + (isRight ? "Next" : "Previous") + " Page" + ChatColor.DARK_GRAY + 
				"(" + ChatColor.YELLOW + this.page + ChatColor.DARK_GRAY + "/" + ChatColor.YELLOW + maxPage + ChatColor.DARK_GRAY + ")";
		s = this.page == 0 ? ChatColor.YELLOW + "Return to Menu" : s;
		MenuItem item = new MenuItem(ChatColor.YELLOW +  s, new MaterialData(Material.ARROW))
		{
			@Override
			public void onClick(Player player) 
			{
				if (page == 0 && !isRight)
				{
					switchMenu(player, prev);
				}
				else
				{
					if (isRight) page = page + 1;
					else page = page - 1;
					update();
				}
			}
		};
		if (isRight)
		{
			item.addDescription(ChatColor.DARK_GRAY + "Click to go to the next page!");
		}
		else if (!isRight && page == 0)
		{
			item.addDescription(ChatColor.DARK_GRAY + "Click to return to the menu!");
		}
		else
		{
			item.addDescription(ChatColor.DARK_GRAY + "Click to go to the previous page!");
		}
		
		return item;
	}
	
	public MenuItem getOfflinePlayerToggle()
	{
		String s = (this.showOffinePlayers ? "Hide" : "Show" ) + " offline players";
		MenuItem item = new MenuItem(ChatColor.RED + s, new MaterialData(Material.EYE_OF_ENDER)) 
		{

			@Override
			public void onClick(Player player) 
			{
				showOffinePlayers = !showOffinePlayers;
				update();
			}
			
		};
		if (showOffinePlayers)
		{
			item.setEnchanted(true);
		}
		item.addDescription(ChatColor.GRAY + "Click to " + (this.showOffinePlayers ? "hide" : "show" ) + " offline players");
		return item;
	}
	
	@Override
	public void openMenu(Player player) 
	{
		this.openPlayer = player;
		update();
		super.openMenu(player);
	}
}
