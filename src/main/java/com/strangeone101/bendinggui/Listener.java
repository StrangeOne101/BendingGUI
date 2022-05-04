package com.strangeone101.bendinggui;

import com.projectkorra.projectkorra.ability.util.MultiAbilityManager;
import com.strangeone101.bendinggui.config.ConfigStandard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;

import com.strangeone101.bendinggui.menus.MenuBendingOptions;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class Listener implements org.bukkit.event.Listener {

	private final int NAME = "\u00A7aConfigure Bending".hashCode();

	@EventHandler(priority = EventPriority.LOW)
	public void onItemRightClick(PlayerInteractEvent e) {
		if (e.getHand() == EquipmentSlot.OFF_HAND) return;
		try {
			if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				//The first half of the if statement checks for old BendingGUI items that don't have the NBT marker on them
				if (isCompass(e.getItem())) {
					if (!BendingGUI.enabled) {
						if (!BendingGUI.versionInfo.equals("")) {
							if (e.getPlayer().hasPermission("bendinggui.admin")) {
								String s = BendingGUI.versionInfo.startsWith("!") ? "WARNING: " + BendingGUI.versionInfo.substring(1) : BendingGUI.versionInfo;
								e.getPlayer().sendMessage(ChatColor.RED + "[BendingGUI] " + s);
								e.getPlayer().sendMessage(ChatColor.RED + "[BendingGUI] If you wish to disable this message, you can do so in the config.");
							} else {
								e.getPlayer().sendMessage(ChatColor.RED + new LangBuilder("Display.Errors.Disabled").toString());
							}
							
						}
						return;
					}
					
					if (MultiAbilityManager.playerAbilities.containsKey(e.getPlayer())) {
						e.getPlayer().sendMessage(ChatColor.RED + new LangBuilder("Display.Errors.CantEditNow").toString());
					} else {
						MenuBendingOptions menu = new MenuBendingOptions(e.getPlayer());
						menu.openMenu(e.getPlayer());
					}
					
					e.setCancelled(true);
				}
			}		
		}
		catch (Exception exception) {
			e.setCancelled(true);
			exception.printStackTrace();
			e.getPlayer().sendMessage(ChatColor.RED + new LangBuilder("Display.Errors.FailedToOpen").toString());
		}
	}

	@EventHandler
	public void inventoryPickupEvent(InventoryPickupItemEvent event) {
		if (isCompass(event.getItem().getItemStack()) && ConfigStandard.getInstance().doDestroyOnStore()) {
			event.setCancelled(true);
			event.getItem().remove();
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void throwItem(PlayerDropItemEvent event) {
		if (isCompass(event.getItemDrop().getItemStack()) && ConfigStandard.getInstance().doDestroyOnThrow()) {
			event.getItemDrop().remove();
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		if (!event.getKeepInventory() && ConfigStandard.getInstance().doDestroyOnDeath()) {
			event.getDrops().removeIf(this::isCompass);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onMenuItemClicked(InventoryClickEvent event) {
		if (!BendingGUI.enabled || event.isCancelled()) return;
		try {
			Inventory inventory = event.getInventory();
	        if (inventory.getHolder() instanceof MenuBase) {
	            MenuBase menu = (MenuBase) inventory.getHolder();
	            if (event.getWhoClicked() instanceof Player) {
	                Player player = (Player) event.getWhoClicked();
	                if (event.getSlotType() != InventoryType.SlotType.OUTSIDE) {
	                	int index = event.getRawSlot();
	                    if (index < inventory.getSize()) {
	                    	if (event.getCursor() != null && event.getCursor().getType() != Material.AIR) {
	                    		event.setCancelled(true);
	                    		menu.closeMenu(player);
	                    		player.sendMessage(ChatColor.RED + new LangBuilder("Display.Errors.NoTouchy").toString());
	                    	}
	                    	MenuItem item = menu.getMenuItem(index);
	                    	if (item != null)
	                    	{
	                    		item.isShiftClicked = event.isShiftClick();
	                    		item.onClick(player);
	                    		event.setCancelled(true);
	                    	}
	                    }
	                    else {
	                    	if (event.isShiftClick()) {
	                    		event.setCancelled(true);
	                    	}
	                    	menu.setLastClickedSlot(index);
	                    }
	                }
	            }
	        }
		}
		catch (Exception e) {
			event.getWhoClicked().closeInventory();
			event.getWhoClicked().sendMessage(ChatColor.RED + new LangBuilder("Display.Errors.ClickEvent").toString());
			e.printStackTrace();
		}
    }

	@EventHandler(priority = EventPriority.HIGH)
	public void onMenuItemClickedHigh(InventoryClickEvent event) {
		//If the item is a compass AND it is in the top of the inventory, not the player part
		if (event.getCursor() != null && isCompass(event.getCursor()) && event.getRawSlot() < event.getInventory().getSize()) {
			boolean prevent = isChest(event.getInventory()) ? ConfigStandard.getInstance().doDestroyOnStoreInChests() : ConfigStandard.getInstance().doDestroyOnStore();
			if (prevent) {
				event.setResult(Event.Result.DENY);
			}
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		if (!BendingGUI.enabled) return;
		if (e.getInventory() instanceof MenuBendingOptions) {
			MenuBendingOptions menu = (MenuBendingOptions) e.getInventory();
			if (DynamicUpdater.players.containsKey(menu.getMenuPlayer().getUniqueId()) && DynamicUpdater.players.get(menu.getOpenPlayer().getUniqueId()).contains(menu.getOpenPlayer().getUniqueId())) {
				DynamicUpdater.players.get(menu.getOpenPlayer().getUniqueId()).remove(menu.getOpenPlayer().getUniqueId());
			}
		}
	}
	
	@EventHandler
	public void onLogin(final PlayerLoginEvent e) {
		Bukkit.getScheduler().runTaskLater(BendingGUI.INSTANCE, () -> {
			if (e.getPlayer().hasPermission("bendinggui.admin") && ConfigStandard.getInstance().hasAdminAlerts()) {
				if (!BendingGUI.versionInfo.equals("")) {
					String s = BendingGUI.versionInfo.startsWith("!") ? "WARNING: " + BendingGUI.versionInfo.substring(1) : BendingGUI.versionInfo;
					e.getPlayer().sendMessage(ChatColor.RED + "[BendingGUI] " + s);
					e.getPlayer().sendMessage(ChatColor.RED + "[BendingGUI] If you wish to disable this message, you can do so in the config.");
				}
			}

		}, 1L);
	}


	private boolean isCompass(ItemStack stack) {
		return stack != null && stack.getItemMeta() != null && (stack.getItemMeta().getDisplayName().hashCode() == NAME
				|| stack.getItemMeta().getPersistentDataContainer().has(BendingGUI.COMPASS, PersistentDataType.BYTE));
	}

	private boolean isChest(Inventory inventory) {
		return inventory.getType() == InventoryType.CHEST && (inventory.getSize() == 9 * 3 || inventory.getSize() == 9 * 6) && inventory.getHolder() instanceof Chest;
	}
}
