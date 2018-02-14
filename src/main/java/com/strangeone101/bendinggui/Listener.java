package com.strangeone101.bendinggui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;

import com.strangeone101.bendinggui.menus.MenuBendingOptions;

public class Listener implements org.bukkit.event.Listener 
{
	@EventHandler(priority = EventPriority.LOW)
	public void onItemRightClick(PlayerInteractEvent e)
	{
		if (e.getHand() == EquipmentSlot.OFF_HAND) return;
		try
		{
			if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
			{
				if (e.getItem() != null && e.getItem().isSimilar(BendingGUI.getGuiItem()))
				{
					if (!BendingGUI.enabled) {
						if (!BendingGUI.versionInfo.equals("")) {
							if (e.getPlayer().hasPermission("bendinggui.admin")) {
								String s = BendingGUI.versionInfo.startsWith("!") ? "WARNING: " + BendingGUI.versionInfo.substring(1) : BendingGUI.versionInfo;
								e.getPlayer().sendMessage(ChatColor.RED + "[BendingGUI] " + s);
								e.getPlayer().sendMessage(ChatColor.RED + "[BendingGUI] If you wish to disable this message, you can do so in the config.");
							} else {
								e.getPlayer().sendMessage(ChatColor.RED + "There is a problem with BendingGUI at the moment. Please contact your admin!");
							}
							
						}
						return;
					}
					MenuBendingOptions menu = new MenuBendingOptions(e.getPlayer());
					menu.openMenu(e.getPlayer());
					
					e.setCancelled(true);
				}
			}		
		}
		catch (Exception exception)
		{
			e.setCancelled(true);
			exception.printStackTrace();
			e.getPlayer().sendMessage(ChatColor.RED + "An error occured while trying to open the bending interface. Please report this to your admin or the plugin developer!");
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onMenuItemClicked(InventoryClickEvent event) 
	{
		if (!BendingGUI.enabled || event.isCancelled()) return;
		try
		{
			Inventory inventory = event.getInventory();
	        if (inventory.getHolder() instanceof MenuBase) 
	        {
	            MenuBase menu = (MenuBase) inventory.getHolder();
	            if (event.getWhoClicked() instanceof Player) 
	            {
	                Player player = (Player) event.getWhoClicked();
	                if (event.getSlotType() != InventoryType.SlotType.OUTSIDE) 
	                {
	                	int index = event.getRawSlot();
	                    if (index < inventory.getSize()) 
	                    {
	                    	if (event.getCursor().getType() != Material.AIR)
	                    	{
	                    		event.setCancelled(true);
	                    		menu.closeMenu(player);
	                    		player.sendMessage(ChatColor.RED + "The bending gui cannot be tampered with!");
	                    	}
	                    	MenuItem item = menu.getMenuItem(index);
	                    	if (item != null)
	                    	{
	                    		item.isShiftClicked = event.isShiftClick();
	                    		item.onClick(player);
	                    		event.setCancelled(true);
	                    	}
	                    }
	                    else
	                    {
	                    	if (event.isShiftClick()) {
	                    		event.setCancelled(true);
	                    	}
	                    	menu.setLastClickedSlot(index);
	                    }
	                }
	            }
	        }
		}
		catch (Exception e)
		{
			event.getWhoClicked().closeInventory();
			event.getWhoClicked().sendMessage(ChatColor.RED + "An error occured while processing the clickevent. Please report this to your admin or the plugin developer!");
			e.printStackTrace();
		}
    }
	
	@EventHandler
	public void onClose(InventoryCloseEvent e)
	{
		if (!BendingGUI.enabled) return;
		if (e.getInventory() instanceof MenuBendingOptions)
		{
			MenuBendingOptions menu = (MenuBendingOptions) e.getInventory();
			if (DynamicUpdater.players.containsKey(menu.getMenuPlayer().getUniqueId()) && DynamicUpdater.players.get(menu.getOpenPlayer().getUniqueId()).contains(menu.getOpenPlayer()))
			{
				DynamicUpdater.players.get(menu.getOpenPlayer().getUniqueId()).remove(menu.getOpenPlayer());
			}
		}
	}
	
	@EventHandler
	public void onLogin(final PlayerLoginEvent e) {
		Bukkit.getScheduler().runTaskLater(BendingGUI.INSTANCE, new Runnable() {
			public void run() {
				if (e.getPlayer().hasPermission("bendinggui.admin") && Config.alerts) {
					if (!BendingGUI.versionInfo.equals("")) {
						String s = BendingGUI.versionInfo.startsWith("!") ? "WARNING: " + BendingGUI.versionInfo.substring(1) : BendingGUI.versionInfo;
						e.getPlayer().sendMessage(ChatColor.RED + "[BendingGUI] " + s);
						e.getPlayer().sendMessage(ChatColor.RED + "[BendingGUI] If you wish to disable this message, you can do so in the config.");
					}
				}
				
			}}, 1L);
		
	}
}
