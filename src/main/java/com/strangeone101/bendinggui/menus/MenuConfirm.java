package com.strangeone101.bendinggui.menus;

import java.util.function.Function;

import com.strangeone101.bendinggui.LangBuilder;
import com.strangeone101.bendinggui.Util;
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
	protected Function<LangBuilder, LangBuilder> contextProvider;
	private String lang;
	
	public MenuConfirm(MenuBase previousMenu, RunnablePlayer confirm, RunnablePlayer cancel,
			Function<LangBuilder,LangBuilder> context, String langMenuName) {
		super(context.apply(new LangBuilder("Display." + langMenuName + ".Confirm.Title")).toString(), 3);
		this.prev = previousMenu;
		this.runConfirm = confirm;
		this.runCancel = cancel;
		this.lang = langMenuName;
		this.contextProvider = context;
		this.addMenuItem(getConfirm(), (this.getInventory().getSize() - 1) / 2 - 1);
		this.addMenuItem(getCancel(),(this.getInventory().getSize() - 1) / 2 + 1);
	}

	public MenuConfirm(MenuBase previousMenu, RunnablePlayer confirm, RunnablePlayer cancel, String langMenuName)
	{
		this(previousMenu, confirm, cancel, (e) -> e, langMenuName);
	}

	protected MenuItem getConfirm()
	{
		MenuItem confirm = new MenuItem(ChatColor.GREEN + this.contextProvider.apply(
				new LangBuilder("Display." + lang + ".Confirm.Yes.Title")).toString(), Material.LIME_WOOL) {
			@Override
			public void onClick(Player player) 
			{
				runConfirm.run(player);
			}
		};
		confirm.setDescriptions(Util.lengthSplit(ChatColor.GRAY + this.contextProvider.apply(
				new LangBuilder("Display." + lang + ".Confirm.Yes.Lore")).toString(), 45));
		return confirm;
	}
	

	private MenuItem getCancel()
	{
		MenuItem cancel = new MenuItem(ChatColor.RED + this.contextProvider.apply(new LangBuilder("Display." + lang + ".Confirm.No.Title")).toString(), Material.RED_WOOL) {
			@Override
			public void onClick(Player player) 
			{
				runCancel.run(player);
			}
		};
		cancel.setDescriptions(Util.lengthSplit(ChatColor.GRAY + this.contextProvider.apply(new LangBuilder("Display." + lang + ".Confirm.No.Lore")).toString(), 45));
		return cancel;
	}
}
