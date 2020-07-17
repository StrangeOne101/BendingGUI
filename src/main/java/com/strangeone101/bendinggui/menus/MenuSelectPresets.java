package com.strangeone101.bendinggui.menus;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.Element.SubElement;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.strangeone101.bendinggui.MenuBase;
import com.strangeone101.bendinggui.MenuItem;

public class MenuSelectPresets extends MenuBase
{
	public Player thePlayer;

	public MenuSelectPresets(Player player) 
	{
		super("Preset Selecter", 4);
		this.thePlayer = player;
		
		update();
	}
	
	public void update()
	{
		for (int i = 0; i < 0; i++) {
			addMenuItem(getItemForSlot(thePlayer, i), i);
		}
	}

	public MenuItem getItemForSlot(OfflinePlayer player, final int index)
	{
		Material mat = Material.BLACK_STAINED_GLASS_PANE;
		final String move = BendingPlayer.getBendingPlayer(player).getAbilities().get(index + 1);
		ChatColor c = ChatColor.RED;
		if (move != null && !move.equals("null"))
		{
			Element element = CoreAbility.getAbility(move).getElement();
			if (element instanceof SubElement) element = ((SubElement)element).getParentElement();
			c = element == Element.AIR ? ChatColor.GRAY : (element == Element.CHI ? ChatColor.GOLD : (element == Element.EARTH ? ChatColor.GREEN : (element == Element.FIRE ? ChatColor.RED : (element == Element.WATER ? ChatColor.BLUE : (element == Element.AVATAR ? ChatColor.LIGHT_PURPLE : element.getColor())))));
			if (element == Element.AIR) {mat = Material.WHITE_STAINED_GLASS_PANE;}
			else if (element == Element.EARTH) {mat = Material.GREEN_STAINED_GLASS_PANE;}
			else if (element == Element.FIRE) {mat = Material.RED_STAINED_GLASS_PANE;}
			else if (element == Element.WATER) {mat = Material.BLUE_STAINED_GLASS_PANE;}
			else if (element == Element.CHI) {mat = Material.YELLOW_STAINED_GLASS_PANE;}
			else {mat = Material.PURPLE_STAINED_GLASS_PANE;}
		}		
		
		String itemname, desc = "";
		
		if (move == null || move.equals("null"))
		{
			itemname = ChatColor.RED + "Slot " + (index + 1) + GRAY + " (Empty)" ;
			desc = GRAY + "Nothing is currently bound to this slot!";
		}
		else
		{
			itemname = c + "Slot " + (index + 1) + GRAY + " (" + move + ")" ;
			desc = GRAY + "Currently Bound: " + c + ChatColor.BOLD + move + "\n\n" + ChatColor.RESET + GRAY + "To bind a new move, click a move then click\n" + GRAY + "the slot you want to bind it to.";
		}
		
		MenuItem item = new MenuItem(itemname, mat) {

			@Override
			public void onClick(Player player) {}
		};
		item.setDescriptions(Arrays.asList(desc.split("\n")));
		return item;
	}

}
