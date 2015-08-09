package com.strangeone101.bendinggui.menus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.event.PlayerChangeElementEvent;
import com.projectkorra.projectkorra.event.PlayerChangeElementEvent.Result;
import com.strangeone101.bendinggui.Config;
import com.strangeone101.bendinggui.MenuBase;
import com.strangeone101.bendinggui.MenuItem;
import com.strangeone101.bendinggui.RunnablePlayer;

public class MenuElementSelect extends MenuBase 
{
	public OfflinePlayer thePlayer;
	public MenuBase menu = null;
	public Player openPlayer;
	
	public MenuElementSelect(OfflinePlayer player) 
	{
		super("Please select an element!", 3);
		this.thePlayer = player;
	}
	
	public MenuElementSelect(OfflinePlayer player, MenuBase previousMenu)
	{
		this(player);
		this.menu = previousMenu;
	}
	
	public void update()
	{
		this.addMenuItem(this.getChooseElement(Element.Fire), 2 + 9);
		this.addMenuItem(this.getChooseElement(Element.Water), 3 + 9);
		this.addMenuItem(this.getChooseElement(Element.Chi), 4 + 9);
		this.addMenuItem(this.getChooseElement(Element.Earth), 5 + 9);
		this.addMenuItem(this.getChooseElement(Element.Air), 6 + 9);
		
		if (menu != null)
		{
			MenuItem item = new MenuItem(ChatColor.YELLOW + "Return to Menu", new MaterialData(Material.ARROW)) {
				@Override
				public void onClick(Player player) 
				{
					switchMenu(player, menu);
				}
			};
			this.addMenuItem(item, 18);
		}
	}
	
	/*private class ChooseElementItem extends MenuItem
	{
		protected Element type;
		
		public ChooseElementItem(String itemName, MaterialData item, Element bending)
		{
			super(itemName, item);
			this.type = bending;
		}
		
		@Override
		public void onClick(Player player) 
		{
			
			MenuConfirm confirm = new MenuConfirm();
			
			getMenu().switchMenu(player, new MenuElementConfirm(type));
			//this.getItemStack().addEnchantment(Enchantment.SILK_TOUCH, 0);
			//this.addDescription(ChatColor.GREEN + "" + ChatColor.BOLD + "SELECTED!");
		}
	};*/
	
	public MenuItem getChooseElement(final Element element)
	{
		MaterialData mat = new MaterialData(Material.STICK);
		ChatColor c1 = ChatColor.YELLOW;
		ChatColor c2 = ChatColor.GOLD;
		final boolean b1 = thePlayer == openPlayer;
		if (element == Element.Air) {mat = new MaterialData(Material.STRING); c1 = ChatColor.WHITE; c2 = ChatColor.GRAY;}
		else if (element == Element.Earth) {mat = new MaterialData(Material.GRASS); c1 = ChatColor.GREEN; c2 = ChatColor.DARK_GREEN;}
		else if (element == Element.Fire) {mat = new MaterialData(Material.BLAZE_POWDER); c1 = ChatColor.RED; c2 = ChatColor.DARK_RED;}
		else if (element == Element.Water) {mat = new MaterialData(Material.WATER_BUCKET); c1 = ChatColor.BLUE; c2 = ChatColor.DARK_BLUE;}	
		final ChatColor c3 = c1;
		final MenuElementSelect instance = this;
		final MenuConfirm confirm = new MenuConfirm(this, new RunnablePlayer() {
			@Override
			public void run(Player player) 
			{
				if (thePlayer instanceof Player && !(((Player)thePlayer).hasPermission("bending." + element.toString().toLowerCase())) && thePlayer.getName().equals(openPlayer.getName()))
				{
					player.sendMessage(ChatColor.RED + "You don't have permission to choose this element!");
					player.closeInventory();
					return;
				}
				GeneralMethods.getBendingPlayer(thePlayer.getName()).setElement(element);
				GeneralMethods.removeUnusableAbilities(thePlayer.getName());
				if (thePlayer instanceof Player) ((Player)thePlayer).sendMessage(ChatColor.YELLOW + "You are now a " + c3 + (element == Element.Chi ? "chiblocker" : element.toString().toLowerCase() + ChatColor.YELLOW + " bender!"));
				GeneralMethods.saveElements(GeneralMethods.getBendingPlayer(thePlayer.getName()));
				if (thePlayer instanceof Player)
				{
					Bukkit.getServer().getPluginManager().callEvent(new PlayerChangeElementEvent((Player)thePlayer, (Player)thePlayer, element, Result.CHOOSE));
				}
				player.closeInventory();
			}
		}, new RunnablePlayer() {
			
			@Override
			public void run(Player player) {
				switchMenu(player, instance);
				
			}
		}, Arrays.asList(new String[] {ChatColor.GRAY + "Are you sure you want to choose " + c3 + element.toString().toUpperCase() + ChatColor.RESET + ChatColor.GRAY + "?", ChatColor.GRAY + "This cannot be changed!"})
		, Arrays.asList(new String[] {ChatColor.GRAY + "Return to the previous menu"}));
		MenuItem item = new MenuItem(c1 + "Choose " + c2 + ChatColor.BOLD + (element == Element.Chi ? "CHIBLOCKER" : element.toString().toUpperCase()), mat) {

			@Override
			public void onClick(Player player) 
			{
				if (!b1)
				{
					if (!openPlayer.hasPermission("bending.admin.choose"))
					{
						openPlayer.sendMessage(ChatColor.RED + "You don't have permission to choose this player's bending!");
						closeMenu(openPlayer);
						return;
					}
					openPlayer.sendMessage(ChatColor.YELLOW + "You have made " + thePlayer.getName() + " a " + c3 + (element == Element.Chi ? "Chiblocker" : element.toString()));
				}
				else
				{
					if (GeneralMethods.getBendingPlayer(thePlayer.getName()) != null && !GeneralMethods.getBendingPlayer(thePlayer.getName()).getElements().isEmpty() && !openPlayer.hasPermission("bending.command.rechoose"))
					{
						openPlayer.sendMessage(ChatColor.RED + "You don't have permission to change your element!");
						closeMenu(openPlayer);
					}
				}
				
				confirm.openMenu(openPlayer);
			}
		};
		switch (element)
		{
		case Fire:
			item.setDescriptions(this.getDesc(Config.fireDesc));
			break;
		case Water:
			item.setDescriptions(this.getDesc(Config.waterDesc));
			break;
		case Air:
			item.setDescriptions(this.getDesc(Config.airDesc));
			break;
		case Earth:
			item.setDescriptions(this.getDesc(Config.earthDesc));
			break;
		case Chi:
			item.setDescriptions(this.getDesc(Config.chiDesc));
			break;
		default:
			break;
		}
		
		return item;
	}
	
	protected List<String> getDesc(String line)
	{
		int maxLenght = 55;
		Pattern p = Pattern.compile("\\G\\s*(.{1,"+maxLenght+"})(?=\\s|$)", Pattern.DOTALL);
		Matcher m = p.matcher(line);
		List<String> l = new ArrayList<String>();
		while (m.find())
		{
			l.add(ChatColor.GRAY + m.group(1));
		}
		return l;
	}

	public void openMenu(Player player) 
	{
		openPlayer = player; 
		
		if (GeneralMethods.getBendingPlayer(thePlayer.getName()).isPermaRemoved())
		{
			if (openPlayer.getName().equals(thePlayer.getName()))
			{
				player.sendMessage(ChatColor.RED + "You cannot choose an element because your bending has been permanently removed!");
			}
			else
			{
				player.sendMessage(ChatColor.RED + "This player has had their bending permanently removed!");
			}
			closeMenu(player);
			return;
		}
		
		this.update();
		super.openMenu(player);
	};
}
