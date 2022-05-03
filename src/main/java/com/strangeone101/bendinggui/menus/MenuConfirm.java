package com.strangeone101.bendinggui.menus;

import java.util.List;

import com.strangeone101.bendinggui.LangBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.strangeone101.bendinggui.MenuBase;
import com.strangeone101.bendinggui.MenuItem;
import com.strangeone101.bendinggui.RunnablePlayer;

public class MenuConfirm extends MenuBase 
{
	protected MenuBase prev;
	protected RunnablePlayer runConfirm;
	protected RunnablePlayer runCancel;
	private String lang;
	
	public MenuConfirm(MenuBase previousMenu, RunnablePlayer confirm, RunnablePlayer cancel, String langMenuName)
	{
		super(new LangBuilder("Display." + langMenuName + ".Confirm.Title").toString(), 3);
		this.prev = previousMenu;
		this.runConfirm = confirm;
		this.runCancel = cancel;
		this.lang = langMenuName;
		this.addMenuItem(getConfirm(), (this.getInventory().getSize() - 1) / 2 - 1);
		this.addMenuItem(getCancel(),(this.getInventory().getSize() - 1) / 2 + 1);
		
	}

	protected MenuItem getConfirm()
	{
		MenuItem confirm = new MenuItem(ChatColor.GREEN + new LangBuilder("Display." + lang + ".Confirm.Yes.Title").toString(), Material.LIME_WOOL) {
			@Override
			public void onClick(Player player) 
			{
				runConfirm.run(player);
			}
		};
		confirm.addDescription(ChatColor.GRAY + new LangBuilder("Display." + lang + ".Confirm.Yes.Lore").toString());
		return confirm;
	}
	

	private MenuItem getCancel()
	{
		MenuItem cancel = new MenuItem(ChatColor.RED + new LangBuilder("Display." + lang + ".Confirm.No.Title").toString(), Material.RED_WOOL) {
			@Override
			public void onClick(Player player) 
			{
				runCancel.run(player);
			}
		};
		cancel.addDescription(ChatColor.GRAY + new LangBuilder("Display." + lang + ".Confirm.No.Lore").toString());
		return cancel;
	}
}
