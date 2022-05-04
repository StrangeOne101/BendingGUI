package com.strangeone101.bendinggui.menus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.strangeone101.bendinggui.LangBuilder;
import com.strangeone101.bendinggui.config.ConfigStandard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.SkullMeta;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.Element;
import com.strangeone101.bendinggui.BendingGUI;
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
		super(new LangBuilder("Display.Players.Title").toString(), getSize());
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

	public MenuItem getPlayerItem(final OfflinePlayer player)
	{
		String key = "Display.Players." + (player == this.openPlayer ? "You" : (player.isOnline() ? "Online" : "Offline"));
		ChatColor color = !player.isOnline() ? ChatColor.GRAY : ChatColor.YELLOW;
		BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
		Element element = bPlayer == null || bPlayer.getElements().size() == 0 ? null : (bPlayer.getElements().size() > 1 ? Element.AVATAR : bPlayer.getElements().get(0));

		MenuItem item = new MenuItem(color + new LangBuilder(key).player(player).element(element).toString(), Material.PLAYER_HEAD)
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
		
		if (bPlayer == null || bPlayer.getElements() == null || bPlayer.getElements().size() == 0)
		{
			item.addDescription(ChatColor.GRAY + new LangBuilder("Display.Players.NonBender").toString());
			if (!Util.getStaff(player.getUniqueId()).equals("")) {
				item.addDescription(ChatColor.DARK_PURPLE + Util.getStaff(player.getUniqueId()));
			}
			if (openPlayer.hasPermission("bendinggui.admin.view"))
			{
				item.addDescription("");
				item.addDescription(ChatColor.RED + new LangBuilder("Display.Players.Admin.ChooseClick").player(player).toString());
			}
			return item;
		}
		if ((player instanceof Player && ((Player)player).hasPermission("bending.avatar")) || 
				bPlayer.getElements().size() > 1)
		{

			//item.addDescription((player.getName() != openPlayer.getName() ? ChatColor.GRAY + "Currently the " : ChatColor.GRAY + "You are the " ) + Element.AVATAR.getColor() + "Avatar" );
			//item.addDescription(ChatColor.DARK_GRAY + (player.getName() != openPlayer.getName() ? "They" : "You") + " are currently a" + (b ? "n" : "") + ":");
			item.addDescription(Element.AVATAR.getColor() + new LangBuilder(key + ".Lore.Avatar" + (player == openPlayer ?  "Self" : "")).player(player).plural(player.getName()).toString());

		}
		boolean b = bPlayer.hasElement(Element.EARTH) || bPlayer.hasElement(Element.AIR);
		item.addDescription(GRAY + new LangBuilder("Display.Main.Overview.Lore.ElementPrefix").yourOrPlayer(player, openPlayer).anOrA(b ? "airOrEarth" : "").capitalizeFirst().toString());
		/*else
		{
			boolean b = bPlayer.hasElement(Element.EARTH) || bPlayer.hasElement(Element.AIR);
			item.addDescription((player.getName() != openPlayer.getName() ? ChatColor.DARK_GRAY + "They" : ChatColor.GRAY + "You" ) + " are currently a" + (b ? "n" : "") + ":");
		}*/
	
		if (bPlayer.getElements().contains(Element.AIR))
		{
			String s = ChatColor.DARK_GRAY + "- " + ChatColor.GRAY + new LangBuilder("Display.Main.Overview.Element.Air");
			if (ConfigStandard.getInstance().doShowSubElements()) {
				List<String> list = new ArrayList<String>();
				if (bPlayer.hasSubElement(Element.SubElement.FLIGHT)) {
					list.add(new LangBuilder("Display.Main.Overview.Element.Flight").toString());
				}
				if (bPlayer.hasSubElement(Element.SubElement.SPIRITUAL)) {
					list.add(new LangBuilder("Display.Main.Overview.Element.SpiritualProjection").toString()); //Spiritually
				}
				for (Element.SubElement sub : Element.getAddonSubElements(Element.AIR)) {
					if (bPlayer.hasElement(sub)) {
						list.add(new LangBuilder("Display.Main.Overview.Element." + sub.getName()).toString());
					}
				}
				if (list.size() > 0)
					s = s + GRAY + " " + new LangBuilder("Display.Main.Overview.Lore.SubList").list(list.toArray(new String[0]));
				List<String> l = Util.lengthSplit(s, ConfigStandard.getInstance().getOverviewTrim());
				for (String s1 : l) {
					item.addDescription(s1);
				}
			} else {
				item.addDescription(s);
			}
		}
		if (bPlayer.getElements().contains(Element.EARTH))
		{
			String s = ChatColor.DARK_GRAY + "- " + ChatColor.GREEN + new LangBuilder("Display.Main.Overview.Element.Earth");
			if (ConfigStandard.getInstance().doShowSubElements()) {
				List<String> list = new ArrayList<String>();
				if (bPlayer.hasSubElement(Element.METAL)) {
					list.add(new LangBuilder("Display.Main.Overview.Element.Metal").toString());
				}
				if (bPlayer.hasSubElement(Element.LAVA)) {
					list.add(new LangBuilder("Display.Main.Overview.Element.Lava").toString());
				}
				if (bPlayer.hasSubElement(Element.SAND)) {
					list.add(new LangBuilder("Display.Main.Overview.Element.Sand").toString());
				}
				for (Element.SubElement sub : Element.getAddonSubElements(Element.EARTH)) {
					if (bPlayer.hasElement(sub)) {
						list.add(new LangBuilder("Display.Main.Overview.Element." + sub.getName()).toString());
					}
				}
				if (list.size() > 0)
					s = s + GRAY + " " + new LangBuilder("Display.Main.Overview.Lore.SubList").list(list.toArray(new String[0]));

				List<String> l = Util.lengthSplit(s, ConfigStandard.getInstance().getOverviewTrim());
				for (String s1 : l) {
					item.addDescription(s1);
				}
			} else {
				item.addDescription(s);
			}
		}
		if (bPlayer.getElements().contains(Element.FIRE))
		{
			String s = ChatColor.DARK_GRAY + "- " + ChatColor.RED + new LangBuilder("Display.Main.Overview.Element.Fire");
			if (ConfigStandard.getInstance().doShowSubElements()) {
				List<String> list = new ArrayList<String>();
				if (bPlayer.hasSubElement(Element.LIGHTNING)) {
					list.add(new LangBuilder("Display.Main.Overview.Element.Lightning").toString());
				}
				if (bPlayer.hasSubElement(Element.COMBUSTION)) {
					list.add(new LangBuilder("Display.Main.Overview.Element.Combustion").toString());
				}
				if (bPlayer.hasSubElement(Element.BLUE_FIRE)) {
					list.add(new LangBuilder("Display.Main.Overview.Element.BlueFire").toString());
				}
				for (Element.SubElement sub : Element.getAddonSubElements(Element.FIRE)) {
					if (bPlayer.hasElement(sub)) {
						list.add(new LangBuilder("Display.Main.Overview.Element." + sub.getName()).toString());
					}
				}
				if (list.size() > 0)
					s = s + GRAY + " " + new LangBuilder("Display.Main.Overview.Lore.SubList").list(list.toArray(new String[0]));

				List<String> l = Util.lengthSplit(s, ConfigStandard.getInstance().getOverviewTrim());
				for (String s1 : l) {
					item.addDescription(s1);
				}
			} else {
				item.addDescription(s);
			}
		}
		if (bPlayer.getElements().contains(Element.WATER))
		{
			String s = ChatColor.DARK_GRAY + "- " + ChatColor.BLUE + new LangBuilder("Display.Main.Overview.Element.Water");
			if (ConfigStandard.getInstance().doShowSubElements()) {
				List<String> list = new ArrayList<String>();
				if (bPlayer.hasSubElement(Element.BLOOD)) {
					list.add(new LangBuilder("Display.Main.Overview.Element.Blood").toString());
				}
				if (bPlayer.hasSubElement(Element.PLANT)) {
					list.add(new LangBuilder("Display.Main.Overview.Element.Plant").toString());
				}
				if (bPlayer.hasSubElement(Element.HEALING)) {
					list.add(new LangBuilder("Display.Main.Overview.Element.Healing").toString());
				}
				for (Element.SubElement sub : Element.getAddonSubElements(Element.WATER)) {
					if (bPlayer.hasElement(sub)) {
						list.add(new LangBuilder("Display.Main.Overview.Element." + sub.getName()).toString());
					}
				}
				if (list.size() > 0)
					s = s + GRAY + " " + new LangBuilder("Display.Main.Overview.Lore.SubList").list(list.toArray(new String[0]));

				List<String> l = Util.lengthSplit(s, ConfigStandard.getInstance().getOverviewTrim());
				for (String s1 : l) {
					item.addDescription(s1);
				}
			} else {
				item.addDescription(s);
			}
		}
		if (bPlayer.getElements().contains(Element.CHI))
		{
			String s = ChatColor.DARK_GRAY + "- " + ChatColor.GOLD + new LangBuilder("Display.Main.Overview.Element.Chi").toString();
			item.addDescription(s);
		}
		if (!Util.getStaff(player.getUniqueId()).equals("")) {
			item.addDescription(ChatColor.DARK_PURPLE + Util.getStaff(player.getUniqueId()));
		}
		
		if (openPlayer.hasPermission("bendinggui.admin"))
		{
			item.addDescription("");
			item.addDescription(ChatColor.RED + new LangBuilder("Display.Players.Admin.Click").player(player).toString());
		}
		return item;
	}
	
	public MenuItem getArrowItem(final boolean isRight)
	{
		int maxPage = players.size() / (5 * 7) + 1;
		String baseKey = "Display.Common.Page.";
		String typeKey = !isRight && page == 0 ? "Back" : (isRight ? "Next" : "Previous");
		//String s = ChatColor.YELLOW + (isRight ? "Next" : "Previous") + " Page" + ChatColor.DARK_GRAY +
				//"(" + ChatColor.YELLOW + this.page + ChatColor.DARK_GRAY + "/" + ChatColor.YELLOW + maxPage + ChatColor.DARK_GRAY + ")";
		//s = this.page == 0 ? ChatColor.YELLOW + "Return to Menu" : s;
		MenuItem item = new MenuItem(ChatColor.YELLOW + new LangBuilder(baseKey + typeKey + ".Title").toString(), Material.ARROW)
		{
			@Override
			public void onClick(Player player) 
			{
				if (page == 0 && !isRight)
				{
					switchMenu(player, prev);
					return;
				}

				if (page == maxPage - 1 && isRight) return;
				page = isRight ? page + 1 : page - 1;
				if (isShiftClicked) {
					page = isRight ? maxPage - 1 : 0;
				}

				update();

				/*if (page == 0 && !isRight)
				{
					switchMenu(player, prev);
				}
				else
				{
					if (isRight) page = page + 1;
					else page = page - 1;
					update();
				}*/
			}
		};
		item.setDescriptions(Arrays.asList((ChatColor.GRAY + new LangBuilder(baseKey + typeKey + ".Lore").toString()).split("\\n")));

		/*if (isRight)
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
		}*/
		
		return item;
	}
	
	public MenuItem getOfflinePlayerToggle()
	{
		String key = "Display.Players.ToggleOffline." + (this.showOffinePlayers ? "On" : "Off" );
		MenuItem item = new MenuItem(ChatColor.RED + new LangBuilder(key + ".Title").toString(), Material.ENDER_EYE)
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
		item.addDescription(ChatColor.GRAY + new LangBuilder(key + ".Lore").toString());
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
