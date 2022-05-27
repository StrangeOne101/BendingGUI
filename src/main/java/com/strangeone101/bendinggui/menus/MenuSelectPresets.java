package com.strangeone101.bendinggui.menus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.util.MultiAbilityManager;
import com.projectkorra.projectkorra.object.Preset;
import com.strangeone101.bendinggui.BendingBoard;
import com.strangeone101.bendinggui.BendingGUI;
import com.strangeone101.bendinggui.DynamicUpdater;
import com.strangeone101.bendinggui.LangBuilder;
import com.strangeone101.bendinggui.Listener;
import com.strangeone101.bendinggui.RunnablePlayer;
import com.strangeone101.bendinggui.api.ElementSupport;
import com.strangeone101.bendinggui.config.ConfigPresets;
import com.strangeone101.bendinggui.spirits.SpiritsSupport;
import me.xnuminousx.spirits.elements.SpiritElement;
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
	public OfflinePlayer thePlayer;

	protected List<Preset> presets;
	protected Set<String> globalPresets = new HashSet<>();

	protected boolean deleteMode = false;

	protected MenuBase previousMenu;

	public MenuSelectPresets(OfflinePlayer player, MenuBase previousMenu)
	{
		super(new LangBuilder("Display.Presets.Title").toString(), getMenuSize(player));
		this.thePlayer = player;
		this.previousMenu = previousMenu;

		update();
	}
	
	public void update() {
		//Load the presets again
		this.presets = Preset.presets.get(thePlayer.getUniqueId());
		if (this.presets == null) this.presets = new ArrayList<>();
		if (thePlayer instanceof Player && ((Player) thePlayer).hasPermission("bending.command.preset.external")) {
			globalPresets = Preset.externalPresets.keySet();
		}

		//Clear the existing stuff
		for (int i = 0; i < this.getInventory().getContents().length; i++) {
			if (this.getInventory().getContents()[i] == null) {
				this.getInventory().clear(i);
			} else if (this.getInventory().getContents()[i].getType() != Material.PLAYER_HEAD) {
				this.getInventory().clear(i);
			}
		}

		//Put the slots at the top
		for (int i = 0; i < 9; i++) {
			this.addMenuItem(getItemForSlot(thePlayer, i + 1), i);
		}

		//Empty placeholder item
		if (globalPresets.size() == 0 && presets.size() == 0) {
			this.addMenuItem(getEmptyPresetThing(), 9);
		} else { //Or all the presets
			int index = 9;
			for (String globalKey : globalPresets) {
				this.addMenuItem(getGlobalPreset(globalKey), index);

				index++;
			}

			for (Preset preset : presets) {
				this.addMenuItem(getPreset(preset), index);

				index++;
			}
		}

		MenuItem item = new MenuItem(ChatColor.YELLOW + new LangBuilder("Display.Common.Page.Back.Title").toString(), Material.ARROW) {
			@Override
			public void onClick(Player player)
			{
				switchMenu(player, previousMenu);
			}
		};
		item.setDescriptions(List.of((ChatColor.GRAY + new LangBuilder("Display.Common.Page.Back.Lore").toString()).split("\n")));
		this.addMenuItem(item, getInventory().getSize() - 9); //Bottom left

		this.addMenuItem(getCreatePreset(), getInventory().getSize() - 5);
		this.addMenuItem(getRemoveToggle(), getInventory().getSize() - 1);
	}

	public MenuItem getGlobalPreset(String preset) {
		HashMap<Integer, String> abilities = new HashMap<>();
		BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(thePlayer);

		for (int i = 0; i < 9; i++) { //Turn the arraylist into a map
			if (Preset.externalPresets.get(preset).size() - 1 <= i) {
				String s = Preset.externalPresets.get(preset).get(i);

				if (s != null && !s.equals("")) //Ignore blank slots
					abilities.put(i + 1, s);
			}
		}

		boolean bound = bPlayer != null && abilities.equals(bPlayer.getAbilities()); //Check the abilities match the current binds

		String name = ChatColor.YELLOW + new LangBuilder("Display.Presets.Global.Title").preset(preset, abilities).toString();
		List<String> lore = new ArrayList<>(List.of((ChatColor.GRAY + new LangBuilder("Display.Presets.Global.Lore").preset(preset, abilities).toString()).split("\n")));

		if (deleteMode) {
			lore.add("");
			lore.add(ChatColor.GRAY + new LangBuilder("Display.Presets.Global.DeleteLore").toString());
		} else if (bound) {
			lore.add("");
			lore.add(ChatColor.GRAY + new LangBuilder("Display.Presets.Global.BoundLore").toString());
		} else {
			lore.add("");
			lore.add(ChatColor.GRAY + new LangBuilder("Display.Presets.Global.BindLore").toString());
		}

		//Adding a 'global' prefix to it will make the material different from player ones with the same name
		Material material = getMaterialFromName("Global" + preset);

		if (ConfigPresets.getInstance().getGlobal().containsKey(preset.toLowerCase(Locale.ROOT))) {
			material = ConfigPresets.getInstance().getGlobal().get(preset.toLowerCase(Locale.ROOT));
		}

		MenuItem item = new MenuItem(name, material) {
			@Override
			public void onClick(Player player) {
				if (!deleteMode && !bound) {
					if (bPlayer != null) {
						bPlayer.setAbilities(abilities);
						BendingBoard.updateBoard((Player) thePlayer);
						((Player) thePlayer).sendMessage(ChatColor.YELLOW + new LangBuilder("Chat.Presets.Bind").preset(preset, abilities).toString());
						update();
					}
				}
			}
		};
		item.setDescriptions(lore);

		if (bound) item.setEnchanted(true);

		return item;
	}

	public MenuItem getPreset(Preset preset) {
		HashMap<Integer, String> abilities = Preset.getPresetContents((Player) thePlayer, preset.getName());
		BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(thePlayer);

		boolean bound = bPlayer != null && abilities.equals(bPlayer.getAbilities()); //Check the abilities match the current binds

		String name = ChatColor.YELLOW + new LangBuilder("Display.Presets.Preset.Title").preset(preset.getName(), abilities).toString();
		List<String> lore = new ArrayList<>(List.of((ChatColor.GRAY + new LangBuilder("Display.Presets.Preset.Lore").preset(preset.getName(), abilities).toString()).split("\n")));

		if (deleteMode) {
			lore.add("");
			lore.add(ChatColor.GRAY + new LangBuilder("Display.Presets.Preset.DeleteLore").toString());
		} else if (bound) {
			lore.add("");
			lore.add(ChatColor.GRAY + new LangBuilder("Display.Presets.Preset.BoundLore").toString());
		} else {
			lore.add("");
			lore.add(ChatColor.GRAY + new LangBuilder("Display.Presets.Preset.BindLore").toString());
		}

		//Get material based randomly on the name
		Material material = getMaterialFromName(name);

		MenuSelectPresets instance = this;

		MenuItem item = new MenuItem(name, material) {
			@Override
			public void onClick(Player player) {
				if (!deleteMode && !bound) {
					if (bPlayer != null) {
						bPlayer.setAbilities(abilities);
						BendingBoard.updateBoard((Player) thePlayer);
						player.sendMessage(ChatColor.YELLOW + new LangBuilder("Chat.Presets.Bind").preset(preset.getName(), abilities).player(thePlayer).toString());
						update();
					}
				} else if (deleteMode) {
					MenuConfirm confirm = new MenuConfirm(instance, new RunnablePlayer() {
						@Override
						public void run(Player player) {
							preset.delete();
							player.sendMessage(ChatColor.YELLOW + new LangBuilder("Chat.Presets.Delete").preset(preset.getName(), abilities).player(thePlayer).toString());
							switchMenu(player, instance);
							instance.deleteMode = !instance.deleteMode;
							instance.update();
						}
					}, new RunnablePlayer() {
						@Override
						public void run(Player player) {
							switchMenu(player, instance);
							instance.update();
						}
					}, context -> context.preset(preset.getName(), abilities).player(thePlayer), "Presets.Delete");

					switchMenu(player, confirm);
				}
			}
		};
		item.setDescriptions(lore);

		if (bound) item.setEnchanted(true);

		return item;
	}

	public MenuItem getItemForSlot(OfflinePlayer player, final int index) {
		Material mat = Material.BLACK_STAINED_GLASS_PANE;
		BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
		String move = null;
		if (bPlayer != null) move = bPlayer.getAbilities().get(index);
		ChatColor c = ChatColor.RED;
		if (move != null && !move.equals("null"))
		{
			Element element = CoreAbility.getAbility(move).getElement();
			if (element instanceof SubElement) element = ((SubElement)element).getParentElement();

			if (BendingGUI.INSTANCE.getSupportedElements().contains(element)) {
				ElementSupport support = BendingGUI.INSTANCE.getSupportedElement(element);
				mat = support.getSlotMaterial();
				c = support.getColor();
			} else {
				c = element == Element.AIR ? ChatColor.GRAY : (element == Element.CHI ? ChatColor.GOLD : (element == Element.EARTH ? ChatColor.GREEN : (element == Element.FIRE ? ChatColor.RED : (element == Element.WATER ? ChatColor.BLUE : (element == Element.AVATAR ? ChatColor.LIGHT_PURPLE : element.getColor())))));
				if (element == Element.AIR) {mat = Material.WHITE_STAINED_GLASS_PANE;}
				else if (element == Element.EARTH) {mat = Material.LIME_STAINED_GLASS_PANE;}
				else if (element == Element.FIRE) {mat = Material.RED_STAINED_GLASS_PANE;}
				else if (element == Element.WATER) {mat = Material.BLUE_STAINED_GLASS_PANE;}
				else if (element == Element.CHI) {mat = Material.YELLOW_STAINED_GLASS_PANE;}
				else {mat = Material.PURPLE_STAINED_GLASS_PANE;}
			}
		}

		String itemname, desc = "";

		if (move == null || move.equals("null"))
		{
			itemname = ChatColor.RED + new LangBuilder("Display.Presets.Slot.Empty.Title").slot(index + 1).toString();
			desc = ChatColor.GRAY + new LangBuilder("Display.Presets.Slot.Empty.Lore").slot(index + 1).toString();
		}
		else
		{
			itemname = ChatColor.YELLOW + new LangBuilder("Display.Presets.Slot.Full.Title").slot(index + 1).ability(CoreAbility.getAbility(move)).toString();
			desc = ChatColor.GRAY + new LangBuilder("Display.Presets.Slot.Full.Lore").slot(index + 1).ability(CoreAbility.getAbility(move)).toString();
		}

		MenuItem item = new Blank(ChatColor.YELLOW + itemname, mat);
		item.setDescriptions(Arrays.asList((ChatColor.GRAY + desc).split("\n")));
		return item;
	}

	public MenuItem getRemoveToggle() {
		String onOff = deleteMode ? "On" : "Off";
		String name = new LangBuilder("Display.Presets.Delete." + onOff + ".Title").toString();
		List<String> lore = List.of(new LangBuilder("Display.Presets.Delete." + onOff + ".Lore").toString().split("\n"));

		MenuItem item = new MenuItem(ChatColor.RED + name, Material.BARRIER) {
			@Override
			public void onClick(Player player) {
				deleteMode = !deleteMode;
				update();
			}
		};
		item.setDescriptions(lore);
		item.setEnchanted(deleteMode);
		return item;
	}

	public MenuItem getCreatePreset() {
		int max = GeneralMethods.getMaxPresets((Player) thePlayer);
		BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(thePlayer);

		String createOrMax = presets.size() >= max ? "Max" : "Create";

		String name = ChatColor.YELLOW + new LangBuilder("Display.Presets." + createOrMax + ".Title").toString();
		List<String> lore = List.of((ChatColor.GRAY + new LangBuilder("Display.Presets." + createOrMax + ".Lore").toString()).split("\n"));

		MenuSelectPresets instance = this;

		MenuItem item = new MenuItem(name, Material.MAP) {
			@Override
			public void onClick(Player player) {
				if (presets.size() >= max) {
					closeMenu(player);
					player.sendMessage(ChatColor.RED + new LangBuilder("Display.Errors.MaxPresets").page(presets.size(), max).player(thePlayer).toString());
					return;
				}

				if (bPlayer.getAbilities().size() == 0) {
					closeMenu(player);
					player.sendMessage(ChatColor.RED + new LangBuilder("Display.Errors.NoBinds").player(thePlayer).toString());
					return;
				}

				String cancel = new LangBuilder("Chat.Presets.Create.CancelInput").toString();
				Consumer<String> consumer = chatMessage -> {
					if (cancel.equalsIgnoreCase(chatMessage)) { //If they entered "cancel" into the chat
						switchMenu(player, instance); //Reopen the preset menu
						return;
					}

					//Make sure no special characters are in it
					if (chatMessage.matches(".*([ |&=?$!<>()\"':;,./`~#@\u00A7\\[\\]]).*")) {
						player.sendMessage(ChatColor.RED + new LangBuilder("Display.Errors.InvalidPresetName").preset(chatMessage, new HashMap<>()).player(thePlayer).toString());
						return;
					}

					//Make sure they can't double up on preset names
					if (Preset.presetExists((Player) thePlayer, chatMessage)) {
						player.sendMessage(ChatColor.RED + new LangBuilder("Display.Errors.DupePreset").preset(chatMessage, new HashMap<>()).player(thePlayer).toString());
						return;
					}

					MenuConfirm confirm = new MenuConfirm(instance, new RunnablePlayer() {
						@Override
						public void run(Player player) {
							Preset newPreset = new Preset(thePlayer.getUniqueId(), chatMessage, bPlayer.getAbilities());
							newPreset.save((Player) thePlayer);
							player.sendMessage(ChatColor.GREEN + new LangBuilder("Chat.Presets.Create.Success").preset(chatMessage, bPlayer.getAbilities()).player(thePlayer).toString());
							switchMenu(player, instance);
							instance.update();
						}
					}, new RunnablePlayer() {
						@Override
						public void run(Player player) {
							switchMenu(player, instance);
							instance.update();
						}
					}, context -> context.player(thePlayer).preset(chatMessage, bPlayer.getAbilities()), "Presets.Create");

					switchMenu(player, confirm);
				};

				player.sendMessage(ChatColor.GREEN + new LangBuilder("Chat.Presets.Create.Prompt").player(thePlayer).toString());
				Listener.chatListen(player, consumer, 30 * 1000, () -> {
					player.sendMessage(ChatColor.RED + new LangBuilder("Chat.Presets.Create.Timeout").player(thePlayer).toString());
				});
				closeMenu(player); //Make them see the chat message so they can enter something in chat
			}
		};

		item.setDescriptions(lore);

		return item;
	}

	public MenuItem getEmptyPresetThing() {
		return new Blank(ChatColor.YELLOW + new LangBuilder("Display.Presets.Empty.Title").toString(), Material.GRAY_DYE);
	}

	public static Map<String, List<Material>> getKeyedDefaults() {
		Map<String, List<Material>> defaults = new HashMap<>();

		List<Material> arena = List.of(Material.BOW, Material.GOLDEN_SWORD, Material.IRON_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD);
		List<Material> survival = List.of(Material.LEATHER_HELMET, Material.IRON_HELMET, Material.GOLDEN_HELMET, Material.DIAMOND_HELMET, Material.TURTLE_HELMET, Material.COMPASS, Material.CLOCK);
		List<Material> water = List.of(Material.POTION, Material.SNOWBALL, Material.WATER_BUCKET, Material.ICE, Material.BLUE_ICE, Material.SNOW_BLOCK);
		List<Material> fire = List.of(Material.FIRE_CHARGE, Material.COAL, Material.BLAZE_POWDER, Material.FLINT_AND_STEEL, Material.NETHER_BRICK);
		List<Material> earth = List.of(Material.GRASS_BLOCK, Material.GRANITE, Material.STONE, Material.GREEN_TERRACOTTA);
		List<Material> air = List.of(Material.FEATHER, Material.STICK, Material.STRING, Material.QUARTZ_BLOCK, Material.GLASS, Material.WHITE_WOOL);
		List<Material> chi = List.of(Material.ARROW, Material.LEATHER_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.IRON_LEGGINGS, Material.FLINT);
		List<Material> metal = List.of(Material.IRON_INGOT, Material.IRON_BLOCK, Material.RAW_IRON, Material.IRON_PICKAXE, Material.IRON_CHESTPLATE);
		List<Material> global = List.of(Material.IRON_SWORD, Material.DIAMOND_SWORD, Material.BOW, Material.SHIELD, Material.SPECTRAL_ARROW);
		List<Material> avatar = List.of(Material.PURPLE_DYE, Material.END_CRYSTAL, Material.NETHER_STAR, Material.BEACON, Material.PURPLE_TERRACOTTA);
		List<Material> spirit = List.of(Material.PURPLE_DYE, Material.BLUE_WOOL, Material.TUBE_CORAL_BLOCK, Material.LAPIS_LAZULI, Material.ENDER_PEARL);

		defaults.put("arena", arena);
		defaults.put("pvp", arena);
		defaults.put("survival", survival);
		defaults.put("water", water);
		defaults.put("fire", fire);
		defaults.put("earth", earth);
		defaults.put("air", air);
		defaults.put("chi", chi);
		defaults.put("metal", metal);
		defaults.put("avatar", avatar);
		defaults.put("spirit", spirit);
		defaults.put("global", global);

		return defaults;
	}

	public static Material getMaterialFromName(String name) {
		Map<String, List<Material>> keywords = ConfigPresets.getInstance().getKeywords();

		List<Material> materials = keywords.get("global");

		if (materials == null) materials = getKeyedDefaults().get("global");

		for (String key : keywords.keySet()) {
			if (name.toLowerCase(Locale.ROOT).contains(key.toLowerCase(Locale.ROOT))) {
				materials = keywords.get(key);
				break;
			}
		}

		Random random = new Random(name.hashCode());
		int pick = random.nextInt(materials.size());

		return materials.get(pick);
	}

	public static int getMenuSize(OfflinePlayer player) {
		int size = 0;
		List presets = Preset.presets.get(player.getUniqueId());
		if (presets != null) size += presets.size();
		if (player instanceof Player && ((Player) player).hasPermission("bending.command.preset.external")) {
			size += Preset.externalPresets.size();
		}

		int rows = size / 9 + 1;

		return Math.min(Math.max(rows, 2), 4) + 2;
	}

	public static Element getPresetElement(Map<Integer, String> abilities) {
		Set<Element> elements = new HashSet<>();
		for (String ability : abilities.values()) {
			 if (ability != null && !ability.equals("")) {
				 CoreAbility coreAbility = CoreAbility.getAbility(ability);

				 if (coreAbility != null) {
					 Element element = coreAbility.getElement();

					 if (element == Element.AVATAR) continue;
					 if (element instanceof SubElement) element = ((SubElement) element).getParentElement();

					 elements.add(element);
				 }
			 }
		}

		if (elements.size() == 0) { //Since Avatar elements aren't stored, 0 elements means only avatar elements are on
			return Element.AVATAR;
		}

		if (elements.size() != 1) {
			boolean spiritTest = true;

			//Test if each element is a spirit element. If it is, set the element to the base spirit element
			for (Element e : elements) {
				if (!SpiritsSupport.isSpiritElement(e)) spiritTest = false;
			}

			if (spiritTest) return SpiritElement.SPIRIT;

			return Element.AVATAR;
		}

		return elements.toArray(new Element[0])[0]; //Return first element
	}

	private class Blank extends MenuItem {
		public Blank(String text, Material icon) {
			super(text, icon);
		}

		@Override
		public void onClick(Player player) {}
	}

}
