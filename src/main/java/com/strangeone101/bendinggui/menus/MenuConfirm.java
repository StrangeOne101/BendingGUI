package com.strangeone101.bendinggui.menus;

import java.util.List;

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
	protected List<String> loreConfirm;
	protected List<String> loreCancel;
	
	public MenuConfirm(MenuBase previousMenu, RunnablePlayer confirm, RunnablePlayer cancel, List<String> confirmLore, List<String> cancelLore) 
	{
		super("Are you sure?", 3);
		this.prev = previousMenu;
		this.runConfirm = confirm;
		this.runCancel = cancel;
		this.loreConfirm = confirmLore;
		this.loreCancel = cancelLore;
		this.addMenuItem(getConfirm(), (this.getInventory().getSize() - 1) / 2 - 1);
		this.addMenuItem(getCancel(),(this.getInventory().getSize() - 1) / 2 + 1);
		
	}

	protected MenuItem getConfirm()
	{
		MenuItem confirm = new MenuItem(ChatColor.GREEN + "" + ChatColor.BOLD + "YES", Material.LIME_WOOL) {
			@Override
			public void onClick(Player player) 
			{
				runConfirm.run(player);
			}
		};
		confirm.setDescriptions(loreConfirm);
		return confirm;
	}
	

	private MenuItem getCancel()
	{
		MenuItem cancel = new MenuItem(ChatColor.RED + "" + ChatColor.BOLD + "NO", Material.RED_WOOL) {
			@Override
			public void onClick(Player player) 
			{
				runCancel.run(player);
			}
		};
		cancel.setDescriptions(loreCancel);
		return cancel;
	}
}
