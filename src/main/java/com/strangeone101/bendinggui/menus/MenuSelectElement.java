package com.strangeone101.bendinggui.menus;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.strangeone101.bendinggui.BendingBoard;
import com.strangeone101.bendinggui.BendingGUI;
import com.strangeone101.bendinggui.LangBuilder;
import com.strangeone101.bendinggui.Util;
import com.strangeone101.bendinggui.api.ChooseSupport;
import com.strangeone101.bendinggui.api.ElementSupport;
import com.strangeone101.bendinggui.config.ConfigStandard;
import com.strangeone101.bendinggui.spirits.SpiritsSupport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.Element.SubElement;
import com.projectkorra.projectkorra.event.PlayerChangeElementEvent;
import com.projectkorra.projectkorra.event.PlayerChangeElementEvent.Result;
import com.strangeone101.bendinggui.MenuBase;
import com.strangeone101.bendinggui.MenuItem;
import com.strangeone101.bendinggui.RunnablePlayer;

public class MenuSelectElement extends MenuBase 
{
	public OfflinePlayer thePlayer;
	public MenuBase menu_ = null;
	public Player openPlayer;
	
	public MenuSelectElement(OfflinePlayer player) 
	{
		super(new LangBuilder("Display.Choose.Title").toString(), 3);
		this.thePlayer = player;
	}
	
	public MenuSelectElement(OfflinePlayer player, MenuBase previousMenu)
	{
		this(player);
		this.menu_ = previousMenu;
	}
	
	public void update()
	{
		this.addMenuItem(this.getChooseElement(Element.FIRE), 2 + 9);
		this.addMenuItem(this.getChooseElement(Element.WATER), 3 + 9);
		this.addMenuItem(this.getChooseElement(Element.CHI), 4 + 9);
		this.addMenuItem(this.getChooseElement(Element.EARTH), 5 + 9);
		this.addMenuItem(this.getChooseElement(Element.AIR), 6 + 9);
		
		if (menu_ != null)
		{
			MenuItem item = new MenuItem(ChatColor.YELLOW + new LangBuilder("Display.Common.Page.Back.Title").toString(), Material.ARROW) {
				@Override
				public void onClick(Player player) 
				{
					switchMenu(player, menu_);
				}
			};
			item.setDescriptions(List.of(new LangBuilder("Display.Common.Page.Back.Lore").toString().split("\n")));
			this.addMenuItem(item, 18);
		}

		for (Element customElement : BendingGUI.INSTANCE.getSupportedElements()) {
			ElementSupport support = BendingGUI.INSTANCE.getSupportedElement(customElement);
			if (support instanceof ChooseSupport) {
				this.addMenuItem(this.getChooseElement(support.getElement()), ((ChooseSupport) support).getChooseMenuIndex());
			}
		}
	}
	
	public MenuItem getChooseElement(final Element element)
	{
		BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(thePlayer);
		Material mat = ConfigStandard.getInstance().getChooseIconMaterial(element);
		ChatColor c1 = ChatColor.YELLOW;
		if (element == Element.AIR) {c1 = ChatColor.WHITE;}
		else if (element == Element.EARTH) {c1 = ChatColor.GREEN;}
		else if (element == Element.FIRE) {c1 = ChatColor.RED;}
		else if (element == Element.WATER) {c1 = ChatColor.BLUE;}
		final MenuSelectElement instance = this;
		final MenuConfirm confirm = new MenuConfirm(this, new RunnablePlayer() {
			@Override
			public void run(Player player) 
			{
				if (thePlayer instanceof Player && !(((Player)thePlayer).hasPermission("bending." + element.getName().toLowerCase())) && thePlayer == openPlayer)
				{
					player.sendMessage(ChatColor.RED + new LangBuilder("Chat.Choose.NoPermissionElement").element(element).toString());
					player.closeInventory();
					return;
				}

				if (SpiritsSupport.isSpiritElement(element)) {
					SpiritsSupport.giveElement(element, bPlayer, player, true);
				} else {
					bPlayer.setElement(element);
					Bukkit.getServer().getPluginManager().callEvent(new PlayerChangeElementEvent(player, (Player)thePlayer, element, Result.CHOOSE));
				}
				
				for (SubElement sub : Element.getAllSubElements()) {
					if (sub.getParentElement() == element && bPlayer.hasSubElementPermission(sub)) {
						bPlayer.addSubElement(sub);
					}
				}

				if (!MenuBendingOptions.canChangeInstantly((Player)thePlayer)) {
					bPlayer.addCooldown("ChooseElement", ConfigManager.defaultConfig.get().getLong("Properties.ChooseCooldown"), true);
				}
				
				GeneralMethods.removeUnusableAbilities(bPlayer.getName());
				BendingBoard.updateBoard(player);
				
				if (thePlayer instanceof Player)
					((Player)thePlayer).sendMessage(ChatColor.YELLOW + new LangBuilder("Chat.Choose.Success.Self").element(element).anOrA(element.getName()).toString());
				
				GeneralMethods.saveElements(bPlayer);
				GeneralMethods.saveSubElements(bPlayer);
				
				player.closeInventory();
			}
		}, new RunnablePlayer() {
			
			@Override
			public void run(Player player) {
				switchMenu(player, instance);
				
			}
		}, (context) -> context.element(element).player(thePlayer)
				.yourOrPlayer(thePlayer, openPlayer)
				.plural(thePlayer.getName()).anOrA(element.getName()),
	"Choose");
		MenuItem item = new MenuItem(c1 + new LangBuilder("Display.Choose." + element.getName() + ".Title").element(element).toString(), mat) {

			@Override
			public void onClick(Player player) 
			{
				if (thePlayer != openPlayer)
				{
					if (!openPlayer.hasPermission("bending.admin.choose"))
					{
						openPlayer.sendMessage(ChatColor.RED + new LangBuilder("Chat.Choose.Admin.NoPermission").player(thePlayer).toString());
						closeMenu(openPlayer);
						return;
					}
					//openPlayer.sendMessage(ChatColor.YELLOW + "You have made " + thePlayer.getName() + " a " + c3 + (element == Element.CHI ? "Chiblocker" : element.toString()));
					openPlayer.sendMessage(ChatColor.GREEN + new LangBuilder("Chat.Choose.Success.Admin").element(element).player(thePlayer).anOrA(element.getName()).toString());
				}
				else
				{
					if (bPlayer != null && !bPlayer.getElements().isEmpty() && !openPlayer.hasPermission("bending.command.rechoose"))
					{
						openPlayer.sendMessage(ChatColor.RED + new LangBuilder("Chat.Choose.Rechoose.NoPermission").toString());
						closeMenu(openPlayer);
					}
				}
				
				confirm.openMenu(openPlayer);
			}
		};
		item.setDescriptions(Util.lengthSplit(new LangBuilder("Display.Choose." + element.getName() + ".Lore").toString(),
				ConfigStandard.getInstance().getElementTrim()));

		return item;
	}

	public void openMenu(Player player) 
	{
		openPlayer = player; 
		
		if (BendingPlayer.getBendingPlayer(thePlayer).isPermaRemoved())
		{
			if (openPlayer.getName().equals(thePlayer.getName()))
			{
				player.sendMessage(ChatColor.RED + new LangBuilder("Chat.Choose.PermaRemoved").toString());
			}
			else
			{
				player.sendMessage(ChatColor.RED + new LangBuilder("Chat.Choose.Admin.PermaRemoved").player(thePlayer).toString());
			}
			closeMenu(player);
			return;
		}
		
		this.update();
		super.openMenu(player);
	}
}
