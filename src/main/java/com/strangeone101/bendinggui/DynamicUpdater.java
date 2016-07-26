package com.strangeone101.bendinggui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import com.strangeone101.bendinggui.menus.MenuBendingOptions;

public class DynamicUpdater 
{
	/**Standard GUI*/
	protected static HashMap<UUID, MenuBendingOptions> guiData = new HashMap<UUID, MenuBendingOptions>();
	protected static HashMap<UUID, List<UUID>> players = new HashMap<UUID, List<UUID>>();
	protected static HashMap<UUID, Integer> pages = new HashMap<UUID, Integer>();
	protected static HashMap<UUID, Integer> combopages = new HashMap<UUID, Integer>();
	
	/**Gets the menu for the player given.*/
	public static MenuBendingOptions getMenu(OfflinePlayer player)
	{
		if (guiData.keySet().contains(player.getUniqueId()))
		{
			return guiData.get(player.getUniqueId());
		}
		return null;
	}
	
	/**Update everyone elses Menu with the one provided.*/
	public static void updateMenu(OfflinePlayer player, MenuBendingOptions menu)
	{
		guiData.put(player.getUniqueId(), menu);
		if (!players.containsKey(player.getUniqueId()))
		{
			players.put(player.getUniqueId(), new ArrayList<UUID>());
		}
		for (UUID id : players.get(player.getUniqueId()))
		{
			if (Bukkit.getPlayer(id) == null)
			{
				players.get(player.getUniqueId()).remove(id);
				continue;
			}
			if (Bukkit.getPlayer(id).getOpenInventory() == null || Bukkit.getPlayer(id).getOpenInventory().getTopInventory() == null || !(Bukkit.getPlayer(id).getOpenInventory().getTopInventory() instanceof MenuBendingOptions))
			{
				players.get(player.getUniqueId()).remove(id);
				continue;
			}
			
			if (((MenuBendingOptions)Bukkit.getPlayer(id).getOpenInventory().getTopInventory()).getMenuPlayer().getUniqueId() == player.getUniqueId())
			{
				continue;
			}
			
			if (Bukkit.getPlayer(player.getUniqueId()) == menu.getOpenPlayer()) continue;
			
			MenuBendingOptions menu1 = (MenuBendingOptions)Bukkit.getPlayer(id).getOpenInventory().getTopInventory();
			menu1.updateFromMenu(menu);
		}
	}
	
	/*public static DynamicUpdater getData(OfflinePlayer player)
	{
		if (!guiData.containsKey(player.getUniqueId()))
		{
			return new DynamicUpdater(player);
		}
		return guiData.get(player.getUniqueId());
	}*/
	
	/**This is required because of a stupid bug when right clicking on a block
	 * with the GUI makes it fail 600 times for no reason at all. Not a fix,
	 * simply a workaround.*/
	/*@Deprecated
	public static void updateAbilityData(final OfflinePlayer player)
	{
		Runnable runnable = new Runnable() {

			public void run() {
				
				List<String> moves = new ArrayList<String>();
				HashSet<String> checked = new HashSet<String>();
				
				@SuppressWarnings("unchecked")
				Collection<String>[] order = new Collection[] {AbilityModuleManager.airbendingabilities, AbilityModuleManager.flightabilities, AbilityModuleManager.spiritualprojectionabilities, 
						AbilityModuleManager.earthbendingabilities, AbilityModuleManager.sandabilities, AbilityModuleManager.metalabilities, AbilityModuleManager.lavaabilities,
						AbilityModuleManager.firebendingabilities, AbilityModuleManager.combustionabilities, AbilityModuleManager.lightningabilities,
						AbilityModuleManager.waterbendingabilities, AbilityModuleManager.healingabilities, AbilityModuleManager.plantabilities, AbilityModuleManager.iceabilities, AbilityModuleManager.bloodabilities, 
						AbilityModuleManager.chiabilities, AbilityModuleManager.abilities};

				
				for (Collection<String> s : order)
				{
					if (s == AbilityModuleManager.abilities)
					{
						s = new HashSet<String>();
						s.addAll(AbilityModuleManager.abilities);
						s.removeAll(checked);
					}
					for (int i = 0; i < s.size(); i++)
					{
						String move = (String) s.toArray()[i];
						
						if (!((s == AbilityModuleManager.airbendingabilities || s == AbilityModuleManager.earthbendingabilities ||
								s == AbilityModuleManager.firebendingabilities || s == AbilityModuleManager.waterbendingabilities) &&
								AbilityModuleManager.subabilities.contains(move)))
						{
							if (GeneralMethods.canBend(player.getName(), move) && !moves.contains(move))
							{
								moves.add(move);
							}
						}
						checked.add(move);
					}
				}
				
				if (moves.size() == 0)
				{
					try 
					{
						Thread.sleep(200L);
						run();
					} 
					catch (InterruptedException e) {e.printStackTrace();}
				}
				else
				{
					getData(player).abilityData = moves;
				}
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
	}*/
	
	public static void setPage(OfflinePlayer player, int page)
	{
		pages.put(player.getUniqueId(), page);
	}
	
	public static int getPage(OfflinePlayer player)
	{
		if (pages.containsKey(player.getUniqueId()))
			return pages.get(player.getUniqueId());
		return 0;
	}
	
	public static void setComboPage(OfflinePlayer player, int page)
	{
		combopages.put(player.getUniqueId(), page);
	}
	
	public static int getComboPage(OfflinePlayer player)
	{
		if (combopages.containsKey(player.getUniqueId()))
			return combopages.get(player.getUniqueId());
		return 0;
	}
}
