package com.strangeone101.bendinggui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.bukkit.OfflinePlayer;

import com.projectkorra.ProjectKorra.GeneralMethods;
import com.projectkorra.ProjectKorra.Ability.AbilityModuleManager;

public class PlayerGuiData 
{
	protected OfflinePlayer player;
	protected int page = 0;
	public List<String> abilityData = new ArrayList<String>();
	
	protected static HashMap<UUID, PlayerGuiData> guiData = new HashMap<UUID, PlayerGuiData>();
	
	
	protected PlayerGuiData(OfflinePlayer player)
	{
		this.player = player;
		if (!guiData.containsKey(player.getUniqueId()))
		{
			guiData.put(player.getUniqueId(), this);
		}
	}
	
	public void setPage(int page)
	{
		this.page = page;
	}
	
	public int getPage()
	{
		return page;
	}
	
	public UUID getPlayerUUID()
	{
		return player.getUniqueId();
	}
	
	public static PlayerGuiData getData(OfflinePlayer player)
	{
		if (!guiData.containsKey(player.getUniqueId()))
		{
			return new PlayerGuiData(player);
		}
		return guiData.get(player.getUniqueId());
	}
	
	/**This is required because of a stupid bug when right clicking on a block
	 * with the GUI makes it fail 600 times for no reason at all. Not a fix,
	 * simply a workaround.*/
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
	}
}
