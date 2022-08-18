package com.strangeone101.bendinggui;

import com.projectkorra.projectkorra.ability.util.MultiAbilityManager;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.event.BendingPlayerCreationEvent;
import com.strangeone101.bendinggui.config.ConfigStandard;
import com.strangeone101.bendinggui.menus.MenuSelectElement;
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
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;

import com.strangeone101.bendinggui.menus.MenuBendingOptions;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Listener implements org.bukkit.event.Listener {

	private final int NAME = "\u00A7aConfigure Bending".hashCode();

	private static Map<UUID, Consumer<String>> CHAT_LISTENERS = new HashMap<>();

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

	@EventHandler
	public void onBendingPlayerCreate(BendingPlayerCreationEvent event) {
		if (event.getBendingPlayer().getElements().size() == 0 && !event.getBendingPlayer().isPermaRemoved()) {
			if (ConfigStandard.getInstance().doShowChoosePromptOnFirstJoin()) {
				MenuBendingOptions menu = new MenuBendingOptions(event.getBendingPlayer().getPlayer()); //We use this menu and not the choose menu
				menu.openMenu(event.getBendingPlayer().getPlayer());								     //Because this has a good redirect with messages and perm checks
			}
			if (ConfigStandard.getInstance().doGiveFirstJoin()) {
				addCompass(event.getBendingPlayer().getPlayer());
			}
		}
	}

	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent event) {
		if (ConfigStandard.getInstance().doGiveChangeWorld()) {
			if (!ConfigManager.defaultConfig.get().getStringList("Properties.DisabledWorlds").contains(event.getPlayer().getWorld().getName())) {
				addCompass(event.getPlayer());
			}
		}
	}

	@EventHandler
	public void onLogout(PlayerQuitEvent event) {
		CHAT_LISTENERS.remove(event.getPlayer().getUniqueId()); //Remove all players from here
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		if (CHAT_LISTENERS.containsKey(event.getPlayer().getUniqueId())) {
			Consumer<String> consumer = CHAT_LISTENERS.get(event.getPlayer().getUniqueId());

			//Do this on the main thread so it doesn't run async
			Bukkit.getScheduler().runTask(BendingGUI.INSTANCE, () -> consumer.accept(event.getMessage()));
			CHAT_LISTENERS.remove(event.getPlayer().getUniqueId());
			event.setCancelled(true);
		}
	}


	private boolean isCompass(ItemStack stack) {
		return stack != null && stack.getItemMeta() != null && (stack.getItemMeta().getDisplayName().hashCode() == NAME
				|| stack.getItemMeta().getPersistentDataContainer().has(BendingGUI.COMPASS, PersistentDataType.BYTE));
	}

	private boolean isChest(Inventory inventory) {
		return inventory.getType() == InventoryType.CHEST && (inventory.getSize() == 9 * 3 || inventory.getSize() == 9 * 6) && inventory.getHolder() instanceof Chest;
	}

	private void addCompass(Player player) {
		if (ConfigStandard.getInstance().doUseItem() && player.hasPermission("bendinggui.command")) {
			ItemStack stack = ConfigStandard.getInstance().getItem();
			if (!player.getInventory().contains(stack)) {
				player.getInventory().addItem(stack);
			}
		}
	}

	public static void chatListen(Player player, Consumer<String> onChat, long timeout, Runnable onTimeout) {
		CHAT_LISTENERS.put(player.getUniqueId(), onChat);

		new BukkitRunnable() {
			@Override
			public void run() {
				//If the player hasn't sent any chat message and the callback hasn't been fired
				if (CHAT_LISTENERS.containsKey(player.getUniqueId()) && CHAT_LISTENERS.get(player.getUniqueId()) == onChat) {
					onTimeout.run();
				}
			}
		}.runTaskLater(BendingGUI.INSTANCE, timeout / 50);
	}
}
