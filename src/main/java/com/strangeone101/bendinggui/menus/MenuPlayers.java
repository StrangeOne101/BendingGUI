package com.strangeone101.bendinggui.menus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.strangeone101.bendinggui.LangBuilder;
import com.strangeone101.bendinggui.api.ElementOrder;
import com.strangeone101.bendinggui.config.ConfigStandard;
import com.strangeone101.bendinggui.spirits.SpiritsSupport;
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
	public Player openPlayer;
	public boolean showOffinePlayers;

	public static Map<Integer, Function<MenuPlayers, MenuItem>> CUSTOM_ICONS = new HashMap<>();

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

		//Add custom items to the menu
		for (int index : CUSTOM_ICONS.keySet()) {
			if (this.getMenuItem(index) != null) this.removeMenuItem(index);

			this.addMenuItem(CUSTOM_ICONS.get(index).apply(this), index);
		}
	}

	public MenuItem getPlayerItem(final OfflinePlayer player)
	{
		String titleKey = "Display.Players." + (player == this.openPlayer ? "You" : (player.isOnline() ? "Online" : "Offline"));
		ChatColor color = !player.isOnline() ? ChatColor.GRAY : ChatColor.YELLOW;
		BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
		Element displayElement = bPlayer == null || bPlayer.getElements().size() == 0 ? null : (bPlayer.getElements().size() > 1 ? Element.AVATAR : bPlayer.getElements().get(0));

		MenuItem item = new MenuItem(color + new LangBuilder(titleKey).player(player).element(displayElement).toString(), Material.PLAYER_HEAD)
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
		if (player instanceof Player && SpiritsSupport.isAvatar(bPlayer)) {
			item.addDescription(Element.AVATAR.getColor() + new LangBuilder("Display.Main.Overview.Lore.Avatar" + (player.equals(openPlayer) ?  "Self" : "")).player(player).plural(player.getName()).toString());
		}
		boolean b = bPlayer.hasElement(Element.EARTH) || bPlayer.hasElement(Element.AIR);
		item.addDescription(GRAY + new LangBuilder("Display.Main.Overview.Lore.ElementPrefix").yourOrPlayer(player, openPlayer).anOrA(b ? "airOrEarth" : "").capitalizeFirst().toString());

		for (Element element : ElementOrder.getParentElements()) {
			if (element == Element.AVATAR) continue; //We won't show Avatar in the list (since we show it above)
			if (!bPlayer.hasElement(element)) continue;

			String s = ChatColor.DARK_GRAY + "- " + BendingGUI.getColor(element) + new LangBuilder("Display.Main.Overview.Element." + element.getName());

			if (ConfigStandard.getInstance().doShowSubElements()) {
				List<String> list = new ArrayList<>();

				//Get all subs for this element
				for (Element.SubElement sub : ElementOrder.getSubelements()) {
					if (sub.getParentElement() == element && bPlayer.hasSubElement(sub)) { //And if they have it
						String subString = new LangBuilder("Display.Main.Overview.Element." + sub.getName()).toString();
						if (subString != null && !subString.equals("")) list.add(subString); //This will allow us to skip Ice since its pointless to display (ice is set to "")
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
			}
		};
		item.setDescriptions(Arrays.asList((ChatColor.GRAY + new LangBuilder(baseKey + typeKey + ".Lore").toString()).split("\\n")));

		if (!isRight && page == 0) {
			item.setModelData(ConfigStandard.getInstance().getModelDataBase() + 32);
		} else {
			item.setModelData(ConfigStandard.getInstance().getModelDataBase() + 2056 + (isRight ? 1 : 0));
		}


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
		item.setModelData(ConfigStandard.getInstance().getModelDataBase() + 2060);
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
